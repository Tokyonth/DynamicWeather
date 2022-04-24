package com.tokyonth.weather.ui.fragment.provider

import com.tokyonth.weather.data.hf.WeatherSun
import com.tokyonth.weather.databinding.FragmentWeatherBinding
import com.tokyonth.weather.view.sunrisesunset.formatter.SunriseSunsetLabelFormatter
import com.tokyonth.weather.view.sunrisesunset.model.Time

import java.util.*

/**
 * 日出日落
 */
class SsvWeatherProvider : BaseWeatherProvider<WeatherSun, FragmentWeatherBinding>() {

    override fun fillView() {
        binding.run {
            ssvView.labelFormatter = object : SunriseSunsetLabelFormatter {
                override fun formatSunriseLabel(sunrise: Time): String {
                    return formatLabel(sunrise)
                }

                override fun formatSunsetLabel(sunset: Time): String {
                    return formatLabel(sunset)
                }

                private fun formatLabel(time: Time): String {
                    return String.format(Locale.getDefault(), "%02d:%02d", time.hour, time.minute)
                }
            }
            ssvView.setSsvTime(Pair(getData().sunrise, getData().sunset))
            ssvView.startAnimate()
        }
    }

}
