package com.tokyonth.weather.view.sunrisesunset.model

/**
 * 日出日落相关时间
 */
class Time(
    /**
     * 时
     */
    var hour: Int,
    /**
     * 分
     */
    var minute: Int
) {

    companion object {
        const val MINUTES_PER_HOUR = 60
    }

    fun transformToMinutes(): Int {
        return hour * MINUTES_PER_HOUR + minute
    }

}
