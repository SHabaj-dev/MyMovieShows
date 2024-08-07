package com.sbz.mymovieshows.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.sbz.mymovieshows.databinding.FragmentHomeBinding
import com.sbz.mymovieshows.repository.MoviesRepository
import com.sbz.mymovieshows.ui.ViewModelProviderFactory
import com.sbz.mymovieshows.ui.activitys.movieInfo.MovieInfoActivity
import com.sbz.mymovieshows.ui.dialogs.LoadingDialog
import com.sbz.mymovieshows.ui.fragments.home.adapters.LatestMoviesViewPagerAdapter
import com.sbz.mymovieshows.ui.fragments.home.adapters.TopRatedMoviesAdapter
import com.sbz.mymovieshows.ui.fragments.home.adapters.ZoomOutPageTransformer
import com.sbz.mymovieshows.utils.Constants.QUERY_PAGE_SIZE
import com.sbz.mymovieshows.utils.Resource

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val sliderHandler = Handler()
    private lateinit var homeViewModel: HomeViewModel
    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }
    private lateinit var topRatedMoviesAdapter: TopRatedMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val moviesRepository = MoviesRepository()
        val viewModelProviderFactory = ViewModelProviderFactory(moviesRepository)

        homeViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        topRatedMoviesAdapter = TopRatedMoviesAdapter()
        setUpRecyclerView()
        setUpNestedScrollView()

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val viewPagerAdapter = LatestMoviesViewPagerAdapter(binding.viewPager)
        val popularMoviesAdapter = LatestMoviesViewPagerAdapter(binding.viewPagerPopularMovies)

        binding.lottieAnimation.setOnClickListener {
            startActivity(Intent(requireActivity(), MovieInfoActivity::class.java))
        }

        homeViewModel.nowPlayingMovies.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoadingDialog()
                    response.data?.let { movieResponse ->
                        viewPagerAdapter.differ.submitList(movieResponse.results)
                    }
                }

                is Resource.Error -> {
                    hideLoadingDialog()
                    response.message?.let { message ->
                        Toast.makeText(
                            requireActivity(),
                            "Something went wrong in nowPlayingMovies",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is Resource.Loading -> {
                    showLoadingDialog()
                }
            }
        })

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer(ZoomOutPageTransformer())

        binding.viewPager.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = 3
            clipChildren = false
            clipToPadding = false

            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(transformer)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRunnable)
                    sliderHandler.postDelayed(sliderRunnable, 2000)
                }

            })
        }

        homeViewModel.popularMoviesList.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    hideLoadingDialog()
                    response.data?.let { responseData ->
                        popularMoviesAdapter.differ.submitList(responseData.results)
                    }
                }

                is Resource.Error -> {
                    hideLoadingDialog()
                    response.message?.let { message ->
                        Toast.makeText(
                            requireActivity(),
                            "Something went wrong in nowPlayingMovies",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is Resource.Loading -> {
                    showLoadingDialog()
                }
            }

        })

        binding.viewPagerPopularMovies.apply {
            adapter = popularMoviesAdapter
            offscreenPageLimit = 3
            clipChildren = false
            clipToPadding = false

            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(transformer)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRunnable)
                    sliderHandler.postDelayed(sliderRunnable, 2000)
                }

            })
        }

        homeViewModel.topRatedMoviesList.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    hideLoadingDialog()
                    response.data?.let { responseList ->
                        topRatedMoviesAdapter.differ.submitList(responseList.results)

                    }
                }

                is Resource.Error -> {
                    hideLoadingDialog()
                    response.message?.let { message ->
                        Toast.makeText(
                            requireActivity(),
                            "Something went wrong!!! Please Try Again",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d("HOME_FRAGMENT", "Error in TopRatedMovies : $message")
                    }
                }

                is Resource.Loading -> showLoadingDialog()
            }

        })

    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false


    val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            paginationLogic()
        }
    }

    private fun paginationLogic() {
        val layoutManager = binding.rvTopRated.layoutManager as GridLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount

        val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
        val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
        val isNotAtBeginning = firstVisibleItemPosition >= 0
        val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
        val shouldPaginate =
            isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

        Log.d("SHABAJ", "shouldPaginate = $shouldPaginate")

        if (!shouldPaginate) {
            homeViewModel.getTopRatedMovies()
            isScrolling = false
        }
    }


    private fun setUpRecyclerView() {
        Log.d("HOME_FRAGMENT", "Inside the RecyclerViewInitializer")
        binding.rvTopRated.apply {
            layoutManager = GridLayoutManager(requireActivity(), 3, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = topRatedMoviesAdapter
            addOnScrollListener(scrollListener)
        }
    }


    private val sliderRunnable = Runnable {
        binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1)
    }


    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showLoadingDialog() {
        loadingDialog.show()
    }

    private fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }


    private fun setUpNestedScrollView() {
        binding.nestedScrollView.apply {
            setFadingEdgeLength(50)
            isVerticalFadingEdgeEnabled = true

        }

        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY > oldScrollY && scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) {
                    paginationLogic()
                }
            }
        })


    }

}