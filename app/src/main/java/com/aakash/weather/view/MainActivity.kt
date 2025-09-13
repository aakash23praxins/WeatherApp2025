package com.aakash.weather.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aakash.weather.R
import com.aakash.weather.databinding.ActivityMainBinding
import com.aakash.weather.model.DeviceInfo
import com.aakash.weather.repository.PreferenceRepository
import com.aakash.weather.utils.NetworkUtils
import com.aakash.weather.utils.Utils
import com.aakash.weather.view.adapter.TodayAdapter
import com.aakash.weather.viewmodel.WeatherViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.BuildConfig
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var city: String? = null
    private var apiKey = com.aakash.weather.BuildConfig.WEATHER_API_KEY

    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var todayAdapter: TodayAdapter

    // Modern Location API
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var firebaseFireStore: FirebaseFirestore

    private var lat: String? = null
    private var long: String? = null
    private var lastFetchedCity: String? = null
    private var searchJob: Job? = null


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkIsFirstTime()
        registerPermissionLauncher()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 5000L
        ).setMinUpdateIntervalMillis(2000L).build()
        if (isLocationEnabled()) {
            fetchSingleLocation()
        } else {
            lifecycleScope.launch {
                Snackbar.make(
                    binding.root,
                    "Please turn on location permission for better weather update!!",
                    Snackbar.LENGTH_SHORT
                ).show()
                delay(1300)
                Snackbar.make(
                    binding.root,
                    "We want to give you better weather update..",
                    Snackbar.LENGTH_SHORT
                ).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }



        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location: Location? = result.lastLocation
                location?.let {
                    lat = it.latitude.toString()
                    long = it.longitude.toString()
                    Log.d("WeatherApp", "Lat: $lat, Lon: $long")

                    city = getCityName(it.latitude, it.longitude)
                    city?.let { name -> getApiData(apiKey, name) }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.statusbarColor, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.statusbarColor2)
        }

        todayAdapter = TodayAdapter(this)
        binding.todayRecyclerView.adapter = todayAdapter
        binding.todayRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.edtGetCityName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                city = ""
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                city += s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                val cityName = s.toString()
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(800)
                    if (cityName.isNotEmpty() && cityName != lastFetchedCity) {
                        lastFetchedCity = cityName
                        getApiData(apiKey, cityName)
                    }
                }
            }
        })
    }


    private fun registerPermissionLauncher() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    fetchSingleLocation()
                } else {
                    lifecycleScope.launch {
                        Snackbar.make(
                            binding.root,
                            "Please turn on location permission for better weather update!!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        delay(1300)
                        Snackbar.make(
                            binding.root,
                            "We want to give you better weather update..",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    @Suppress("MissingPermission")
    private fun fetchSingleLocation() {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, null
        ).addOnSuccessListener { location: Location? ->
            if (location != null) {
                handleLocation(location)
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc: Location? ->
                    if (lastLoc != null) {
                        handleLocation(lastLoc)
                    } else {
                        Log.e("WeatherApp", "Still no location available")
                    }
                }
            }
        }
    }


    private fun handleLocation(location: Location) {
        lat = location.latitude.toString()
        long = location.longitude.toString()
        Log.d("WeatherApp", "Lat: $lat, Lon: $long")

        val newCity = getCityName(location.latitude, location.longitude)
        if (newCity != null && newCity != lastFetchedCity) {
            lastFetchedCity = newCity
            getApiData(apiKey, newCity)
        }
    }


    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun getApiData(apiKey: String, city: String) {
//        weatherViewModel.fetchData(apiKey, city).enqueue(object : Callback<WeatherModel> {
//            override fun onResponse(
//                call: Call<WeatherModel?>,
//                response: Response<WeatherModel?>
//            ) {
//                if (response.isSuccessful) {
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
//                    binding.txtWeatherText.text = currentWeatherText
//
//                    binding.edtGetCityName.setText(city)
//                }
//            }
//
//            override fun onFailure(call: Call<WeatherModel?>, t: Throwable) {
//                Log.d("FAILURE", "DATA failed ${t.message}")
//            }
//        })

        weatherViewModel.fetchAstroData(
            key = apiKey, q = city, day = 1.toString()
        ).observe(this) { weatherData ->
            weatherData?.let {
                Log.d("DATA Activity", "data response $it")

                val data = it
                todayAdapter.setDataList(hourList = data.forecast.forecastday[0].hour)
                val currentWeather = data.current.temp_c
                val humidity = data.current.humidity
                val visibility = data.current.vis_km
                val wind = data.current.wind_kph
                val feelsLike = data.current.feelslike_c
                val icon = data.current.condition.icon

                val sunRise = data.forecast.forecastday[0].astro.sunrise
                val sunSet = data.forecast.forecastday[0].astro.sunset

                val cName = data.location.name
                val currentWeatherText = data.current.condition.text
                val dateTime = data.location.localtime.split(" ")
                val date = dateTime[0]
                val time = dateTime[1]

                val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
                val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
                val parsedDate = LocalDate.parse(date, inputFormatter)

                binding.txtDate.text = parsedDate.format(outputFormatter)
                binding.txtTime.text = time

                val hour = time.split(":")

                println("the hour is $hour")

                todayAdapter.scrollToTime(
                    binding.todayRecyclerView, hour[0].toInt()
                )

                setWeatherIcon(currentWeatherText)

                binding.txtCityName.text = cName
                binding.txtTemp.text = currentWeather
                binding.txtHumidity.text = humidity
                binding.txtVisibility.text = visibility
                binding.txtWindSpeed.text = wind
                binding.txtFeelsLike.text = feelsLike
                binding.txtWeatherText.text = currentWeatherText

                binding.txtSunRise.text = sunRise
                binding.txtSunSet.text = sunSet

                binding.edtGetCityName.setText(city)
                binding.txtCityName.text = city
            }
        }
    }

    private fun setWeatherIcon(weatherText: String) {
        val iconList = Utils.weatherIconMap
        iconList.forEach { (key, value) ->
            if (key == weatherText) {
                Glide.with(applicationContext).applyDefaultRequestOptions(
                    RequestOptions().placeholder(R.drawable.cloud).error(R.drawable.cloud)
                ).load(value).into(binding.imgIcon)
            }
        }
    }

    private fun getCityName(latitude: Double, longitude: Double): String? {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                addresses[0].locality ?: addresses[0].subAdminArea
            } else null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchSingleLocation()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun isLocationEnabled(): Boolean {
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkIsFirstTime() {
        val isFirsTimeFlag = preferenceRepository.getIsFirstTime()
        Log.d("isFirsTimeFlag", "isFirsTimeFlag---> $isFirsTimeFlag")
        if (!isFirsTimeFlag) {
            val deviceInfo = getDeviceData()
            setFirebaseStoreData(deviceInfo)
            preferenceRepository.setIsFirstTime(true)
        }


    }

    private fun setFirebaseStoreData(deviceInfo: DeviceInfo) {
        val devices = mapOf(
            "deviceId" to deviceInfo.deviceId,
            "manufacturer" to deviceInfo.manufacturer,
            "model" to deviceInfo.model,
            "brand" to deviceInfo.brand,
            "androidVersion" to deviceInfo.androidVersion,
            "sdkInt" to deviceInfo.sdkInt.toString(),
            "deviceName" to deviceInfo.deviceName,
            "ipAddress" to deviceInfo.ipAddress,
            "networkType" to deviceInfo.networkType,
            "carrierName" to deviceInfo.carrierName.toString(),
            "appVersion" to deviceInfo.appVersion,
            "firstOpenTime" to deviceInfo.firstOpenTime.toString()
        )


        firebaseFireStore.collection("users_devices")
            .add(devices)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "FIREBASE Success",
                    "DocumentSnapshot added with ID: ${documentReference.id}"
                )
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE Failure ", "Error adding document, ${e.message.toString()}")
            }

    }

    private fun getDeviceData(): DeviceInfo {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val brand = Build.BRAND
        val androidVersion = Build.VERSION.RELEASE
        val sdkInt = Build.VERSION.SDK_INT
        val deviceName = "$manufacturer $model"

        val ipAddress = NetworkUtils.getIpAddress()
        val networkType = NetworkUtils.getNetworkType(this)
        val carrierName = NetworkUtils.getCarrierName(this)

        val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        val appVersion = packageInfo.versionName ?: "1.0"

        return DeviceInfo(
            deviceId = UUID.randomUUID().toString(),
            manufacturer = manufacturer,
            model = model,
            brand = brand,
            androidVersion = androidVersion,
            sdkInt = sdkInt,
            deviceName = deviceName,
            ipAddress = ipAddress,
            networkType = networkType,
            carrierName = carrierName,
            appVersion = appVersion,
            firstOpenTime = System.currentTimeMillis()
        )
    }


}
