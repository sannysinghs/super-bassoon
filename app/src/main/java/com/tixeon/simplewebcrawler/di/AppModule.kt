package com.tixeon.simplewebcrawler.di

import com.tixeon.simplewebcrawler.data.remote.api.restaurants.NearbyRestaurantApi
import com.tixeon.simplewebcrawler.data.remote.api.restaurants.NearbyRestaurantsApiImplementation
import com.tixeon.simplewebcrawler.utils.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesAppDispatchers() = object : AppDispatchers {
        override fun io(): CoroutineDispatcher = Dispatchers.IO
        override fun main(): CoroutineDispatcher = Dispatchers.Main
        override fun default(): CoroutineDispatcher = Dispatchers.Default
    }

    @Provides
    @Singleton
    fun providesNearbyRestaurantsApi(
        impl: NearbyRestaurantsApiImplementation
    ): NearbyRestaurantApi = impl
}