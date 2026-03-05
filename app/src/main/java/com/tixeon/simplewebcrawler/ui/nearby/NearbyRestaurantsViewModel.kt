package com.tixeon.simplewebcrawler.ui.nearby

import com.tixeon.simplewebcrawler.data.remote.api.restaurants.NearbyRestaurantApi
import com.tixeon.simplewebcrawler.data.remote.model.restaurants.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class NearbyRestaurantsViewModel(
    private val nearbyRestaurantApi: NearbyRestaurantApi
) {
    private val _restaurants: MutableStateFlow<RestaurantView> = MutableStateFlow(RestaurantView.Loading)
    val restaurants =  _restaurants.asStateFlow()

    suspend fun loadRestaurants(page: Int) = withContext(Dispatchers.IO) {
        _restaurants.value = RestaurantView.Loading
        val result = nearbyRestaurantApi.fetchRestaurants(page)

        _restaurants.value = RestaurantView.Result(result.data)

        return@withContext restaurants
    }
}


sealed class RestaurantView {
    data object Loading: RestaurantView()
    data class Result(val restaurants: List<Restaurant>): RestaurantView()
    data class Error(val err: String): RestaurantView()
}