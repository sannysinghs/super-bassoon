package com.tixeon.simplewebcrawler.data.remote.model.restaurants

import android.media.Rating

data class NearbyRestaurantResult(
    val page: Int,
    val totalPages: Int,
    val data: List<Restaurant>,
)

data class Restaurant(
    val city: String,
    val name: String,
    val estimatedCost: Double,
    val id: Int,
    val rating: UserRating,
)

data class UserRating(
    val averageRating: Double,
    val votes: Long,
)