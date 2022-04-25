package com.tokyonth.weather.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData

import com.tokyonth.weather.base.BaseViewModel
import com.tokyonth.weather.data.hf.WeatherNow

open class DynamicViewModel(application: Application) : BaseViewModel(application) {

    val backgroundChangeLiveData = MutableLiveData<WeatherNow>()

    val cityChangeLiveData = MutableLiveData<String>()

}
