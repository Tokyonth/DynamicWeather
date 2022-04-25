package com.tokyonth.weather.network

import com.tokyonth.weather.data.hf.*
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    /**
     * 实时天气
     */
    @GET("weather/now")
    suspend fun weatherNow(
        @Query("location") location: String
    ): WeatherNow

    /**
     * 7天天气预报
     */
    @GET("weather/7d")
    suspend fun weather7Day(
        @Query("location") location: String
    ): Weather7Day

    /**
     * 24小时预报
     */
    @GET("weather/24h")
    suspend fun weather24Hour(
        @Query("location") location: String
    ): Weather24Hour

    /**
     * 空气质量
     */
    @GET("air/now")
    suspend fun weatherAir(
        @Query("location") location: String
    ): WeatherAir

    /**
     * 生活指数
     */
    @GET("indices/1d")
    suspend fun weatherIndices(
        @Query("location") location: String,
        @Query("type") type: String
    ): WeatherLife

}
