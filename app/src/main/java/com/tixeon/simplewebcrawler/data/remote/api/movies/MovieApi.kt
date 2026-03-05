package com.tixeon.simplewebcrawler.data.remote.api.movies

import com.tixeon.simplewebcrawler.data.remote.model.movies.MovieResult
import retrofit2.http.GET


interface MovieApi {

    @GET("movie/popular")
    fun getSimilarMovies(movieId: Int): MovieResult
}