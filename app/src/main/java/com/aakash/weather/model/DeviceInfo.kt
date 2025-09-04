package com.aakash.weather.model

data class DeviceInfo(
    val deviceId: String,
    val manufacturer: String,
    val model: String,
    val brand: String,
    val androidVersion: String,
    val sdkInt: Int,
    val deviceName: String,
    val ipAddress: String,
    val networkType: String,
    val carrierName: String?,
    val appVersion: String,
    val firstOpenTime: Long
)
