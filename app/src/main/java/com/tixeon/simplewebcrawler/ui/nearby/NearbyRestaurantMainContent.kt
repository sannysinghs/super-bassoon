package com.tixeon.simplewebcrawler.ui.nearby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NearbyRestaurantsMainContent(
    viewState: RestaurantView,
    loadMore: () -> Unit,
    modifier: Modifier,
) {

    when (viewState) {
        is RestaurantView.Result -> NearbyRestaurantsListing(
            modifier,
            viewState,
            loadMore = loadMore
        )

        is RestaurantView.Loading -> {
            Text("Loading: Restaurants...")
        }

        is RestaurantView.Error -> {
            Text("Error: ${viewState.err}")
        }
    }

}

@Composable
private fun NearbyRestaurantsListing(
    modifier: Modifier,
    viewState: RestaurantView.Result,
    loadMore: () -> Unit,
) {
    val lazyColumnListState = rememberLazyListState()
    val shouldStartPaginate by remember {
        derivedStateOf {
            (lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -9) >= (lazyColumnListState.layoutInfo.totalItemsCount - 6)
        }
    }

    LaunchedEffect(shouldStartPaginate) {
        if (shouldStartPaginate && !viewState.isLoadMore) {
            loadMore()
        }
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyColumnListState,
    ) {
        items(viewState.restaurants, key = { it.id }) {
            Column {
                Text(it.name, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                Text("Location: ${it.city}", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
                Text("Estimated: ${it.estimatedCost}", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
                Text("Average: ${it.rating.averageRating}", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
                Text("Count: ${it.rating.votes}", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)

                HorizontalDivider()
            }
        }
    }
}
