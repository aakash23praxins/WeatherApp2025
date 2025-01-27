package com.example.weatherapp

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private var city = ""
    private val BASE_URL = "https://api.weatherapi.com/v1/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor =
                getResources().getColor(R.color.statusbarColor, this.theme);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getResources().getColor(R.color.statusbarColor2);
        }


        binding.edtGetCityName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                city = ""
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                city += p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                city = p0.toString()
                getApiData(apiKey,city,BASE_URL)
            }
        })

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
                    val icon = data.current.condition.icon

                    val cName = data.location.name
                    val country = data.location.country
                    val region = data.location.region
                    val localTime = data.location.localtime

                    binding.txtCityName.text = cName
                    binding.txtTemp.text = currentWeather
                    binding.txtHumidity.text = humidity
                    binding.txtVisibility.text = visibility
                    binding.txtWindSpeed.text = wind
                    binding.txtFeelsLike.text = feelsLike
                    binding.txtWeatherText.text = data.current.condition.text


                }
            }

            override fun onFailure(p0: Call<WeatherModel?>, p1: Throwable) {
                Log.d("FAILURE", "DATA failed ${p1.message}")
            }
        })
    }
}