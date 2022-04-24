package com.tokyonth.weather.data.hf

import com.google.gson.annotations.SerializedName
import com.tokyonth.weather.network.BaseResponse

data class Weather7Day(

    @field:SerializedName("fxLink")
    val fxLink: String,

    @field:SerializedName("daily")
    val daily: List<DailyItem>,

    @field:SerializedName("updateTime")
    val updateTime: String

) : BaseResponse() {

    data class DailyItem(

        @field:SerializedName("moonset")
        val moonset: String,

        @field:SerializedName("windSpeedDay")
        val windSpeedDay: String,

        @field:SerializedName("sunrise")
        val sunrise: String,

        @field:SerializedName("moonPhaseIcon")
        val moonPhaseIcon: String,

        @field:SerializedName("windScaleDay")
        val windScaleDay: String,

        @field:SerializedName("windScaleNight")
        val windScaleNight: String,

        @field:SerializedName("wind360Day")
        val wind360Day: String,

        @field:SerializedName("iconDay")
        val iconDay: String,

        @field:SerializedName("wind360Night")
        val wind360Night: String,

        @field:SerializedName("tempMax")
        val tempMax: String,

        @field:SerializedName("cloud")
        val cloud: String,

        @field:SerializedName("textDay")
        val textDay: String,

        @field:SerializedName("precip")
        val precip: String,

        @field:SerializedName("textNight")
        val textNight: String,

        @field:SerializedName("humidity")
        val humidity: String,

        @field:SerializedName("moonPhase")
        val moonPhase: String,

        @field:SerializedName("windDirDay")
        val windDirDay: String,

        @field:SerializedName("windDirNight")
        val windDirNight: String,

        @field:SerializedName("vis")
        val vis: String,

        @field:SerializedName("fxDate")
        val fxDate: String,

        @field:SerializedName("moonrise")
        val moonrise: String,

        @field:SerializedName("pressure")
        val pressure: String,

        @field:SerializedName("iconNight")
        val iconNight: String,

        @field:SerializedName("sunset")
        val sunset: String,

        @field:SerializedName("windSpeedNight")
        val windSpeedNight: String,

        @field:SerializedName("uvIndex")
        val uvIndex: String,

        @field:SerializedName("tempMin")
        val tempMin: String

    )

}
