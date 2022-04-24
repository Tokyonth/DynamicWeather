package com.tokyonth.weather.service.notification

import android.os.Build
import android.widget.RemoteViews
import android.content.Intent
import android.app.PendingIntent
import android.content.Context
import android.net.Uri

import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.tokyonth.weather.R
import com.tokyonth.weather.ui.activity.MainActivity
import com.tokyonth.weather.utils.ktx.string

class WeatherNotification(val context: Context) {

    companion object {

        fun with(context: Context): WeatherNotification {
            val instance: WeatherNotification by lazy {
                WeatherNotification(context).apply {
                    initNotify()
                }
            }
            return instance
        }

        private const val notificationId = 0xFFFF70

    }

    private lateinit var notifyWeatherImpl: WeatherNotificationImpl

    fun start(isOpenNotify: Boolean) {
        if (isOpenPermission()) {
            if (isOpenNotify) {
                notifyWeatherImpl.notifyWeather(notificationId)
            } else {
                notifyWeatherImpl.cancelWeather(notificationId)
            }
        }
    }

    private fun initNotify() {
        notifyWeatherImpl = WeatherNotificationImpl(context, createCustomView())
    }

    private fun isOpenPermission(): Boolean {
        if (!notifyWeatherImpl.isNotificationEnabled()) {
            MaterialAlertDialogBuilder(context)
                .setTitle(string(R.string.text_tips))
                .setMessage(string(R.string.text_none_notification_permission))
                .setPositiveButton(string(R.string.text_definite)) { _, _ ->
                    val intent = Intent()
                        .setAction("android.settings.APPLICATION_DETAILS_SETTINGS")
                        .setData(Uri.fromParts("package", context.packageName, null))
                    context.startActivity(intent)
                }.create()
                .show()
            return false
        }
        return true
    }

    private fun createPendIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getActivity(context, -1, intent, flag)
    }

    private fun createCustomView(): RemoteViews {
        //val weatherIconPath = WeatherHelper.getWeatherImagePath(weatherEntry.img)
        val remoteViews =
            RemoteViews(context.packageName, R.layout.layout_notifition_weather).apply {
                setOnClickPendingIntent(R.id.ll_notification_weather, createPendIntent())
                /*setImageViewResource(R.id.iv_notification_icon, weatherIconPath)
                setTextViewText(R.id.tv_notification_city, weatherEntry.cityName)
                setTextViewText(
                    R.id.tv_notification_weather, weatherEntry.weather + "\t" +
                            weatherEntry.temp + context.getString(R.string.celsius)
                )*/
                /*val icon = Icon.createWithResource(App.context.packageName, weatherIconPath)
                setImageViewIcon(R.id.notification_iv, icon)*/
               // setTextViewText(R.id.tv_notification_time, DateUtils.systemDate)
            }
        return remoteViews
    }

}
