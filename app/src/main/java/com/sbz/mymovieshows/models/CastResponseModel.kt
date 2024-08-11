package com.sbz.mymovieshows.models

data class CastResponseModel(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)