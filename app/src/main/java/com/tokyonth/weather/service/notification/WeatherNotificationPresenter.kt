package com.tokyonth.weather.service.notification

interface WeatherNotificationPresenter {

    fun notifyWeather(id: Int)

    fun cancelWeather(id: Int)

}
