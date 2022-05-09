package com.tokyonth.weather.service.notification

import android.os.Build
import android.app.AppOpsManager
import android.app.NotificationChannel
import android.content.Context
import android.app.NotificationManager
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

import com.tokyonth.weather.R
import com.tokyonth.weather.utils.ktx.string

import java.lang.Exception

class WeatherNotificationImpl(val context: Context, private val remoteViews: RemoteViews) :
    WeatherNotificationPresenter {

    private var mNotificationManager: NotificationManager? = null

    private fun getNotificationManager(): NotificationManager {
        if (mNotificationManager == null) {
            synchronized(WeatherNotificationImpl::class.java) {
                if (mNotificationManager == null) {
                    mNotificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                }
            }
        }
        return mNotificationManager!!
    }

    fun isNotificationEnabled(): Boolean {
        if (Build.VERSION.SDK_INT >= 24) {
            return getNotificationManager().areNotificationsEnabled()
        }
        val mAppOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val pag = context.applicationContext.packageName
        val uid = context.applicationInfo.uid
        try {
            val appOpsClass = Class.forName(AppOpsManager::class.java.name)
            val method = appOpsClass.getMethod(
                "checkOpNoThrow",
                Integer.TYPE,
                Integer.TYPE,
                String::class.java
            )
            val field = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION")
            val value = field[Int::class.java] as Int
            return method.invoke(mAppOps, value, uid, pag) as Int == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private val notificationBuilder: NotificationCompat.Builder
        get() {
            val channelId = string(R.string.app_name)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    channelId,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    enableLights(false)
                    setSound(null, null)
                }
                getNotificationManager().createNotificationChannel(channel)
            }
            val notification = NotificationCompat.Builder(context, channelId).apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setAutoCancel(true)
                setOngoing(true) //设置无法取消
                setAutoCancel(false) //点击后不取消
                setCustomContentView(remoteViews)
            }
            return notification
        }

    override fun notifyWeather(id: Int) {
        if (!isNotificationEnabled()) {
            return
        }
        getNotificationManager().notify(id, notificationBuilder.build())
    }

    override fun cancelWeather(id: Int) {
        getNotificationManager().cancel(id)
    }

}
