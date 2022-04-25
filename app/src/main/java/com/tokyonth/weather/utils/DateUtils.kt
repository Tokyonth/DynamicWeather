package com.tokyonth.weather.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {

    fun paresTime(time: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val o = OffsetDateTime.parse(time)
            o.format(DateTimeFormatter.ofPattern("HH:mm"))
        } else {
            val d = SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.CHINA)
            val p = d.parse(time)
            val d1 = SimpleDateFormat("HH:mm", Locale.CHINA)
            d1.format(p!!)
        }
    }

    fun format(time: String): String {
        val d = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val p = d.parse(time)
        val d1 = SimpleDateFormat("MM-dd", Locale.CHINA)
        return d1.format(p!!)
    }

    fun nowDay(): String {
        return "2022425"
    }

}
