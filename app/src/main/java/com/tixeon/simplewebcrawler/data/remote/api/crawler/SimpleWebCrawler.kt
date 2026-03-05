package com.tixeon.simplewebcrawler.data.remote.api.crawler

import java.net.URL
import com.tixeon.simplewebcrawler.data.remote.model.crawler.RootUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentLinkedQueue


class SimpleWebCrawler(private val crawlerAgent: WebCrawlerAgent) {

    suspend fun crawl(url: URL): Flow<List<RootUrl>> = withContext(Dispatchers.IO) {
        val results = mutableListOf<RootUrl>()
        val crawled = mutableSetOf<URL>()
        val queue = ConcurrentLinkedQueue<URL>()
        queue.add(url)

        while (queue.isNotEmpty()) {
            val currentUrl = queue.poll() ?: continue

            if (currentUrl !in crawled) {
                crawlerAgent.crawl(currentUrl).also { urls ->
                    results.add(RootUrl(currentUrl.toString(), urls.map { it.toString() }))
                    queue.addAll(urls)
                }
                crawled.add(currentUrl)
            }
        }

        return@withContext flowOf(results)
    }

    private fun isSameDomain(s: String): Boolean = true
}
