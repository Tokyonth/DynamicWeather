package com.tokyonth.weather.ui.fragment.provider

import android.view.View
import com.tokyonth.weather.R
import com.tokyonth.weather.data.hf.WeatherNow
import com.tokyonth.weather.databinding.FragmentWeatherBinding
import com.tokyonth.weather.utils.ktx.string

class TempWeatherProvider : BaseWeatherProvider<WeatherNow, FragmentWeatherBinding>() {

    override fun fillView() {
        val tempInfo = getData().now.temp + string(R.string.degree)
        binding.run {
            tvWeatherTips.visibility = View.VISIBLE
            tvWeatherTips.text =
                getData().now.text + " - 体感: " + getData().now.feelsLike + string(R.string.degree)
            tvWeatherTemp.text = tempInfo
        }

        val windSpeed = getData().now.windSpeed
        binding.pagerWeatherMsg.run {
            weatherWindSpeedTv.text = "风速 : " + windSpeed + "m/s"
            windmillBig.startAnimation()
            windmillSmall.startAnimation()
            windmillBig.setWindSpeed(windSpeed.toDouble())
            windmillSmall.setWindSpeed(windSpeed.toDouble())
        }

        val humidityInfo = "空气湿度 : " + getData().now.humidity + "%"
        val windInfo = """
         ${getData().now.windDir}
         ${getData().now.windScale}
         """.trimIndent()

        binding.pagerWeatherMsg.run {
            weatherWindTv.text = windInfo
            weatherHumidityTv.text = humidityInfo
            weatherPressureTv.text = "气体压强 : " + getData().now.pressure + "hPa"
            /*weatherForecastDayTipsTv.text =
                WeatherHelper.getDayWeatherTipsInfo(data.dailyList)
            weatherForecastHourlyTipsTv.text =
                WeatherHelper.getHourlyWeatherTipsInfo(data.hourlyList)*/
        }
    }

    fun destroy() {
        binding.pagerWeatherMsg.run {
            windmillBig.stopAnimation()
            windmillBig.clearAnimation()
            windmillSmall.stopAnimation()
            windmillSmall.clearAnimation()
        }
    }

}
