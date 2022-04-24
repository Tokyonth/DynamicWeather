package com.tokyonth.weather.dynamic.drawer

import android.graphics.Canvas

import com.tokyonth.weather.dynamic.BaseDrawer
import com.tokyonth.weather.dynamic.holder.RainHolder
import com.tokyonth.weather.dynamic.holder.RainDrawable
import com.tokyonth.weather.dynamic.data.SkyBackground
import com.tokyonth.weather.utils.RandomUtils.getRandom
import com.tokyonth.weather.utils.ktx.dp2px

import java.util.ArrayList

class RainDrawer(isNight: Boolean) : BaseDrawer<RainHolder>(isNight) {

    companion object {
        private const val RAIN_COUNT = 50
    }

    override fun drawWeather(canvas: Canvas, sensorX: Float, alpha: Float) {
        val drawable = RainDrawable()
        for (holder in getDefaultHolders()) {
            holder.updateRandom(drawable, alpha)
            drawable.draw(canvas)
        }
    }

    override fun makeSkyBackground(): Pair<IntArray, IntArray> {
        return Pair(SkyBackground.RAIN_N, SkyBackground.RAIN_D)
    }

    override fun fillHolders(holders: ArrayList<RainHolder>) {
        val rainWidth: Float = 2.dp2px() //*(1f -  getDownRandFloat(0, 1));
        val minRainHeight: Float = 8.dp2px()
        val maxRainHeight: Float = 14.dp2px()
        val speed: Float = 400.dp2px()
        for (i in 0 until RAIN_COUNT) {
            val x: Float = getRandom(0F, getWidth())
            val holder =
                RainHolder(x, rainWidth, minRainHeight, maxRainHeight, getHeight(), speed)
            holders.add(holder)
        }
    }

}
