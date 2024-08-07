package com.sbz.mymovieshows.ui.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbz.mymovieshows.models.MovieResponse
import com.sbz.mymovieshows.repository.MoviesRepository
import com.sbz.mymovieshows.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(
    private val repository: MoviesRepository
) : ViewModel() {

    val nowPlayingMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var nowPlayingMoviesPageNo = 1

    val popularMoviesList: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var popularPageNumber = 1


    val topRatedMoviesList: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var topRatedPageNumber = 1
    var topRatedMoviesResponse: MovieResponse? = null

    init {
        getNowPlayingMovies()
        getPopularMovies()
        getTopRatedMovies()
    }


    private fun getNowPlayingMovies() = viewModelScope.launch {
        nowPlayingMovies.postValue(Resource.Loading())
        val response = repository.getNowPlayingMovies(nowPlayingMoviesPageNo)
        nowPlayingMovies.postValue(handleNowPlayingMoviesResponse(response))
    }

    private fun getPopularMovies() = viewModelScope.launch {
        popularMoviesList.postValue(Resource.Loading())
        val response = repository.getPopularMovies(popularPageNumber)
        popularMoviesList.postValue(handlePopularMovieResponse(response))
    }


     fun getTopRatedMovies() = viewModelScope.launch {
        topRatedMoviesList.postValue(Resource.Loading())
        val response = repository.getTopRatedMovies(topRatedPageNumber)
        topRatedMoviesList.postValue(handleTopRatedResponse(response))
    }

    private fun handleTopRatedResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                topRatedPageNumber++
                if(topRatedMoviesResponse == null){
                    topRatedMoviesResponse = resultResponse
                }else{
                    val oldMoviesList = topRatedMoviesResponse?.results
                    val newArticles = resultResponse.results
                    oldMoviesList?.addAll(newArticles)
                }
                return Resource.Success(topRatedMoviesResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleNowPlayingMoviesResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handlePopularMovieResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }

        return Resource.Error(response.message())
    }
}