package com.tokyonth.weather.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData

import com.tokyonth.weather.App
import com.tokyonth.weather.base.BaseViewModel
import com.tokyonth.weather.data.hf.*
import com.tokyonth.weather.network.ApiRepository
import com.tokyonth.weather.network.requestResult

class WeatherViewModel(application: Application) : BaseViewModel(application) {

    companion object {

        private val SNAPSHOT_PATH =
            App.context.getExternalFilesDir("weather")!!.absolutePath + "/snapshot.json"

    }

    var nowLiveData = MutableLiveData<WeatherNow>()

    var hour24LiveData = MutableLiveData<Weather24Hour>()

    var airLiveData = MutableLiveData<WeatherAir>()

    var lifeLiveData = MutableLiveData<WeatherLife>()

    var sunLiveData = MutableLiveData<WeatherSun>()

    var dailyLiveData = MutableLiveData<Weather7Day>()

    var errorLiveData = MutableLiveData<String>()

    var refreshLiveData = MutableLiveData<Boolean>()

    fun getCityWeather(locationCode: String) {
        requestResult(
            apiBlock = {
                ApiRepository.api.weatherNow(locationCode)
            },
            onSuccess = {
                nowLiveData.value = it
            },
            onError = {
                errorLiveData.value = it.errorMsg
            })
    }

    fun get24HourWeather(locationCode: String) {
        requestResult(
            apiBlock = {
                ApiRepository.api.weather24Hour(locationCode)
            },
            onSuccess = {
                hour24LiveData.value = it
            },
            onError = {
                errorLiveData.value = it.errorMsg
            })
    }

    fun get7DayWeather(locationCode: String) {
        requestResult(
            apiBlock = {
                ApiRepository.api.weather7Day(locationCode)
            },
            onSuccess = {
                dailyLiveData.value = it
            },
            onError = {
                errorLiveData.value = it.errorMsg
            })
    }

    fun getAirWeather(locationCode: String) {
        requestResult(
            apiBlock = {
                ApiRepository.api.weatherAir(locationCode)
            },
            onSuccess = {
                airLiveData.value = it
            },
            onError = {
                errorLiveData.value = it.errorMsg
            })
    }

    fun getLifeWeather(locationCode: String, type: String) {
        requestResult(
            apiBlock = {
                ApiRepository.api.weatherIndices(locationCode, type)
            },
            onSuccess = {
                lifeLiveData.value = it
            },
            onError = {
                errorLiveData.value = it.errorMsg
            })
    }

    fun getSunWeather(locationCode: String, date: String) {
        requestResult(
            apiBlock = {
                ApiRepository.api.weatherSun(locationCode, date)
            },
            onSuccess = {
                sunLiveData.value = it
            },
            onError = {
                errorLiveData.value = it.errorMsg
            })
    }

    fun loadSnapshot() {
/*        val json = FileUtils.read(SNAPSHOT_PATH)
        if (json != null) {
            val snapshot = Gson().fromJson(json, WeatherEntity::class.java)
            successLiveData.value = snapshot
        }*/
    }

}
