package com.tixeon.simplewebcrawler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.tixeon.simplewebcrawler.data.remote.api.crawler.BasicHttpCrawlerAgent
import com.tixeon.simplewebcrawler.data.remote.api.crawler.SimpleWebCrawler
import com.tixeon.simplewebcrawler.data.remote.api.restaurants.NearbyRestaurantsApiImplementation
import com.tixeon.simplewebcrawler.ui.CrawlerViewModel
import com.tixeon.simplewebcrawler.ui.crawler.CrawlerMainContent
import com.tixeon.simplewebcrawler.ui.nearby.NearbyRestaurantsMainContent
import com.tixeon.simplewebcrawler.ui.nearby.NearbyRestaurantsViewModel
import com.tixeon.simplewebcrawler.ui.theme.SimpleWebCrawlerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleWebCrawlerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = remember {
                        CrawlerViewModel(
                            api = SimpleWebCrawler(
                                crawlerAgent = BasicHttpCrawlerAgent(
                                    userAgent = "MacOS/1.0",
                                )
                            )
                        )
                    }

                    val nearbyVm = remember {
                        NearbyRestaurantsViewModel(
                            nearbyRestaurantApi = NearbyRestaurantsApiImplementation()
                        )
                    }
                    NearbyRestaurantsMainContent(
                        modifier = Modifier.padding(innerPadding),
                        vm = nearbyVm,
                    )
                }
            }
        }
    }
}