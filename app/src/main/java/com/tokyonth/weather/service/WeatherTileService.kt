package com.tokyonth.weather.service

import android.os.Build
import android.service.quicksettings.TileService
import android.content.Intent
import androidx.annotation.RequiresApi

import com.tokyonth.weather.ui.activity.MainActivity

@RequiresApi(api = Build.VERSION_CODES.N)
class WeatherTileService : TileService()/*, OnWeatherListener*/ {

    override fun onStartListening() {
/*        val defaultCity = LitePal.find(DefaultCityEntry::class.java, 1)
        if (defaultCity != null) {
            val weatherModel: WeatherModel = WeatherModelImpl()
            weatherModel.loadLocationWeather(defaultCity, this)
        } else {
            Toast.makeText(
                this,
                applicationContext.getString(R.string.text_default_city_not_exist),
                Toast.LENGTH_SHORT
            ).show()
        }*/
    }

    override fun onClick() {
        val intent = Intent().apply {
            setClass(this@WeatherTileService, MainActivity::class.java)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivityAndCollapse(intent)
    }

/*
    override fun loadSuccess(weatherEntry: WeatherEntry) {
        val weatherIconPath = WeatherHelper.getWeatherImagePath(weatherEntry.img)
        val icon = Icon.createWithResource(this, weatherIconPath)
        val temp = weatherEntry.temp + applicationContext.getString(R.string.celsius)
        val local = weatherEntry.cityName
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.icon = icon
        qsTile.label = local + temp
        qsTile.state = Tile.STATE_ACTIVE
        qsTile.updateTile()
    }

    override fun loadFailure(msg: String) {
        val icon = Icon.createWithResource(this, R.drawable.weather_nothing)
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.icon = icon
        qsTile.label = "N/A"
        qsTile.state = Tile.STATE_ACTIVE
        qsTile.updateTile()
    }
*/

}