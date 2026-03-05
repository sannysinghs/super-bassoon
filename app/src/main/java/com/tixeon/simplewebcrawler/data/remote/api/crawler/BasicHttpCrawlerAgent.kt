package com.tixeon.simplewebcrawler.data.remote.api.crawler

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class BasicHttpCrawlerAgent(private val userAgent: String) : WebCrawlerAgent {
    override suspend fun crawl(url: URL): List<URL> = withContext(Dispatchers.IO) {
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.apply {
                requestMethod = "GET"
                readTimeout = 10000
                connectTimeout = 10000

                setRequestProperty("User-Agent", userAgent)
                setRequestProperty("Accept", "text/html")
            }

            val links = mutableListOf<URL>()
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val content: String =
                    BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                        reader.readText()
                    }

                val matcher = urlPattern.matcher(content)
                while (matcher.find()) {
                    val link = URL(matcher.group(1))
                    if (link.host == url.host) {
                        links.add(link)
                    }
                }
            } else {
                println("Http Error: ${connection.responseCode} ${connection.responseMessage}")
                connection.errorStream?.run {
                    BufferedReader(InputStreamReader(this)).use {
                        val sb = StringBuilder()
                        var errorLine = ""
                        while (readlnOrNull()?.also { errorLine = it } != null) {
                            sb.append(errorLine)
                        }

                        println(errorLine)
                    }
                }
            }

            return@withContext links
        } catch (e: FileNotFoundException) {
            // Handle the specific FileNotFoundException
            println("File Not Found Error: ${e.message}")
            emptyList()
        } catch (e: IOException) {
            // Handle other IO exceptions (e.g., network issues)
            println("IO Error: ${e.message}")
            emptyList()
        } finally {
            connection.disconnect()
        }


    }
}