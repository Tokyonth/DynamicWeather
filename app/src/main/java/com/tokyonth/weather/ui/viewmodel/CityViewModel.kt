package com.tokyonth.weather.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokyonth.weather.base.BaseViewModel
import com.tokyonth.weather.data.entity.LocationEntity
import com.tokyonth.weather.data.db.DbManager
import com.tokyonth.weather.data.entity.SavedCityEntity
import com.tokyonth.weather.data.hf.WeatherNow
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : BaseViewModel(application) {

    val allSavedCityLiveData = MutableLiveData<MutableList<SavedCityEntity>>()

    var foldWeatherLiveData = MutableLiveData<Pair<Int, WeatherNow>>()

    val savedCityLiveData = MutableLiveData<SavedCityEntity>()

    val deleteCityLiveData = MutableLiveData<Int>()

    fun getAllSavedCity() {
        viewModelScope.launch {
            val all = DbManager.db.getCityDao().queryAllSavedCity()
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

    fun deleteCity(position: Int, savedCityEntity: SavedCityEntity) {
        viewModelScope.launch {
            val state = DbManager.db.getCityDao().deleteSavedCity(savedCityEntity)
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
            val id = DbManager.db.getCityDao().insertSavedCity(savedCityEntity)
            savedCityEntity.autoId = id[0]
            savedCityLiveData.value = savedCityEntity
        }
    }

    private fun fillSavedCity(locationEntity: LocationEntity): SavedCityEntity {
        return SavedCityEntity(
            0,
            locationEntity.locationId,
            locationEntity.locationNameZH,
            null, null,
            null, null,
        )
    }

    fun fillWeather(source: SavedCityEntity, weatherNow: WeatherNow) {
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
            DbManager.db.getCityDao().updateSavedCity(result)
        }
    }

}
