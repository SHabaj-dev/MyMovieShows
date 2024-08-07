package com.sbz.mymovieshows.repository

import com.sbz.mymovieshows.api.RetrofitInstance

class MoviesRepository {

    suspend fun getNowPlayingMovies(pageNumber: Int) =
        RetrofitInstance.api.getNowPlayingMovies(pageNumber)

    suspend fun getPopularMovies(pageNumber: Int) =
        RetrofitInstance.api.getPopularMovies(pageNumber)


    suspend fun getTopRatedMovies(pageNumber: Int) =
        RetrofitInstance.api.getTopRatedMovies(pageNumber)

}