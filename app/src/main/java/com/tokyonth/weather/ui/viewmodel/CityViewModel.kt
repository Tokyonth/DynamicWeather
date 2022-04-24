package com.tokyonth.weather.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokyonth.weather.base.BaseViewModel
import com.tokyonth.weather.data.entity.LocationEntity
import com.tokyonth.weather.data.db.DbManager
import com.tokyonth.weather.data.entity.SavedLocationEntity
import com.tokyonth.weather.data.hf.WeatherNow
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : BaseViewModel(application) {

    val allSavedCityLiveData = MutableLiveData<MutableList<SavedLocationEntity>>()

    var foldWeatherLiveData = MutableLiveData<Pair<Int, WeatherNow>>()

    val savedCityLiveData = MutableLiveData<SavedLocationEntity>()

    val deleteCityLiveData = MutableLiveData<Int>()

    fun getAllSavedCity() {
        viewModelScope.launch {
            val all = DbManager.db.getLocationDao().queryAllSavedLocation()
            allSavedCityLiveData.value = all.toMutableList()
        }
    }

    fun getManagerCityWeather(index: Int, cityCode: String) {
/*        requestResult(
            {
                ApiRepository.INSTANCE.getCityWeather(cityCode)
            }, {
                foldWeatherLiveData.value = Pair(index, it!!)
            })*/
    }

    fun deleteCity(position: Int, savedLocationEntity: SavedLocationEntity) {
        viewModelScope.launch {
            val state = DbManager.db.getLocationDao().deleteSavedLocation(savedLocationEntity)
            deleteCityLiveData.value = if (state < 0) {
                -1
            } else {
                position
            }
        }
    }

    fun saveCity(locationEntity: LocationEntity) {
        val savedCityEntity = fillSavedCity(locationEntity)
        viewModelScope.launch {
            val id = DbManager.db.getLocationDao().insertSavedLocation(savedCityEntity)
            savedCityEntity.autoId = id[0]
            savedCityLiveData.value = savedCityEntity
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
/*            name = weatherNow.weather
            temp = weatherNow.temp
            img = weatherNow.img
            quality = weatherNow.aqi.quality
            lowTemp = weatherNow.tempLow
            highTemp = weatherNow.tempHigh
            isInTime = DateUtils.dayOrNightByInt(weatherEntity)*/
        }
        viewModelScope.launch {
            DbManager.db.getLocationDao().updateSavedLocation(result)
        }
    }

}
