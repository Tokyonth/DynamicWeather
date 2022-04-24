package com.tokyonth.weather.dynamic.drawer

import android.graphics.Canvas
import android.graphics.Paint

import com.tokyonth.weather.dynamic.BaseDrawer
import com.tokyonth.weather.dynamic.holder.CircleHolder
import com.tokyonth.weather.dynamic.data.SkyBackground

import java.util.ArrayList

class CloudyDrawer(isNight: Boolean) : BaseDrawer<CircleHolder>(isNight) {

    override fun drawWeather(canvas: Canvas, sensorX: Float, alpha: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        for (holder in getDefaultHolders()) {
            holder.updateAndDraw(canvas, paint, alpha)
        }
    }

    override fun makeSkyBackground(): Pair<IntArray, IntArray> {
        return Pair(SkyBackground.CLOUDY_N, SkyBackground.CLOUDY_D)
    }

    override fun fillHolders(holders: ArrayList<CircleHolder>) {
        val width = getWidth()
        holders.apply {
            add(
                CircleHolder(
                    0.08F * width, 0.01F * width, 0.04F * width, 0.045F * width, 0.44F * width,
                    0.018F, if (isNight) 0x153C6B8C else 0x45FFFFFF
                )
            )
            add(
                CircleHolder(
                    0.20F * width, -0.30F * width, 0.06F * width, 0.022F * width, 0.56F * width,
                    0.015F, if (isNight) 0x183C6B8C else 0x28FFFFFF
                )
            )
            add(
                CircleHolder(
                    0.58F * width, -0.05F * width, -0.15F * width, 0.052F * width, 0.6F * width,
                    0.0125F, if (isNight) 0x223C6B8C else 0x33FFFFFF
                )
            )
            add(
                CircleHolder(
                    0.9F * width, 0.19F * width, 0.08F * width, -0.015F * width, 0.44F * width,
                    0.021F, if (isNight) 0x153C6B8C else 0x15FFFFFF
                )
            )
            add(
                CircleHolder(
                    0.8F * width, -0.10F * width, 0.04F * width, 0.045F * width, 0.44F * width,
                    0.018F, if (isNight) 0x153C6B8C else 0x45FFFFFF
                )
            )
        }
    }

}
