package com.tixeon.simplewebcrawler.ui.crawler

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.tixeon.simplewebcrawler.ui.CrawlerViewModel
import com.tixeon.simplewebcrawler.ui.UrlTile

@Composable
fun CrawlerMainContent(
    modifier: Modifier,
    viewModel: CrawlerViewModel,
) {

    val urls = viewModel.urls.collectAsState()
    LazyColumn(modifier = modifier) {
        item {
            Text(
                "Simple Web Crawler",
                style = MaterialTheme.typography.titleLarge
            )
        }

        item {
            PostNewCrawl {
                viewModel.loadWebContent("https://www.google.com")
            }
        }

        items(urls.value) {
            UrlTile(it.name, totalLinks = it.totalLinks)
        }
    }
}

@Composable
fun PostNewCrawl(
    onNewWebContent: () -> Unit,
) {
    Row {
        Button(content = { Text("Post New Crawl") }, onClick = onNewWebContent)
    }
}
