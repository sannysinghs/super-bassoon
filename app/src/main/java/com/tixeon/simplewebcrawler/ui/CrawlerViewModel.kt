package com.tixeon.simplewebcrawler.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tixeon.simplewebcrawler.data.remote.api.crawler.SimpleWebCrawler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.URL

class CrawlerViewModel(
    private val api: SimpleWebCrawler
): ViewModel() {
    private val _urls = MutableStateFlow<List<UrlItem>>(emptyList())
    val urls: StateFlow<List<UrlItem>> = _urls

    init {
        _urls.value = listOf()
    }

    fun loadWebContent(url: String) {
        if (url.isBlank()) return
        viewModelScope.launch {
            api.crawl(URL(url)).collectLatest { result ->
                _urls.value = result.map {
                    UrlItem(it.name, it.childUrls.size)
                }
            }
        }
    }
}


data class UrlItem(
    val name: String,
    val totalLinks: Int,
)