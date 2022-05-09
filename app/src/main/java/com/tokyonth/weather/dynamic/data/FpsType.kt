package com.tokyonth.weather.dynamic.data

enum class FpsType(private val id: Int) {

    AUTO(0),
    FPS60(1),
    FPS120(2);

    override fun toString(): String {
        return values()[id].toString()
    }

}
