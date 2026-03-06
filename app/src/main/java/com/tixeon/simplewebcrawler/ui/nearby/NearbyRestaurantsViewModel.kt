package com.tixeon.simplewebcrawler.ui.nearby

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tixeon.simplewebcrawler.data.remote.model.restaurants.Restaurant
import com.tixeon.simplewebcrawler.data.repository.RestaurantRepository
import com.tixeon.simplewebcrawler.utils.AppDispatchers
import com.tixeon.simplewebcrawler.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearbyRestaurantsViewModel @Inject constructor(
    private val repository: RestaurantRepository,
    private val dispatchers: AppDispatchers,
): ViewModel() {

    var currentPage by mutableIntStateOf(1)
    private val _restaurants: MutableStateFlow<RestaurantView> =
        MutableStateFlow(RestaurantView.Loading)

    val restaurants = _restaurants.asStateFlow()

    private val resData: MutableList<Restaurant> = mutableListOf()


    fun loadRestaurants() = viewModelScope.launch(dispatchers.io()) {
        if (currentPage == 1) {
            _restaurants.value = RestaurantView.Loading
        } else {
            _restaurants.value = RestaurantView.Result(resData, true)
        }

        when (val result = repository.getRestaurants(currentPage)) {
            is Resource.Success -> {
                if (currentPage < result.data.totalPages) {
                    currentPage++
                }

                resData.addAll(result.data.data)
                _restaurants.value = RestaurantView.Result(resData, false)
            }
            is Resource.Error -> _restaurants.value = RestaurantView.Error(result.message)
        }
    }
}


sealed class RestaurantView {
    data object Loading : RestaurantView()
    data class Result(
        val restaurants: List<Restaurant>,
        val isLoadMore: Boolean,
    ) : RestaurantView()
    data class Error(val err: String) : RestaurantView()
}