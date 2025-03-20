package com.aakash.weather.model

data class WeatherModel(
    val location: Location,
    val current: Current
)

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val localtime: String
)

data class Current(
    val temp_c: String,
    val wind_kph: String,
    val humidity: String,
    val feelslike_c: String,
    val vis_km: String,
    val condition: Condition
)

data class Condition(
    val text: String,
    val icon: String
)


//  name city, celsious, humidity, visibility, wind spedd, sun rise , sun set,feels like