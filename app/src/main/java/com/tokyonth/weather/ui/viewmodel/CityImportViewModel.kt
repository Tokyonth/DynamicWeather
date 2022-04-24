package com.tokyonth.weather.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokyonth.weather.App
import com.tokyonth.weather.base.BaseViewModel
import com.tokyonth.weather.data.entity.LocationEntity
import com.tokyonth.weather.data.db.DbManager
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class CityImportViewModel(application: Application) : BaseViewModel(application) {

    val importCityLiveData = MutableLiveData<Boolean>()

    private fun parseCityCsv(): ArrayList<LocationEntity> {
        val inputStream = App.context.assets.open("China-City-List-latest.csv")
        val buffer = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
        var line: String?
        val list = ArrayList<LocationEntity>()
        while (buffer.readLine().also { line = it } != null) {
            val sp = line?.split(",")!!
            val city = LocationEntity(
                0,
                sp[0], sp[1], sp[2],
                sp[3], sp[4], sp[5],
                sp[6], sp[7], sp[8],
                sp[9], sp[10], sp[11],
                sp[12], sp[13],
            )
            list.add(city)
        }
        return list
    }

    fun importCityData() {
        viewModelScope.launch {
            val parseData = parseCityCsv().toTypedArray()
            DbManager.db.getCityDao().insertCity(*parseData).let {
                importCityLiveData.value = it.size == parseData.size
            }

        }
    }

}
