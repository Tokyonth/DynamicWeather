package com.tokyonth.weather.network

open class BaseResponse {

    private val code: Int = 0

    fun isSuccess(): Boolean {
        return code == 200
    }

}
