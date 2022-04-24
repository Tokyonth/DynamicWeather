package com.tokyonth.weather.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData

import com.tokyonth.weather.base.BaseViewModel
import com.tokyonth.weather.data.hf.WeatherNow

open class DynamicViewModel(application: Application) : BaseViewModel(application) {

    private val _backgroundChangeLiveData = MutableLiveData<WeatherNow>()

    val backgroundChangeLiveData: MutableLiveData<WeatherNow> = _backgroundChangeLiveData

    private val _cityChangeLiveData = MutableLiveData<String>()

    val cityChangeLiveData: MutableLiveData<String> = _cityChangeLiveData

}
