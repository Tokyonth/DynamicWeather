package com.tokyonth.weather.utils.manager

import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

import com.tokyonth.weather.App

class AmapLocationManager {

    companion object {

        val INSTANCE: AmapLocationManager by lazy {
            AmapLocationManager()
        }

    }

    private var locationClient: AMapLocationClient? = null
    private var locationOption: AMapLocationClientOption? = null
    private var locationObs: ((Pair<String, String>?) -> Unit)? = null

    init {
        locationClient = AMapLocationClient(App.context).apply {
            locationOption = defaultOption
            setLocationOption(locationOption)
            setLocationListener(LocationListener())
        }
    }

    fun currentLocal(locationObs: (Pair<String, String>?) -> Unit) {
        this.locationObs = locationObs
        startLocation()
    }

    private val defaultOption: AMapLocationClientOption
        get() {
            val mOption = AMapLocationClientOption().apply {
                locationMode =
                    AMapLocationClientOption.AMapLocationMode.Hight_Accuracy //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
                isGpsFirst = false //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
                httpTimeOut = 6000 //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效(已设置6秒超时)
                interval = 1000 //可选，设置定位间隔。默认为2秒(已设置1秒)
                isNeedAddress = true //可选，设置是否返回逆地理地址信息。默认是true
                isOnceLocation = false //可选，设置是否单次定位。默认是false
                isOnceLocationLatest =
                    false //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
                AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP) //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
                isSensorEnable = false //可选，设置是否使用传感器。默认是false
                isWifiScan =
                    true //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
                isLocationCacheEnable = true //可选，设置是否使用缓存定位，默认为true
                geoLanguage =
                    AMapLocationClientOption.GeoLanguage.DEFAULT //可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
            }
            return mOption
        }

    /**
     * 定位监听
     */
    private inner class LocationListener : AMapLocationListener {
        override fun onLocationChanged(aMapLocation: AMapLocation) {
            // errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (aMapLocation.errorCode == 0) {
                val cityName = aMapLocation.city
                val districtName = aMapLocation.district
                val longitude = aMapLocation.longitude.toString()
                val latitude = aMapLocation.latitude.toString()

                Log.e(
                    "打印-->", "AD:${aMapLocation.adCode}," +
                            "City:${aMapLocation.cityCode}"
                )
                locationObs?.invoke(Pair(aMapLocation.adCode, aMapLocation.district))
            } else {
                locationObs?.invoke(null)
            }
            stopLocation()
        }
    }

    private fun startLocation() {
        locationClient?.apply {
            setLocationOption(locationOption)
            this.startLocation()
        }
    }

    private fun stopLocation() {
        locationClient?.run {
            this.stopLocation()
            this.onDestroy()
        }
        locationClient = null
        locationOption = null
    }

    fun stop() {
        stopLocation()
    }

}
