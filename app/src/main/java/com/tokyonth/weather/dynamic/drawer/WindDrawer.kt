package com.tokyonth.weather.dynamic.drawer

import android.graphics.Canvas
import android.graphics.Paint

import com.tokyonth.weather.dynamic.BaseDrawer
import com.tokyonth.weather.dynamic.holder.ArcHolder
import com.tokyonth.weather.dynamic.data.SkyBackground
import com.tokyonth.weather.utils.RandomUtils.getDownRandFloat
import com.tokyonth.weather.utils.RandomUtils.getRandom
import com.tokyonth.weather.utils.ktx.dp2px

import java.util.ArrayList

class WindDrawer(isNight: Boolean) : BaseDrawer<ArcHolder>(isNight) {

    companion object {
        private const val WIND_COUNT = 30
    }

    override fun drawWeather(canvas: Canvas, sensorX: Float, alpha: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        for (holder in getDefaultHolders()) {
            holder.updateAndDraw(canvas, paint, alpha)
        }
    }

    override fun makeSkyBackground(): Pair<IntArray, IntArray> {
        return Pair(SkyBackground.RAIN_N, SkyBackground.RAIN_D)
    }

    override fun fillHolders(holders: ArrayList<ArcHolder>) {
        val width = getWidth()
        val cx = -width * 0.3F
        val cy = -width * 1.5F
        for (i in 0 until WIND_COUNT) {
            val radiusWidth: Float = getRandom(width * 1.3F, width * 3.0F)
            val radiusHeight: Float =
                radiusWidth * getRandom(0.92F, 0.96F) //getRandom(width * 0.02f,  width * 1.6f);
            val strokeWidth: Float = getDownRandFloat(1F, 2.5F).dp2px()
            val sizeDegree: Float = getDownRandFloat(8F, 15F)
            holders.add(
                ArcHolder(
                    cx, cy, radiusWidth, radiusHeight, strokeWidth, 30F, 99F, sizeDegree,
                    if (isNight) 0x33FFFFFF else 0x66FFFFFF
                )
            )
        }
    }

}
