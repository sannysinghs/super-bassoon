package com.tixeon.simplewebcrawler.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface AppDispatchers {
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}