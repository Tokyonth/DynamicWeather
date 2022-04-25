package com.tokyonth.weather.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData

import com.tokyonth.weather.App
import com.tokyonth.weather.base.BaseViewModel
import com.tokyonth.weather.data.hf.*
import com.tokyonth.weather.network.ApiRepository
import com.tokyonth.weather.network.requestResult
import com.tokyonth.weather.utils.FileUtils

class WeatherViewModel(application: Application) : BaseViewModel(application) {

    var nowLiveData = MutableLiveData<WeatherNow>()

    var hour24LiveData = MutableLiveData<Weather24Hour>()

    var airLiveData = MutableLiveData<WeatherAir>()

    var lifeLiveData = MutableLiveData<WeatherLife>()

    var sunLiveData = MutableLiveData<WeatherSun>()

    var dailyLiveData = MutableLiveData<Weather7Day>()

    var errorLiveData = MutableLiveData<String>()

    var refreshLiveData = MutableLiveData<Int>()

    private val infoCache = HashMap<String, String>()

    private var onFinishedApi = 0

    fun getCityWeather(locationCode: String) {
        requestResult(
            apiBlock = {
                infoCache["locationCode"] = locationCode
                ApiRepository.api.weatherNow(locationCode)
            },
            onSuccess = {
                nowLiveData.value = it
            },
            onError = {
                errorLiveData.value = it.errorMsg
            },
            onFinished = {
                onFinishedApi++
                refreshLiveData.value = onFinishedApi
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
            },
            onFinished = {
                onFinishedApi++
                refreshLiveData.value = onFinishedApi
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
            },
            onFinished = {
                onFinishedApi++
                refreshLiveData.value = onFinishedApi
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
            },
            onFinished = {
                onFinishedApi++
                refreshLiveData.value = onFinishedApi
            })
    }

    fun getLifeWeather(locationCode: String, type: String) {
        requestResult(
            apiBlock = {
                infoCache["indicesType"] = locationCode
                ApiRepository.api.weatherIndices(locationCode, type)
            },
            onSuccess = {
                lifeLiveData.value = it
            },
            onError = {
                errorLiveData.value = it.errorMsg
            },
            onFinished = {
                onFinishedApi++
                refreshLiveData.value = onFinishedApi
            })
    }

    fun getSunWeather(locationCode: String, date: String) {
        requestResult(
            apiBlock = {
                infoCache["sunData"] = locationCode
                ApiRepository.api.weatherSun(locationCode, date)
            },
            onSuccess = {
                sunLiveData.value = it
            },
            onError = {
                errorLiveData.value = it.errorMsg
            },
            onFinished = {
                onFinishedApi++
                refreshLiveData.value = onFinishedApi
            })
    }

    fun refreshWeather() {
        val locationCode = infoCache["locationCode"]
        val indicesType = infoCache["indicesType"]
        val sunData = infoCache["sunData"]
        if (locationCode != null) {
            getCityWeather(locationCode)
            get24HourWeather(locationCode)
            get7DayWeather(locationCode)
            getAirWeather(locationCode)
            getLifeWeather(locationCode, indicesType!!)
            //  getSunWeather(locationCode, sunData!!)
        }
    }

    fun loadSnapshot(locationCode: String) {
        val path =
            App.context.getExternalFilesDir("weather")?.absolutePath + "/snapshot-$locationCode.json"
        val json = FileUtils.read(path)
        if (json != null) {
            //  val snapshot = Gson().fromJson(json, WeatherEntity::class.java)
            //  successLiveData.value = snapshot
        }
    }

}
