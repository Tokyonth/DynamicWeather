package com.tokyonth.weather.view.sunrisesunset.formatter

import com.tokyonth.weather.view.sunrisesunset.model.Time

/**
 * 日出日落标签格式化
 */
interface SunriseSunsetLabelFormatter {

    fun formatSunriseLabel(sunrise: Time): String

    fun formatSunsetLabel(sunset: Time): String

}
