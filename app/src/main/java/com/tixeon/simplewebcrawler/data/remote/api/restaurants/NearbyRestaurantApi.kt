package com.tixeon.simplewebcrawler.data.remote.api.restaurants

import com.google.gson.Gson
import com.tixeon.simplewebcrawler.data.remote.model.restaurants.NearbyRestaurantResult
import com.tixeon.simplewebcrawler.data.remote.model.restaurants.Restaurant
import com.tixeon.simplewebcrawler.data.remote.model.restaurants.UserRating
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

interface NearbyRestaurantApi {
    suspend fun fetchRestaurants(page: Int): NearbyRestaurantResult
}

private const val BASE_URL_NEARBY_RESTAURANTS = "https://jsonmock.hackerrank.com/api/food_outlets"
private const val MAX_READ_TIMEOUT = 10000
private const val MAX_CONNECTION_TIMEOUT = 10000
private const val JSON_KEY_DATA = "data"
private const val JSON_KEY_PAGE = "page"
private const val JSON_KEY_TOTAL_PAGES = "total-pages"

class NearbyRestaurantsApiImplementation: NearbyRestaurantApi {
    override suspend fun fetchRestaurants(page: Int): NearbyRestaurantResult = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL_NEARBY_RESTAURANTS?page=$page")

        val connection = url.openConnection() as HttpURLConnection
        connection.apply {
            requestMethod = "GET"
            readTimeout = MAX_READ_TIMEOUT
            connectTimeout = MAX_CONNECTION_TIMEOUT

            addRequestProperty("Content-Type", "application/json;charset=UTF-8")
        }

        if (connection.responseCode != HttpURLConnection.HTTP_OK)
            throw Exception("Http Error: ${connection.responseCode} ${connection.responseMessage}")

        val jsonRes = StringBuilder()
        BufferedReader(InputStreamReader(connection.inputStream)).use {
            var res: String?
            while ((it.readLine().also { line -> res = line }) != null) {
                jsonRes.append(res)
            }
        }

        val jsonObject = JSONObject(jsonRes.toString())

        val pageNum = jsonObject.getInt(JSON_KEY_PAGE)
        val totalPage = jsonObject.getInt(JSON_KEY_TOTAL_PAGES)
        val data = jsonObject.getJSONArray(JSON_KEY_DATA)

        val restaurantList = (0 until data.length()).map {
            val restaurant = data.getJSONObject(it)
            val rating = restaurant.getJSONObject("user_rating")
            try {
                Restaurant(
                    id = restaurant.getInt("id"),
                    name = restaurant.getString("name"),
                    city = restaurant.getString("city"),
                    estimatedCost = restaurant.getDouble("estimated_cost"),
                    rating = UserRating(
                        averageRating = rating.getDouble("average_rating"),
                        votes = rating.getLong("votes"),
                    ),
                )
            } catch (e: JSONException) {
                throw  IllegalArgumentException("Error parsing json object")
            }
        }

        return@withContext NearbyRestaurantResult(
            page = pageNum,
            totalPages = totalPage,
            data = restaurantList,
        )
    }
}