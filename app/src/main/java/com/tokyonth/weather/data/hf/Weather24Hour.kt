package com.tokyonth.weather.data.hf

import com.google.gson.annotations.SerializedName
import com.tokyonth.weather.network.BaseResponse

data class Weather24Hour(

    @field:SerializedName("fxLink")
    val fxLink: String,

    @field:SerializedName("updateTime")
    val updateTime: String,

    @field:SerializedName("hourly")
    val hourly: List<HourlyItem>

) : BaseResponse() {

    data class HourlyItem(

        @field:SerializedName("temp")
        val temp: String,

        @field:SerializedName("icon")
        val icon: String,

        @field:SerializedName("wind360")
        val wind360: String,

        @field:SerializedName("windDir")
        val windDir: String,

        @field:SerializedName("pressure")
        val pressure: String,

        @field:SerializedName("fxTime")
        val fxTime: String,

        @field:SerializedName("pop")
        val pop: String,

        @field:SerializedName("cloud")
        val cloud: String,

        @field:SerializedName("precip")
        val precip: String,

        @field:SerializedName("dew")
        val dew: String,

        @field:SerializedName("humidity")
        val humidity: String,

        @field:SerializedName("text")
        val text: String,

        @field:SerializedName("windSpeed")
        val windSpeed: String,

        @field:SerializedName("windScale")
        val windScale: String

    )

}
