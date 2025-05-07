package com.aakash.weather.repository

import com.aakash.weather.remote.ApiService
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherRepository @Inject constructor(val retrofitInstance: Retrofit) {
    fun apiService(): ApiService {
        return retrofitInstance.create(ApiService::class.java)
    }
}