package com.sbz.mymovieshows.repository

import com.sbz.mymovieshows.api.RetrofitInstance

class MovieInfoRepository {


    suspend fun getMovieInfo(movieId: Int) =
        RetrofitInstance.api.getMovieInfo(movieId)

    suspend fun getCast(movieId: Int) =
        RetrofitInstance.api.getCast(movieId)

}