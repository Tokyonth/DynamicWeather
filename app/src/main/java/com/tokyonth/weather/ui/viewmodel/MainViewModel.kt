package com.tokyonth.weather.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokyonth.weather.data.db.DbManager
import com.tokyonth.weather.data.entity.SavedLocationEntity
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : DynamicViewModel(application) {

    val savedAllCityLiveData = MutableLiveData<List<SavedLocationEntity>>()

    fun getAllCityCount() {
        viewModelScope.launch {
            val all = DbManager.db.getLocationDao().queryAllSavedLocation()
            savedAllCityLiveData.value = all
        }
    }

}
