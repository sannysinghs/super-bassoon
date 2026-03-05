package com.tixeon.simplewebcrawler.data.remote.model.movies

data class MovieResult(
    val results: List<Movie>,
    val totalResults: Int

)

data class Movie(
    val id: Int
)