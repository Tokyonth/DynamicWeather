package com.tokyonth.weather.ui.fragment.provider

import com.tokyonth.weather.data.hf.Weather7Day
import com.tokyonth.weather.databinding.FragmentWeatherBinding
import com.tokyonth.weather.view.sunrisesunset.formatter.SunriseSunsetLabelFormatter
import com.tokyonth.weather.view.sunrisesunset.model.Time
import java.util.*

class DailyWeatherProvider : BaseWeatherProvider<Weather7Day, FragmentWeatherBinding>() {

    override fun fillView() {
        binding.dailyView.setData(getData())

        binding.ssvView.run {
            labelFormatter = object : SunriseSunsetLabelFormatter {
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
            setSsvTime(Pair(getData().daily[0].sunrise, getData().daily[0].sunset))
            startAnimate()
        }
    }

}
