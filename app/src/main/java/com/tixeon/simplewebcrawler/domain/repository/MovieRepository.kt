package com.tixeon.simplewebcrawler.domain.repository

import com.tixeon.simplewebcrawler.data.remote.model.movies.MovieResult
import com.tixeon.simplewebcrawler.utils.Resource

interface MovieRepository {
    suspend fun getSimilarMovies(movieId: Int): Resource<MovieResult>
}