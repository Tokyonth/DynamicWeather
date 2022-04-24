package com.tokyonth.weather.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokyonth.weather.data.db.DbManager
import com.tokyonth.weather.data.entity.SavedLocationEntity
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : DynamicViewModel(application) {

    private val _savedAllCityLiveData = MutableLiveData<List<SavedLocationEntity>>()

    val savedAllCityLiveData = _savedAllCityLiveData

    fun getAllCityCount() {
        viewModelScope.launch {
            val all = DbManager.db.getLocationDao().queryAllSavedLocation()
            savedAllCityLiveData.value = all
        }
    }

}
