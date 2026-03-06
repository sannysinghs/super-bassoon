package com.tixeon.simplewebcrawler.data.repository

import com.tixeon.simplewebcrawler.data.remote.api.restaurants.NearbyRestaurantApi
import com.tixeon.simplewebcrawler.data.remote.model.restaurants.NearbyRestaurantResult
import com.tixeon.simplewebcrawler.utils.AppDispatchers
import com.tixeon.simplewebcrawler.utils.Resource
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class RestaurantRepository @Inject constructor(
    private val api: NearbyRestaurantApi,
    private val dispatchers: AppDispatchers,
) {
    suspend fun getRestaurants(
        page: Int,
    ): Resource<NearbyRestaurantResult> {
        return try {
            Resource.Success(
                withContext(dispatchers.io()) {
                    api.fetchRestaurants(page)
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