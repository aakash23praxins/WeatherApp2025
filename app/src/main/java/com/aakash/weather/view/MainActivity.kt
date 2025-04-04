package com.aakash.weather.view

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aakash.weather.R
import com.aakash.weather.utils.Utils
import com.aakash.weather.databinding.ActivityMainBinding
import com.aakash.weather.model.WeatherModel
import com.aakash.weather.viewmodel.WeatherViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val apiKey = "9857663abe584adf93670010241402"
    private var city = "Ahmedabad"

    private val weatherViewModel: WeatherViewModel by viewModels()

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
                getApiData(apiKey,city)
            }
        })

        getApiData(apiKey, city)

    }

    private fun getApiData(apiKey: String, city: String) {

        weatherViewModel.fetchData(apiKey, city).enqueue(object : Callback<WeatherModel> {
            override fun onResponse(
                p0: Call<WeatherModel?>,
                response: Response<WeatherModel?>
            ) {
                if (response.isSuccessful) {

                    val data = response.body() as WeatherModel

                    val currentWeather = data.current.temp_c
                    val humidity = data.current.humidity
                    val visibility = data.current.vis_km
                    val wind = data.current.wind_kph
                    val feelsLike = data.current.feelslike_c
                    val icon = data.current.condition.icon

                    val cName = data.location.name
                    val currentWeatherText = data.current.condition.text

                    setWeatherIcon(currentWeatherText)

                    binding.txtCityName.text = cName
                    binding.txtTemp.text = currentWeather
                    binding.txtHumidity.text = humidity
                    binding.txtVisibility.text = visibility
                    binding.txtWindSpeed.text = wind
                    binding.txtFeelsLike.text = feelsLike
                    binding.txtWeatherText.text = data.current.condition.text


                }
            }

            override fun onFailure(
                p0: Call<WeatherModel?>,
                p1: Throwable
            ) {
                Log.d("FAILURE", "DATA failed ${p1.message}")
            }

        })



//        val retrofit = Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val apiInterface = retrofit.create(ApiService::class.java).getData(apiKey, city)
//
//        apiInterface.enqueue(object : Callback<WeatherModel?> {
//            override fun onResponse(p0: Call<WeatherModel?>, response: Response<WeatherModel?>) {
//                if (response.isSuccessful) {
//
//                    val data = response.body() as WeatherModel
//
//                    val currentWeather = data.current.temp_c
//                    val humidity = data.current.humidity
//                    val visibility = data.current.vis_km
//                    val wind = data.current.wind_kph
//                    val feelsLike = data.current.feelslike_c
//                    val icon = data.current.condition.icon
//
//                    val cName = data.location.name
//                    val currentWeatherText = data.current.condition.text
//
//                    setWeatherIcon(currentWeatherText)
//
//                    binding.txtCityName.text = cName
//                    binding.txtTemp.text = currentWeather
//                    binding.txtHumidity.text = humidity
//                    binding.txtVisibility.text = visibility
//                    binding.txtWindSpeed.text = wind
//                    binding.txtFeelsLike.text = feelsLike
//                    binding.txtWeatherText.text = data.current.condition.text
//
//
//                }
//            }
//
//            override fun onFailure(p0: Call<WeatherModel?>, p1: Throwable) {
//                Log.d("FAILURE", "DATA failed ${p1.message}")
//            }
//        })
    }

    private fun setWeatherIcon(weatherText: String) {

        val iconList = Utils.Companion.weatherIconMap
        iconList.forEach { key, value ->
            if (key == weatherText) {
                Glide.with(applicationContext).applyDefaultRequestOptions(
                    RequestOptions().placeholder(R.drawable.cloud).error(
                        R.drawable
                            .cloud
                    )
                ).load(value).into(binding.imgIcon)
            }
        }
    }
}