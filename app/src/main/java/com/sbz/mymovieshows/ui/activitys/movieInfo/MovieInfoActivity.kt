package com.sbz.mymovieshows.ui.activitys.movieInfo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sbz.mymovieshows.R
import com.sbz.mymovieshows.databinding.ActivityMovieInfoBinding
import com.sbz.mymovieshows.models.MovieInfoResponse
import com.sbz.mymovieshows.repository.MovieInfoRepository
import com.sbz.mymovieshows.ui.activitys.movieInfo.adapters.CastAdapter
import com.sbz.mymovieshows.ui.activitys.movieInfo.adapters.GenresAdapter
import com.sbz.mymovieshows.utils.Constants.IMAGE_BASE_URL
import com.sbz.mymovieshows.utils.Resource

class MovieInfoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMovieInfoBinding.inflate(layoutInflater)
    }
    private lateinit var movieInfoViewModel: MovieInfoViewModel
    private var movieId: Int = 0
    private val genresAdapter by lazy {
        GenresAdapter()
    }
    private val castAdapter by lazy {
        CastAdapter()
    }


    private val TAG = "SHABAJ"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        movieId = intent.getIntExtra(getString(R.string.movie_id), 0)

        val movieInfoRepository = MovieInfoRepository()
        val movieInfoViewModelProvider =
            MovieInfoViewModelProviderFactory(movieInfoRepository, movieId)
        movieInfoViewModel =
            ViewModelProvider(this, movieInfoViewModelProvider)[MovieInfoViewModel::class.java]

        binding.rvGenres.apply {
            layoutManager =
                LinearLayoutManager(this@MovieInfoActivity, RecyclerView.HORIZONTAL, false)
            adapter = genresAdapter
        }

        binding.rvCast.apply {
            layoutManager =
                GridLayoutManager(this@MovieInfoActivity, 2, RecyclerView.HORIZONTAL, false)
            adapter = castAdapter
            setFadingEdgeLength(50)
            isHorizontalFadingEdgeEnabled = true
        }

        getMovieInfo()
        getCastInfo()



    }

    private fun getCastInfo() {
        movieInfoViewModel.movieCast.observe(this@MovieInfoActivity, Observer { response ->
            when (response) {

                is Resource.Success -> {
                    response.data?.let { result ->
                        castAdapter.differ.submitList(result.cast)
                    }
                }

                is Resource.Error -> {
                    Log.d(TAG, "onCreate: INSIDE CAST error ${response.message}")
                }

                is Resource.Loading -> {
                    Log.d(TAG, "INSIDE LOADING CAST")
                }
            }
        })
    }

    private fun getMovieInfo() {
        movieInfoViewModel.movieInfo.observe(this@MovieInfoActivity, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        setDataToView(response.data)
                    }
                }

                is Resource.Error -> {
                    Log.d(TAG, "onCreate: INSDIE error ${response.message}")
                }

                is Resource.Loading -> {
                    Log.d(TAG, "INSIDE LOADING")
                }
            }
        })
    }


    private fun setDataToView(movieInfo: MovieInfoResponse) {
        val posterImageUrl = IMAGE_BASE_URL + movieInfo.poster_path

        binding.toolbar.setTitle(movieInfo.original_title)
        binding.tvMovieDescription.text = movieInfo.overview
        Glide.with(this@MovieInfoActivity).load(posterImageUrl).into(binding.ivMoviePoster)
        genresAdapter.differ.submitList(movieInfo.genres)
        binding.tvReleaseDate.text = movieInfo.release_date
        binding.tvRunTime.text = getRunTimeInHoursAndMinutes(movieInfo.runtime)
        binding.tvRating.text = getString(R.string.rating, movieInfo.vote_average)

    }


    private fun getRunTimeInHoursAndMinutes(runTime: Int): String {
        val hour: Int = runTime / 60
        val minutes: Int = runTime % 60
        return "${hour}h:${minutes}m"
    }
}