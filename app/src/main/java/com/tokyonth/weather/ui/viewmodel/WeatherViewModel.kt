package com.tokyonth.weather.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.tokyonth.weather.App
import com.tokyonth.weather.base.BaseViewModel
import com.tokyonth.weather.data.db.DbManager
import com.tokyonth.weather.data.hf.*
import com.tokyonth.weather.network.ApiRepository
import com.tokyonth.weather.network.requestResult
import com.tokyonth.weather.utils.FileUtils
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : BaseViewModel(application) {

    var nowLiveData = MutableLiveData<WeatherNow>()

    var hour24LiveData = MutableLiveData<Weather24Hour>()

    var airLiveData = MutableLiveData<WeatherAir>()

    var lifeLiveData = MutableLiveData<WeatherLife>()

    var dailyLiveData = MutableLiveData<Weather7Day>()

    var refreshLiveData = MutableLiveData<Unit>()

    @Volatile
    private var onFinishedApi = 0

    private var locationCode: String? = null

    fun setLocation(locationCode: String) {
        this.locationCode = locationCode
    }

    private fun getNowWeather() {
        requestResult(
            apiBlock = {
                ApiRepository.api.weatherNow(locationCode!!)
            },
            onSuccess = {
                nowLiveData.value = it
            },
            onError = {

            },
            onFinished = {
                optApiCount()
            })
    }

    private fun get24HourWeather() {
        requestResult(
            apiBlock = {
                ApiRepository.api.weather24Hour(locationCode!!)
            },
            onSuccess = {
                hour24LiveData.value = it
            },
            onError = {

            },
            onFinished = {
                optApiCount()
            })
    }

    private fun get7DayWeather() {
        requestResult(
            apiBlock = {
                ApiRepository.api.weather7Day(locationCode!!)
            },
            onSuccess = {
                dailyLiveData.value = it
            },
            onError = {

            },
            onFinished = {
                optApiCount()
            })
    }

    private fun getAirWeather() {
        requestResult(
            apiBlock = {
                ApiRepository.api.weatherAir(locationCode!!)
            },
            onSuccess = {
                airLiveData.value = it
            },
            onError = {

            },
            onFinished = {
                optApiCount()
            })
    }

    private fun getLifeWeather() {
        requestResult(
            apiBlock = {
                ApiRepository.api.weatherIndices(locationCode!!, "1,2,3,5,9,11,16")
            },
            onSuccess = {
                lifeLiveData.value = it
            },
            onError = {

            },
            onFinished = {
                optApiCount()
            })
    }

    fun pickLocation(adCode: String) {
        viewModelScope.launch {
            val location = DbManager.db.getLocationDao().queryLocationByAdCode(adCode)
            locationCode = location!!.locationId
            refreshWeather()
        }
    }

    private fun optApiCount() {
        onFinishedApi++
        if (onFinishedApi == 5) {
            refreshLiveData.value = Unit
            onFinishedApi = 0
        }
    }

    fun refreshWeather() {
        getNowWeather()
        get24HourWeather()
        get7DayWeather()
        getAirWeather()
        getLifeWeather()
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
