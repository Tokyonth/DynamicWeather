package com.tokyonth.weather.dynamic.drawer

import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable

import com.tokyonth.weather.dynamic.BaseDrawer
import com.tokyonth.weather.dynamic.holder.RainDrawable
import com.tokyonth.weather.dynamic.holder.SnowHolder
import com.tokyonth.weather.dynamic.holder.RainHolder
import com.tokyonth.weather.dynamic.data.SkyBackground
import com.tokyonth.weather.utils.RandomUtils.getRandom
import com.tokyonth.weather.utils.ktx.dp2px

import java.util.ArrayList

class RainAndSnowDrawer(isNight: Boolean) : BaseDrawer<SnowHolder>(isNight) {

    companion object {
        private const val SNOW_COUNT = 15
        private const val RAIN_COUNT = 30
        private const val MIN_SIZE = 6F // dp
        private const val MAX_SIZE = 14F // dp
    }

    private val rainHolders = ArrayList<RainHolder>()

    private var snowDrawable: GradientDrawable? = null
    private var rainDrawable: RainDrawable? = null

    override fun drawWeather(canvas: Canvas, sensorX: Float, alpha: Float) {
        initDrawable()
        for (holder in getDefaultHolders()) {
            holder.updateRandom(snowDrawable!!, alpha)
            snowDrawable?.draw(canvas)
        }
        for (holder in rainHolders) {
            holder.updateRandom(rainDrawable!!, alpha)
            rainDrawable?.draw(canvas)
        }
    }

    private fun initDrawable() {
        snowDrawable = GradientDrawable(
            GradientDrawable.Orientation.BL_TR,
            intArrayOf(-0x66000001, 0x00FFFFFF)
        ).apply {
            shape = GradientDrawable.OVAL
            gradientType = GradientDrawable.RADIAL_GRADIENT
        }

        rainDrawable = RainDrawable()
    }

    override fun makeSkyBackground(): Pair<IntArray, IntArray> {
        return Pair(SkyBackground.RAIN_N, SkyBackground.RAIN_D)
    }

    override fun fillHolders(holders: ArrayList<SnowHolder>) {
        val minSize: Float = MIN_SIZE.dp2px()
        val maxSize: Float = MAX_SIZE.dp2px()
        val speedSnow: Float = 200.dp2px() // 40当作中雪
        for (i in 0 until SNOW_COUNT) {
            val size: Float = getRandom(minSize, maxSize)
            val holder = SnowHolder(getRandom(0F, getWidth()), size, getHeight(), speedSnow)
            holders.add(holder)
        }

        if (rainHolders.size == 0) {
            val rainWidth: Float = 2.dp2px() //*(1f -  getDownRandFloat(0, 1));
            val minRainHeight: Float = 8.dp2px()
            val maxRainHeight: Float = 14.dp2px()
            val speedRain: Float = 360.dp2px()
            for (i in 0 until RAIN_COUNT) {
                val x: Float = getRandom(0F, getWidth())
                val holder =
                    RainHolder(x, rainWidth, minRainHeight, maxRainHeight, getHeight(), speedRain)
                rainHolders.add(holder)
            }
        }
    }

}
