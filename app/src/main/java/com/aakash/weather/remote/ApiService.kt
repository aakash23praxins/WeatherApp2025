package com.aakash.weather.remote

import com.aakash.weather.model.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("current.json")
    fun getData(@Query("key") apiKey: String, @Query("q") cityName: String): Call<WeatherModel>
}