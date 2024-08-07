package com.sbz.mymovieshows.ui.activitys.movieInfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbz.mymovieshows.models.MovieInfoResponse
import com.sbz.mymovieshows.repository.MovieInfoRepository
import com.sbz.mymovieshows.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MovieInfoViewModel(
    private val movieInfoRepository: MovieInfoRepository,
    private val movieId: Int
) : ViewModel() {

    val movieInfo: MutableLiveData<Resource<MovieInfoResponse>> = MutableLiveData()


    init {
        getMovieInfo()
    }


    private fun getMovieInfo() = viewModelScope.launch {
        movieInfo.postValue(Resource.Loading())
        val response = movieInfoRepository.getMovieInfo(movieId)
        movieInfo.postValue(handleMovieInfoResponse(response))

    }

    private fun handleMovieInfoResponse(response: Response<MovieInfoResponse>): Resource<MovieInfoResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

}