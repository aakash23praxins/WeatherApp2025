package com.aakash.weather.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.aakash.weather.model.WeatherModel
import com.aakash.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    fun fetchData(apiKey: String, cityName: String): Call<WeatherModel> {
        return repository.apiService().getData(apiKey, cityName)
    }

    fun fetchAstroData(key: String, q: String, day: String) = liveData {
        try {
            val response = repository.fetchAstronomy(
                q = q,
                day = day,
                key = key
            )
            if (response.isSuccessful) {
                response.body()?.let { emit(it) }
                Log.d("Api DATA", "Data ${response.body()}")
            } else {
                emit(null)
                Log.d("Api Exception", "${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("Error", "The ${e.message}")
        }
    }


}