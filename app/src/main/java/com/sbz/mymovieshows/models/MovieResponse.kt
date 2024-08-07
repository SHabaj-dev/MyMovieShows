package com.sbz.mymovieshows.models

data class MovieResponse(
    val dates: Dates,
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)