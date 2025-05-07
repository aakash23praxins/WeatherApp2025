package com.aakash.weather.viewmodel

import androidx.lifecycle.ViewModel
import com.aakash.weather.model.WeatherModel
import com.aakash.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(val repository: WeatherRepository) : ViewModel() {

    fun fetchData(apiKey: String, cityName: String): Call<WeatherModel> {
        return repository.apiService().getData(apiKey, cityName)
    }

}