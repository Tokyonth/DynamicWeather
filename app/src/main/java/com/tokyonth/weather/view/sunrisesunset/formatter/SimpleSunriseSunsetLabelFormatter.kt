package com.tokyonth.weather.view.sunrisesunset.formatter

import com.tokyonth.weather.view.sunrisesunset.model.Time
import java.util.*

/**
 * SunriseSunsetLabelFormatter 简单实现
 */
class SimpleSunriseSunsetLabelFormatter : SunriseSunsetLabelFormatter {

    override fun formatSunriseLabel(sunrise: Time): String {
        return formatTime(sunrise)
    }

    override fun formatSunsetLabel(sunset: Time): String {
        return formatTime(sunset)
    }

    fun formatTime(time: Time): String {
        return String.format(Locale.getDefault(), "%d:%d", time.hour, time.minute)
    }

}
