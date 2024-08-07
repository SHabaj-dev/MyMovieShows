package com.sbz.mymovieshows.ui.activitys.movieInfo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sbz.mymovieshows.databinding.ActivityMovieInfoBinding
import com.sbz.mymovieshows.models.MovieInfoResponse
import com.sbz.mymovieshows.repository.MovieInfoRepository
import com.sbz.mymovieshows.utils.Resource

class MovieInfoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMovieInfoBinding.inflate(layoutInflater)
    }
    private lateinit var movieInfoViewModel: MovieInfoViewModel
    private var movieId: Int = 0
    private val TAG = "SHABAJ"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        movieId = 519182

        val movieInfoRepository = MovieInfoRepository()
        val movieInfoViewModelProvider =
            MovieInfoViewModelProviderFactory(movieInfoRepository, movieId)
        movieInfoViewModel =
            ViewModelProvider(this, movieInfoViewModelProvider)[MovieInfoViewModel::class.java]

        movieInfoViewModel.movieInfo.observe(this, Observer { response ->
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

        binding.tvMovieDescription.text = movieInfo.overview

    }
}