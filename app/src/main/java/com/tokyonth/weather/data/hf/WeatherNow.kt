package com.tokyonth.weather.data.hf

import com.google.gson.annotations.SerializedName
import com.tokyonth.weather.network.BaseResponse

data class WeatherNow(

    @field:SerializedName("fxLink")
    val fxLink: String,

    @field:SerializedName("now")
    val now: Now,

    @field:SerializedName("updateTime")
    val updateTime: String

) : BaseResponse() {

    data class Now(

        @field:SerializedName("vis")
        val vis: String,

        @field:SerializedName("temp")
        val temp: String,

        @field:SerializedName("obsTime")
        val obsTime: String,

        @field:SerializedName("icon")
        val icon: String,

        @field:SerializedName("wind360")
        val wind360: String,

        @field:SerializedName("windDir")
        val windDir: String,

        @field:SerializedName("pressure")
        val pressure: String,

        @field:SerializedName("feelsLike")
        val feelsLike: String,

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
