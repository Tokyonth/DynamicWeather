package com.tokyonth.weather.dynamic.holder

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

import com.tokyonth.weather.dynamic.BaseHolder
import com.tokyonth.weather.utils.RandomUtils

class CircleHolder(
    private val cx: Float,
    private val cy: Float,
    private val dx: Float,
    private val dy: Float,
    private val radius: Float,
    private val percentSpeed: Float,
    private val color: Int
) : BaseHolder() {

    private var isGrowing = true
    private var curPercent = 0F

    fun updateAndDraw(canvas: Canvas, paint: Paint, alpha: Float) {
        val randomPercentSpeed: Float =
            RandomUtils.getRandom(percentSpeed * 0.7F, percentSpeed * 1.3F)
        if (isGrowing) {
            curPercent += randomPercentSpeed
            if (curPercent > 1F) {
                curPercent = 1F
                isGrowing = false
            }
        } else {
            curPercent -= randomPercentSpeed
            if (curPercent < 0F) {
                curPercent = 0F
                isGrowing = true
            }
        }
        val curCX = cx + dx * curPercent
        val curCY = cy + dy * curPercent
        val curColor: Int = convertAlphaColor(alpha * (Color.alpha(color) / 255F), color)
        paint.color = curColor
        canvas.drawCircle(curCX, curCY, radius, paint)
    }

}
