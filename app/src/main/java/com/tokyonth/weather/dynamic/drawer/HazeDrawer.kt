package com.tokyonth.weather.dynamic.drawer

import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable

import com.tokyonth.weather.dynamic.BaseDrawer
import com.tokyonth.weather.dynamic.holder.HazeHolder
import com.tokyonth.weather.dynamic.data.SkyBackground
import com.tokyonth.weather.utils.RandomUtils.getDownRandFloat
import com.tokyonth.weather.utils.RandomUtils.getRandom
import com.tokyonth.weather.utils.ktx.dp2px

import java.util.ArrayList

class HazeDrawer(isNight: Boolean) : BaseDrawer<HazeHolder>(isNight) {

    companion object {
        private const val HAZE_COUNT = 80
    }

    private var drawable: GradientDrawable? = null

    private val minDX: Float = 0.04F
    private val maxDX: Float = 0.065F
    private val minDY: Float = -0.02F
    private val maxDY: Float = 0.02F

    override fun drawWeather(canvas: Canvas, sensorX: Float, alpha: Float) {
        initDrawable()
        for (holder in getDefaultHolders()) {
            holder.updateRandom(
                drawable!!,
                minDX,
                maxDX,
                minDY,
                maxDY,
                0F,
                0F,
                getWidth(),
                getHeight(),
                alpha
            )
            drawable?.draw(canvas)
        }
    }

    private fun initDrawable() {
        drawable = GradientDrawable(
            GradientDrawable.Orientation.BL_TR,
            if (isNight) intArrayOf(0x55D4BA3F, 0x22D4BA3F) else intArrayOf(-0x77335999, 0x33CCA667)
        ).apply {
            shape = GradientDrawable.OVAL
            gradientType = GradientDrawable.RADIAL_GRADIENT
        }
    }

    override fun makeSkyBackground(): Pair<IntArray, IntArray> {
        return Pair(SkyBackground.HAZE_N, SkyBackground.HAZE_D)
    }

    override fun fillHolders(holders: ArrayList<HazeHolder>) {
        val minSize: Float = 0.8F.dp2px()
        val maxSize: Float = 4.4F.dp2px()
        for (i in 0 until HAZE_COUNT) {
            val starSize: Float = getRandom(minSize, maxSize)
            val holder =
                HazeHolder(
                    getRandom(0F, getWidth()),
                    getDownRandFloat(0F, getHeight()),
                    starSize,
                    starSize
                )
            holders.add(holder)
        }
    }

}
