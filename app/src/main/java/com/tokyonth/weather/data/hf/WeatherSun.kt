package com.tokyonth.weather.data.hf

import com.google.gson.annotations.SerializedName
import com.tokyonth.weather.network.BaseResponse

data class WeatherSun(

    @field:SerializedName("fxLink")
    val fxLink: String,

    @field:SerializedName("sunrise")
    val sunrise: String,

    @field:SerializedName("sunset")
    val sunset: String,

    @field:SerializedName("updateTime")
    val updateTime: String

) : BaseResponse()
