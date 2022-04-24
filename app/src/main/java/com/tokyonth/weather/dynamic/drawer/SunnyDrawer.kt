package com.tokyonth.weather.dynamic.drawer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

import com.tokyonth.weather.dynamic.holder.SunnyHolder
import android.graphics.drawable.GradientDrawable
import com.tokyonth.weather.dynamic.BaseDrawer
import com.tokyonth.weather.dynamic.data.SkyBackground
import com.tokyonth.weather.utils.RandomUtils.getRandom

import java.util.ArrayList

class SunnyDrawer(isNight: Boolean) : BaseDrawer<SunnyHolder>(isNight) {

    companion object {
        private const val SUNNY_COUNT = 3
    }

    private val centerOfWidth = 0.02F

    override fun drawWeather(canvas: Canvas, sensorX: Float, alpha: Float) {
        val drawable: GradientDrawable =
            GradientDrawable(
                GradientDrawable.Orientation.BL_TR,
                intArrayOf(0x20FFFFFF, 0x10FFFFFF)
            ).apply {
                shape = GradientDrawable.OVAL
                gradientType = GradientDrawable.RADIAL_GRADIENT
            }
        for (holder in getDefaultHolders()) {
            holder.updateRandom(drawable, alpha)
            drawable.draw(canvas)
        }
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0x33FFFFFF
            color = Color.argb((alpha * 0.18F * 255F).toInt(), 0xFF, 0xFF, 0xFF)
        }

        val size: Float = getWidth() * centerOfWidth
        canvas.drawCircle(size, size, getWidth() * 0.12F, paint)
    }

    override fun makeSkyBackground(): Pair<IntArray, IntArray> {
        return Pair(SkyBackground.CLEAR_D, SkyBackground.CLEAR_D)
    }

    override fun fillHolders(holders: ArrayList<SunnyHolder>) {
        val minSize = getWidth() * 0.16F
        val maxSize = getWidth() * 1.5F
        val center = getWidth() * centerOfWidth
        val deltaSize = (maxSize - minSize) / SUNNY_COUNT
        for (i in 0 until SUNNY_COUNT) {
            val curSize: Float = maxSize - i * deltaSize * getRandom(0.9F, 1.1F)
            val holder = SunnyHolder(center, center, curSize, curSize)
            holders.add(holder)
        }
    }

}
