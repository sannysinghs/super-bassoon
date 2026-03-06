package com.tixeon.simplewebcrawler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tixeon.simplewebcrawler.ui.nearby.NearbyRestaurantsMainContent
import com.tixeon.simplewebcrawler.ui.nearby.NearbyRestaurantsViewModel
import com.tixeon.simplewebcrawler.ui.theme.SimpleWebCrawlerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val restaurantViewModel: NearbyRestaurantsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleWebCrawlerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewState by restaurantViewModel.restaurants.collectAsState()

                    LaunchedEffect(Unit) {
                        restaurantViewModel.loadRestaurants()
                    }

                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        NearbyRestaurantsMainContent(
                            modifier = Modifier.padding(8.dp),
                            viewState = viewState,
                            loadMore = {
                                restaurantViewModel.loadRestaurants()
                            }
                        )
                    }
                }
            }
        }
    }
}