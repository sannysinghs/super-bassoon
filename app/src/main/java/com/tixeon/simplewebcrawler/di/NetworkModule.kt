package com.tixeon.simplewebcrawler.di

import android.content.Context
import com.tixeon.simplewebcrawler.data.remote.api.movies.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val MAX_CACHE = 10L * 1024 * 1024 // 10 MB

    @Singleton
    @Provides
    fun providesInterceptor(): Interceptor = Interceptor { chain ->
        val url = chain.request()
            .url()
            .newBuilder()
            .addQueryParameter("api_key", "55957fcf3ba81b137f8fc01ac5a31fb5")
            .addQueryParameter("language", "en-US")
            .build()


        val request = chain.request()
            .newBuilder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .build()

        chain.proceed(request)
    }

    @Singleton
    @Provides
    fun providesHttpClient(
        @ApplicationContext context: Context,
        interceptor: Interceptor
    ): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(interceptor)
        .cache(Cache(context.cacheDir, MAX_CACHE))
        .build()


    @Singleton
    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit
        .Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun providesMovieApi(
        retrofit: Retrofit
    ): MovieApi = retrofit.create(MovieApi::class.java)
}