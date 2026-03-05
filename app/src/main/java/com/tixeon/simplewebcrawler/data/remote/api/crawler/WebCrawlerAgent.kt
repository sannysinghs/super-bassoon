package com.tixeon.simplewebcrawler.data.remote.api.crawler

import java.net.URL
import java.util.regex.Pattern

interface WebCrawlerAgent {
    suspend fun crawl(url: URL): List<URL>
}

internal fun isHypertext(href: String): Boolean =
    href.startsWith("http") || href.startsWith("https")

// Pattern to extract URLs from HTML content
internal val urlPattern = Pattern.compile(
    "href=[\"'](https?://[^\"']+)[\"']",
    Pattern.CASE_INSENSITIVE
)