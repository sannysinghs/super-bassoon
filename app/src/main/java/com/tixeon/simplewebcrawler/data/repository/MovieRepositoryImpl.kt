package com.tixeon.simplewebcrawler.data.repository

import com.tixeon.simplewebcrawler.data.remote.api.movies.MovieApi
import com.tixeon.simplewebcrawler.data.remote.model.movies.MovieResult
import com.tixeon.simplewebcrawler.domain.repository.MovieRepository
import com.tixeon.simplewebcrawler.utils.AppDispatchers
import com.tixeon.simplewebcrawler.utils.Resource
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val dispatcher: AppDispatchers
) : MovieRepository {

    override suspend fun getSimilarMovies(movieId: Int): Resource<MovieResult> {
        return try {
            Resource.Success(
                withContext(dispatcher.io()) {
                    movieApi.getSimilarMovies(movieId)
                }
            )
        } catch (e: Exception) {
            Resource.Error(
                when (e) {
                    is IOException -> "Unable to fetch data, please check your internet connection and try again."
                    is HttpException -> "API service is not responding at the moment, please try again later."
                    else -> e.message ?: "Something went wrong, please try again."
                }
            )
        }
    }
}