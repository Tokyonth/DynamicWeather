package com.tokyonth.weather.dynamic.drawer

import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable

import com.tokyonth.weather.dynamic.BaseDrawer
import com.tokyonth.weather.dynamic.holder.SnowHolder
import com.tokyonth.weather.dynamic.data.SkyBackground
import com.tokyonth.weather.utils.RandomUtils.getRandom
import com.tokyonth.weather.utils.ktx.dp2px

import java.util.ArrayList

class SnowDrawer(isNight: Boolean) : BaseDrawer<SnowHolder>(isNight) {

    companion object {
        private const val SNOW_COUNT = 30
        private const val MIN_SIZE = 12F // dp
        private const val MAX_SIZE = 30F // dp
    }

    override fun drawWeather(canvas: Canvas, sensorX: Float, alpha: Float) {
        val drawable: GradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.BL_TR,
            intArrayOf(-0x66000001, 0x00FFFFFF)
        ).apply {
            shape = GradientDrawable.OVAL
            gradientType = GradientDrawable.RADIAL_GRADIENT
        }

        for (holder in getDefaultHolders()) {
            holder.updateRandom(drawable, alpha)
            drawable.draw(canvas)
        }
    }

    override fun makeSkyBackground(): Pair<IntArray, IntArray> {
        return Pair(SkyBackground.SNOW_N, SkyBackground.SNOW_D)
    }

    override fun fillHolders(holders: ArrayList<SnowHolder>) {
        val minSize: Float = MIN_SIZE.dp2px()
        val maxSize: Float = MAX_SIZE.dp2px()
        val speed: Float = 80.dp2px() // 40当作中雪80
        for (i in 0 until SNOW_COUNT) {
            val size: Float = getRandom(minSize, maxSize)
            val holder = SnowHolder(getRandom(0F, getWidth()), size, getHeight(), speed)
            holders.add(holder)
        }
    }

}
