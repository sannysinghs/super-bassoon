package com.tixeon.simplewebcrawler.data.remote.api.crawler

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.net.URL

class JsoupWebCrawlerAgent(
    private val userAgent: String,
) : WebCrawlerAgent {
    override suspend fun crawl(url: URL): List<URL> = withContext(Dispatchers.IO) {
        val res: Connection.Response =
            Jsoup.connect(url.toString())
                .method(Connection.Method.GET)
                .userAgent(userAgent)
                .header("Accept", "text/html")
                .followRedirects(false)
                .execute()


        if (res.hasHeaderWithValue("X-Robots-Tag", "noindex")) {
            return@withContext emptyList()
        }

        val doc = res.parse()

        return@withContext doc.body().getElementsByTag("a")
            .asSequence()
            .filter { a -> isHypertext(a.attr("href")) }
            .map { a -> URL(a.attr("href")) }
            .filter { it.host == url.host }
            .toSet()
            .toList()
    }
}