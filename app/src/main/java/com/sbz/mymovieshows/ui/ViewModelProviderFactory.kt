package com.sbz.mymovieshows.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sbz.mymovieshows.repository.MoviesRepository
import com.sbz.mymovieshows.ui.fragments.home.HomeViewModel

class ViewModelProviderFactory(
    private val moviesRepository: MoviesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(moviesRepository) as T
    }

}