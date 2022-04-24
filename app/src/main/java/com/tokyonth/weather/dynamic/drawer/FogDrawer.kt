package com.tokyonth.weather.dynamic.drawer

import android.graphics.Canvas
import android.graphics.Paint

import com.tokyonth.weather.dynamic.BaseDrawer
import com.tokyonth.weather.dynamic.holder.CircleHolder
import com.tokyonth.weather.dynamic.data.SkyBackground

import java.util.ArrayList

class FogDrawer(isNight: Boolean) : BaseDrawer<CircleHolder>(isNight) {

    override fun drawWeather(canvas: Canvas, sensorX: Float, alpha: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        for (holder in getDefaultHolders()) {
            holder.updateAndDraw(canvas, paint, alpha)
        }
    }

    override fun makeSkyBackground(): Pair<IntArray, IntArray> {
        return Pair(SkyBackground.FOG_N, SkyBackground.FOG_D)
    }

    override fun fillHolders(holders: ArrayList<CircleHolder>) {
        val width = getWidth()
        holders.apply {
            add(
                CircleHolder(
                    0.20F * width,
                    0.30F * width,
                    -0.06F * width,
                    0.022F * width,
                    0.56F * width,
                    0.0015F,
                    if (isNight) 0x44374D5C else 0x4495A2AB
                )
            )
            add(
                CircleHolder(
                    0.59F * width,
                    0.45F * width,
                    0.12F * width,
                    0.032F * width,
                    0.50F * width,
                    0.00125F,
                    if (isNight) 0x55374D5C else 0x33627D90
                )
            )
            add(
                CircleHolder(
                    1.1F * width,
                    0.25F * width,
                    -0.08F * width,
                    -0.015F * width,
                    0.42F * width,
                    0.0025F,
                    if (isNight) 0x5A374D5C else 0x556F8A8D
                )
            )
        }
    }

}
