package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val apiKey = "9857663abe584adf93670010241402"
    private val city = "Ahmedabad"
    private val BASE_URL = "https://api.weatherapi.com/v1/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getApiData(apiKey, city, BASE_URL)
    }

    private fun getApiData(apiKey: String, city: String, baseUrl: String) {

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiInterface = retrofit.create(ApiService::class.java).getData(apiKey, city)

        apiInterface.enqueue(object : Callback<WeatherModel?> {
            override fun onResponse(p0: Call<WeatherModel?>, response: Response<WeatherModel?>) {
                if (response.isSuccessful) {

                    val data = response.body() as WeatherModel

                    val currentWeather = data.current.temp_c
                    val humidity = data.current.humidity
                    val visibility = data.current.vis_km
                    val wind = data.current.wind_kph
                    val feelsLike = data.current.feelslike_c

                    val cName = data.location.name
                    val country = data.location.country
                    val region = data.location.region
                    val localTime = data.location.localtime


                    println(currentWeather)
                    println(humidity)
                    println(visibility)
                    println(wind)

                }
            }

            override fun onFailure(p0: Call<WeatherModel?>, p1: Throwable) {
                Log.d("FAILURE", "DATA failed ${p1.message}")
            }
        })
    }
}