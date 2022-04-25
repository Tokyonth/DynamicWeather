package com.tokyonth.weather.ui.fragment.provider

import android.annotation.SuppressLint
import com.tokyonth.weather.data.WeatherHelper
import com.tokyonth.weather.data.hf.WeatherAir
import com.tokyonth.weather.databinding.FragmentWeatherBinding

/**
 * 空气质量
 */
class AirWeatherProvider : BaseWeatherProvider<WeatherAir, FragmentWeatherBinding>() {

    @SuppressLint("SetTextI18n")
    override fun fillView() {
        val aqiAffect = getData().now.aqi
        val airQualityColor = WeatherHelper.getAirQualityColor(getData().now.level.toInt())
        val levelInfo = "空气质量" + getData().now.level + "级"
        val primaryPolluteInfo = "首要污染物 : " + getData().now.primary
        val aqiIndex = getData().now.aqi.toInt()
        var defaultMax = 100
        while (aqiIndex > defaultMax) {
            defaultMax += 100
        }

        binding.pagerWeatherAqi.run {
            aqiQualityLevelTv.setTextColor(airQualityColor)
            aqiQualityLevelTv.text = levelInfo
            aqiAffectTv.text = aqiAffect
            aqiPrimaryPolluteTv.text = primaryPolluteInfo

            weatherAirqualityTv.text = getData().now.category
            weatherAirqualityTv.setTextColor(airQualityColor)
            weatherAirqualityImageIv.setColorFilter(airQualityColor)
            semicircleProgressView.setSesameValues(aqiIndex, defaultMax)
            semicircleProgressView.semicircleTitleColor = airQualityColor
            semicircleProgressView.frontLineColor = airQualityColor
        }

        binding.pagerWeatherAqi.run {
            aqiPm25Tv.text = "PM2.5 : " + getData().now.pm2p5 + " μg/m³"
            aqiPm10Tv.text = "PM10 : " + getData().now.pm10 + " μg/m³"
            aqiSo2Tv.text = "SO₂ : " + getData().now.so2 + " μg/m³"
            aqiNo2Tv.text = "NO₂ : " + getData().now.no2 + " μg/m³"
            aqiO3Tv.text = "O₃ : " + getData().now.o3 + " μg/m³"
            aqiCoTv.text = "CO : " + getData().now.co + " μg/m³"
        }
    }

}
