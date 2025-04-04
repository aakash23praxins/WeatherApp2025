package com.aakash.weather.utils

class Utils {
    companion object {
        val weatherIconMap = mapOf(
            "Sunny" to "https://cdn-icons-png.freepik.com/512/1710/1710834.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Clear" to "https://cdn-icons-png.freepik.com/512/6660/6660182.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Partly cloudy" to "https://cdn-icons-png.freepik.com/512/6643/6643114.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Cloudy" to "https://cdn-icons-png.freepik.com/512/16227/16227902.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Overcast" to "https://cdn-icons-png.freepik.com/512/10156/10156838.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Mist" to "https://cdn-icons-png.freepik.com/512/13882/13882434.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Patchy rain possible" to "https://cdn-icons-png.freepik.com/512/7946/7946130.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Patchy snow possible" to "https://cdn-icons-png.freepik.com/512/18682/18682952.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Patchy sleet possible" to "https://cdn-icons-png.freepik.com/512/7088/7088028.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Patchy freezing drizzle possible" to "https://cdn-icons-png.freepik.com/512/3512/3512814.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Thundery outbreaks possible" to "https://cdn-icons-png.freepik.com/512/4503/4503041.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Blowing snow" to "http://cdn-icons-png.freepik.com/512/1809/1809256.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Blizzard" to "https://cdn-icons-png.freepik.com/512/10344/10344758.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Fog" to "https://cdn-icons-png.freepik.com/512/17993/17993043.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Freezing fog" to "https://cdn-icons-png.freepik.com/512/17993/17993043.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Patchy light drizzle" to "https://cdn-icons-png.freepik.com/512/4410/4410773.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Light drizzle" to "https://cdn-icons-png.freepik.com/512/523/523551.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Freezing drizzle" to "https://cdn-icons-png.freepik.com/512/10981/10981872.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Heavy freezing drizzle" to "https://cdn-icons-png.freepik.com/512/11055/11055801.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Patchy light rain" to "https://cdn-icons-png.freepik.com/512/10497/10497776.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Light rain" to "https://cdn-icons-png.freepik.com/512/7870/7870011.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Moderate rain at times" to "https://cdn-icons-png.freepik.com/512/523/523551.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Moderate rain" to "https://cdn-icons-png.freepik.com/512/523/523551.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Heavy rain at times" to "https://cdn-icons-png.freepik.com/512/13202/13202237.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Heavy rain" to "https://cdn-icons-png.freepik.com/512/13202/13202237.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Light freezing rain" to "https://cdn-icons-png.freepik.com/512/7875/7875208.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Moderate or heavy freezing rain" to "https://cdn-icons-png.freepik.com/512/7875/7875208.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Light sleet" to "https://cdn-icons-png.freepik.com/512/3312/3312937.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Moderate or heavy sleet" to "https://cdn-icons-png.freepik.com/512/3312/3312937.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Patchy light snow" to "https://cdn-icons-png.freepik.com/512/10369/10369479.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Light snow" to "https://cdn-icons-png.freepik.com/512/10369/10369479.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Patchy moderate snow" to "https://cdn-icons-png.freepik.com/512/10369/10369479.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Moderate snow" to "https://cdn-icons-png.freepik.com/512/10369/10369479.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Patchy heavy snow" to "https://cdn-icons-png.freepik.com/512/2412/2412748.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Heavy snow" to "https://cdn-icons-png.freepik.com/512/14137/14137657.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Ice pellets" to "https://cdn-icons-png.freepik.com/512/615/615497.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Light rain shower" to "https://cdn-icons-png.freepik.com/512/14131/14131413.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Moderate or heavy rain shower" to "https://cdn-icons-png.freepik.com/512/14131/14131413.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Torrential rain shower" to "https://cdn-icons-png.freepik.com/512/14131/14131413.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Light sleet showers" to "https://cdn-icons-png.freepik.com/512/14131/14131413.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Moderate or heavy sleet showers" to "https://cdn-icons-png.freepik.com/512/14131/14131413.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Light snow showers" to "https://cdn-icons-png.freepik.com/512/14131/14131413.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Moderate or heavy snow showers" to "https://cdn-icons-png.freepik.com/512/14131/14131413.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Light showers of ice pellets" to "https://cdn-icons-png.freepik.com/512/11557/11557484.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Moderate or heavy showers of ice pellets" to "https://cdn-icons-png.freepik.com/512/11557/11557484.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Patchy light rain with thunder" to "https://cdn-icons-png.freepik.com/512/1197/1197110.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Moderate or heavy rain with thunder" to "https://cdn-icons-png.freepik.com/512/1197/1197110.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Patchy light snow with thunder" to "https://cdn-icons-png.freepik.com/512/1197/1197110.png?uid=R183363984&ga=GA1.1.1357463132.1737101655",
            "Moderate or heavy snow with thunder" to "https://cdn-icons-png.freepik.com/512/1197/1197110.png?uid=R183363984&ga=GA1.1.1357463132.1737101655"
        )

        val BASE_URL = "https://api.weatherapi.com/v1/"

    }
}