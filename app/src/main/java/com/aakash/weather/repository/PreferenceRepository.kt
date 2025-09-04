package com.aakash.weather.repository

import android.content.SharedPreferences
import com.aakash.weather.global.isFirstTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepository @Inject constructor(
    private val preferences: SharedPreferences
) {
    fun setIsFirstTime(isFirstTimeFlag: Boolean) {
        preferences.edit().apply {
            this.putBoolean(isFirstTime, isFirstTimeFlag)
        }.apply()
    }

    fun getIsFirstTime(): Boolean {
        return preferences.getBoolean(isFirstTime, false)
    }
}