package com.sbz.mymovieshows.api

import com.sbz.mymovieshows.models.MovieInfoResponse
import com.sbz.mymovieshows.models.MovieResponse
import com.sbz.mymovieshows.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {


    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") pageNumber: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") pageNumber: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieResponse>


    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") pageNumber: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieResponse>


    @GET("movie/{movie_id}")
    suspend fun getMovieInfo(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieInfoResponse>


}