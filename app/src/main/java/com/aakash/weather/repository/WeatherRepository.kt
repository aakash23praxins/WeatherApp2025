package com.aakash.weather.repository

import com.aakash.weather.model.WeatherModel
import com.aakash.weather.remote.ApiService
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val retrofitInstance: Retrofit,
    private val apiService: ApiService,
) {
    fun apiService(): ApiService {
        return retrofitInstance.create(ApiService::class.java)
    }

    suspend fun fetchAstronomy(q: String, day: String, key: String): Response<WeatherModel> {
        return apiService.getAstronomy(
            apiKey = key,
            q = q,
            day = day
        )
    }

}