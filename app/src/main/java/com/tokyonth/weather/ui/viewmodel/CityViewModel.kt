package com.tokyonth.weather.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokyonth.weather.base.BaseViewModel
import com.tokyonth.weather.data.entity.LocationEntity
import com.tokyonth.weather.data.db.DbManager
import com.tokyonth.weather.data.entity.SavedLocationEntity
import com.tokyonth.weather.data.event.CityChangeEvent
import com.tokyonth.weather.data.hf.WeatherNow
import com.tokyonth.weather.network.ApiRepository
import com.tokyonth.weather.network.requestResult
import com.tokyonth.weather.utils.event.LifecycleEventBus
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : BaseViewModel(application) {

    val allSavedCityLiveData = MutableLiveData<MutableList<SavedLocationEntity>>()

    var foldWeatherLiveData = MutableLiveData<Pair<Int, WeatherNow>>()

    val savedCityLiveData = MutableLiveData<SavedLocationEntity?>()

    val deleteCityLiveData = MutableLiveData<Int>()

    fun getAllSavedCity() {
        viewModelScope.launch {
            val all = DbManager.db.getLocationDao().queryAllSavedLocation()
            allSavedCityLiveData.value = all.toMutableList()
        }
    }

    fun getManagerCityWeather(index: Int, cityCode: String) {
        requestResult(
            {
                ApiRepository.api.weatherNow(cityCode)
            }, {
                foldWeatherLiveData.value = Pair(index, it)
            })
    }

    fun deleteCity(position: Int, savedLocationEntity: SavedLocationEntity) {
        viewModelScope.launch {
            val state = DbManager.db.getLocationDao().deleteSavedLocation(savedLocationEntity)
            deleteCityLiveData.value = if (state < 0) {
                -1
            } else {
                position
            }
            LifecycleEventBus.sendEvent(CityChangeEvent())
        }
    }

    fun saveCity(locationEntity: LocationEntity) {
        viewModelScope.launch {
            val isExists = DbManager.db.getLocationDao().querySavedLocationById(locationEntity.locationId)
            val result = if (isExists == null) {
                val savedCityEntity = fillSavedCity(locationEntity)
                val id = DbManager.db.getLocationDao().insertSavedLocation(savedCityEntity)
                savedCityEntity.apply {
                    autoId = id[0]
                }
            } else {
                null
            }
            savedCityLiveData.value = result
            LifecycleEventBus.sendEvent(CityChangeEvent())
        }
    }

    private fun fillSavedCity(locationEntity: LocationEntity): SavedLocationEntity {
        return SavedLocationEntity(
            0,
            locationEntity.locationId,
            locationEntity.locationNameZH,
            null, null,
            null, null,
        )
    }

    fun fillWeather(source: SavedLocationEntity, weatherNow: WeatherNow) {
        val result = source.apply {
            weather = weatherNow.now.text
            temp = weatherNow.now.temp
            img = weatherNow.now.icon
            isInTime = 0/*DateUtils.dayOrNightByInt(weatherEntity)*/
        }
        viewModelScope.launch {
            DbManager.db.getLocationDao().updateSavedLocation(result)
        }
    }

}
