package com.tokyonth.weather.data

import android.content.res.Resources
import android.graphics.drawable.GradientDrawable

import java.util.*

import com.tokyonth.weather.dynamic.data.SkyBackground
import com.tokyonth.weather.dynamic.data.WeatherType
import com.tokyonth.weather.dynamic.data.DynamicDrawerType
import com.tokyonth.weather.utils.ktx.color
import com.tokyonth.weather.utils.ktx.dp2px
import com.tokyonth.weather.utils.ktx.string
import com.tokyonth.weather.App
import com.tokyonth.weather.R
import com.tokyonth.weather.data.hf.WeatherNow
import com.tokyonth.weather.utils.DateUtils

object WeatherHelper {

    private fun getWeatherImageId(name: String): Int {
        val resources: Resources = App.context.resources
        val packageName: String = App.context.packageName
        return resources.getIdentifier(name, "drawable", packageName)
    }

    fun getWeatherImagePath(img: String): Int {
        return when (img.toInt()) {
            0 -> getWeatherImageId("weather_sunny")
            1 -> getWeatherImageId("weather_cloudy")
            2 -> getWeatherImageId("weather_overcast")
            3 -> getWeatherImageId("weather_rain_shower")
            4 -> getWeatherImageId("weather_shower")
            5 -> getWeatherImageId("weather_hail")
            6, 7 -> getWeatherImageId("weather_light_rain")
            8, 21, 22 -> getWeatherImageId("weather_moderate_rain")
            9, 23, 301 -> getWeatherImageId("weather_heavy_rain")
            10, 11, 12, 24, 25 -> getWeatherImageId("weather_rainstorm")
            14, 15, 26 -> getWeatherImageId("weather_light_snow")
            13, 16, 17, 27, 28, 302 -> getWeatherImageId("weather_moderate_snow")
            18, 32, 49, 57, 58 -> getWeatherImageId("weather_fog")
            19 -> getWeatherImageId("weather_icerain")
            20, 29, 30, 31 -> getWeatherImageId("weather_sand")
            in 53..56 -> getWeatherImageId("weather_haze")
            else -> 0
        }
    }

    fun getAirQualityColor(airQuality: String): Int {
        return when (airQuality) {
          /*  string(R.string.air_quality_good) -> color(R.color.airQualityGood)
            string(R.string.air_quality_medium) -> color(R.color.airQualityMedium)
            string(R.string.air_quality_lightly_pollute) -> color(R.color.airQualityLightlyPollute)
            string(R.string.air_quality_medium_pollute) -> color(R.color.airQualityMediumPollute)
            string(R.string.air_quality_heavy_pollute) -> color(R.color.airQualityHeavyPollute)
            string(R.string.air_quality_deep_pollute) -> color(R.color.airQualityDeepPollute)*/
            else -> 0
        }
    }

    fun getWeatherBackground(img: String): GradientDrawable {
        val color = when (img.toInt()) {
            0 -> SkyBackground.CLEAR_D
            1, 2 -> SkyBackground.CLOUDY_D
            in 3..12, 19, in 21..25, 301 -> SkyBackground.RAIN_D
            in 13..17, in 26..28, 302 -> SkyBackground.SNOW_D
            18, 32, 49, 57, 58 -> SkyBackground.FOG_D
            20, in 29..31 -> SkyBackground.SAND_D
            in 53..56 -> SkyBackground.HAZE_D
            else -> SkyBackground.DEFAULT
        }
        val bg = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, color).apply {
            cornerRadius = 16.dp2px()
        }
        return bg
    }

    private fun getWeatherType(img: String): WeatherType? {
        return when (img.toInt()) {
            0 -> WeatherType.WEATHER_TYPE_SUNNY
            1, 2 -> WeatherType.WEATHER_TYPE_CLOUDY
            in 3..12, 19, in 21..25, 301 -> WeatherType.WEATHER_TYPE_RAINY
            in 13..17, in 26..28, 302 -> WeatherType.WEATHER_TYPE_SNOWY
            18, 32, 49, 57, 58 -> WeatherType.WEATHER_TYPE_FOGGY
            20, in 29..31 -> WeatherType.WEATHER_TYPE_SANDY
            in 53..56 -> WeatherType.WEATHER_TYPE_HAZY
            else -> null
        }
    }

    fun getDrawerType(weatherNow: WeatherNow): DynamicDrawerType {
        //val isDay = DateUtils.dayOrNight(weatherNow)
        val isDay = true
        return when (getWeatherType(weatherNow.now.icon)) {
            WeatherType.WEATHER_TYPE_SUNNY -> if (isDay) DynamicDrawerType.CLEAR_D else DynamicDrawerType.CLEAR_N
            WeatherType.WEATHER_TYPE_CLOUDY -> if (isDay) DynamicDrawerType.CLOUDY_D else DynamicDrawerType.CLOUDY_N
            WeatherType.WEATHER_TYPE_RAINY -> if (isDay) DynamicDrawerType.RAIN_D else DynamicDrawerType.RAIN_N
            WeatherType.WEATHER_TYPE_SNOWY -> if (isDay) DynamicDrawerType.SNOW_D else DynamicDrawerType.SNOW_N
            WeatherType.WEATHER_TYPE_FOGGY -> if (isDay) DynamicDrawerType.FOG_D else DynamicDrawerType.FOG_N
            WeatherType.WEATHER_TYPE_SANDY -> if (isDay) DynamicDrawerType.SAND_D else DynamicDrawerType.SAND_N
            WeatherType.WEATHER_TYPE_HAZY -> if (isDay) DynamicDrawerType.HAZE_D else DynamicDrawerType.HAZE_N
            else -> DynamicDrawerType.DEFAULT
        }
    }

    fun getIconResId(weather: String): Int {
        return when (weather) {
           /* string(R.string.weather_sun) -> R.drawable.weather_sunny
            string(R.string.weather_overcast) -> R.drawable.weather_overcast
            string(R.string.weather_snow) -> R.drawable.weather_heavy_rain
            string(R.string.weather_rain) -> R.drawable.weather_light_snow
            string(R.string.weather_cloudy) -> R.drawable.weather_cloudy
            string(R.string.weather_thunder) -> R.drawable.weather_thunder
            string(R.string.weather_light_rain) -> R.drawable.weather_light_rain
            string(R.string.weather_shower) -> R.drawable.weather_rain_shower*/
            else -> 0
        }
    }

    fun getLifeIndexIcon(indexName: String): Int {
        return when (indexName) {
           /* string(R.string.index_air_conditioning) -> R.drawable.index_air_conditioning
            string(R.string.index_sport) -> R.drawable.index_sport
            string(R.string.index_uv) -> R.drawable.index_uv
            string(R.string.index_virus) -> R.drawable.index_virus
            string(R.string.index_washing) -> R.drawable.index_washing
            string(R.string.index_air_pollution) -> R.drawable.index_air_pollution
            string(R.string.index_clothes) -> R.drawable.index_clothes*/
            else -> 0
        }
    }

    fun getSunriseSunset(sunrise: String, sunset: String): Array<Int> {
        // index 0 - 3分别是 日出&日落的小时和分钟
        return arrayOf(
            6,
            30,
            20,
            10
        )
    }

}
