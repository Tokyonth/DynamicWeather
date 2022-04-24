package com.tokyonth.weather.dynamic.drawer

import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable

import com.tokyonth.weather.dynamic.BaseDrawer
import com.tokyonth.weather.dynamic.holder.StarHolder
import com.tokyonth.weather.dynamic.data.SkyBackground
import com.tokyonth.weather.utils.RandomUtils.getDownRandFloat
import com.tokyonth.weather.utils.RandomUtils.getRandom
import com.tokyonth.weather.utils.ktx.dp2px

import java.util.ArrayList
import kotlin.math.sqrt

class StarDrawer(isNight: Boolean) : BaseDrawer<StarHolder>(isNight) {

    companion object {
        private const val STAR_COUNT = 80
        private const val STAR_MIN_SIZE = 2F // dp
        private const val STAR_MAX_SIZE = 6F // dp
    }

    override fun drawWeather(canvas: Canvas, sensorX: Float, alpha: Float) {
        val drawable: GradientDrawable =
            GradientDrawable(
                GradientDrawable.Orientation.BL_TR,
                intArrayOf(-0x1, 0x00FFFFFF)
            ).apply {
                shape = GradientDrawable.OVAL
                gradientType = GradientDrawable.RADIAL_GRADIENT
                gradientRadius = (sqrt(2.0) * 60).toFloat()
            }

        for (holder in getDefaultHolders()) {
            holder.updateRandom(drawable, alpha)
            drawable.draw(canvas)
        }
    }

    override fun makeSkyBackground(): Pair<IntArray, IntArray> {
        return Pair(SkyBackground.CLEAR_N, SkyBackground.CLEAR_N)
    }

    override fun fillHolders(holders: ArrayList<StarHolder>) {
        val starMinSize: Float = STAR_MIN_SIZE.dp2px()
        val starMaxSize: Float = STAR_MAX_SIZE.dp2px()
        for (i in 0 until STAR_COUNT) {
            val starSize: Float = getRandom(starMinSize, starMaxSize)
            val y: Float = getDownRandFloat(0F, getHeight())
            // 20%的上半部分屏幕最高alpha为1，其余的越靠下最高alpha越小
            val maxAlpha = 0.2F + 0.8F * (1F - y / getHeight())
            val holder = StarHolder(getRandom(0F, getWidth()), y, starSize, starSize, maxAlpha)
            holders.add(holder)
        }
    }

}
