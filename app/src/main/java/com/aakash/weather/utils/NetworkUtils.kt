package com.aakash.weather.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.telephony.TelephonyManager
import java.net.NetworkInterface

object NetworkUtils {
    fun getIpAddress(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            for (intf in interfaces) {
                val addrs = intf.inetAddresses
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        return addr.hostAddress ?: "Unknown"
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return "Unknown"
    }

    fun getNetworkType(context: Context): String {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return "NONE"
        val capabilities = cm.getNetworkCapabilities(network) ?: return "NONE"
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "MOBILE"
            else -> "UNKNOWN"
        }
    }

    fun getCarrierName(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkOperatorName
    }
}