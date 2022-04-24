package com.tokyonth.weather.data.hf

import com.google.gson.annotations.SerializedName
import com.tokyonth.weather.network.BaseResponse

data class WeatherAir(

    @field:SerializedName("fxLink")
    val fxLink: String,

    @field:SerializedName("now")
    val now: NowAir,

    @field:SerializedName("station")
    val station: List<StationItem>,

    @field:SerializedName("updateTime")
    val updateTime: String

) : BaseResponse() {

    data class NowAir(

        @field:SerializedName("no2")
        val no2: String,

        @field:SerializedName("o3")
        val o3: String,

        @field:SerializedName("level")
        val level: String,

        @field:SerializedName("pm2p5")
        val pm2p5: String,

        @field:SerializedName("pubTime")
        val pubTime: String,

        @field:SerializedName("so2")
        val so2: String,

        @field:SerializedName("aqi")
        val aqi: String,

        @field:SerializedName("pm10")
        val pm10: String,

        @field:SerializedName("category")
        val category: String,

        @field:SerializedName("co")
        val co: String,

        @field:SerializedName("primary")
        val primary: String

    )

    data class StationItem(

        @field:SerializedName("o3")
        val o3: String,

        @field:SerializedName("level")
        val level: String,

        @field:SerializedName("pm2p5")
        val pm2p5: String,

        @field:SerializedName("pubTime")
        val pubTime: String,

        @field:SerializedName("pm10")
        val pm10: String,

        @field:SerializedName("co")
        val co: String,

        @field:SerializedName("no2")
        val no2: String,

        @field:SerializedName("so2")
        val so2: String,

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("aqi")
        val aqi: String,

        @field:SerializedName("id")
        val id: String,

        @field:SerializedName("category")
        val category: String,

        @field:SerializedName("primary")
        val primary: String

    )

}
