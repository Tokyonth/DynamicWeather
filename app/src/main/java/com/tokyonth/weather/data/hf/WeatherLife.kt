package com.tokyonth.weather.data.hf

import com.google.gson.annotations.SerializedName
import com.tokyonth.weather.network.BaseResponse

data class WeatherLife(

    @field:SerializedName("fxLink")
    val fxLink: String,

    @field:SerializedName("daily")
    val daily: List<DailyItemLife>,

    @field:SerializedName("updateTime")
    val updateTime: String

) : BaseResponse() {

    data class DailyItemLife(

        @field:SerializedName("date")
        val date: String,

        @field:SerializedName("level")
        val level: String,

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("text")
        val text: String,

        @field:SerializedName("type")
        val type: String,

        @field:SerializedName("category")
        val category: String

    )

}
