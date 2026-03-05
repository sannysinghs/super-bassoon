package com.tixeon.simplewebcrawler.data.remote.model.crawler

data class RootUrl(
    val name: String,
    val childUrls: List<String>
)