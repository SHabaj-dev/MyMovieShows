package com.sbz.mymovieshows.ui.activitys.movieInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sbz.mymovieshows.repository.MovieInfoRepository

class MovieInfoViewModelProviderFactory(
    private val movieInfoRepository: MovieInfoRepository,
    private val movieId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieInfoViewModel(movieInfoRepository, movieId) as T
    }
}