package com.tokyonth.weather.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokyonth.weather.data.db.DbManager
import com.tokyonth.weather.data.entity.SavedCityEntity
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : DynamicViewModel(application) {

    private val _savedAllCityLiveData = MutableLiveData<List<SavedCityEntity>>()

    val savedAllCityLiveData = _savedAllCityLiveData

    fun getAllCityCount() {
        viewModelScope.launch {
            val all = DbManager.db.getCityDao().queryAllSavedCity()
            savedAllCityLiveData.value = all
        }
    }

}
