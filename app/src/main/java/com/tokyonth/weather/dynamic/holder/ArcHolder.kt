package com.tokyonth.weather.dynamic.holder

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

import com.tokyonth.weather.dynamic.BaseHolder

class ArcHolder(
    private val cx: Float,
    private val cy: Float,
    private val radiusWidth: Float,
    private val radiusHeight: Float,
    private val strokeWidth: Float,
    private val fromDegree: Float,
    private val endDegree: Float,
    private val sizeDegree: Float,
    private val color: Int
) : BaseHolder() {

    private var curDegree: Float
    private val stepDegree: Float
    private val rectF = RectF()

    init {
        curDegree = getRandom(fromDegree, endDegree)
        stepDegree = getRandom(0.5F, 0.9F)
    }

    fun updateAndDraw(canvas: Canvas, paint: Paint, alpha: Float) {
        paint.color = convertAlphaColor(alpha * (Color.alpha(color) / 255F), color)
        paint.strokeWidth = strokeWidth
        curDegree += stepDegree * getRandom(0.8F, 1.2F)
        if (curDegree > endDegree - sizeDegree) {
            curDegree = fromDegree - sizeDegree
        }
        val startAngle = curDegree
        rectF.left = cx - radiusWidth
        rectF.top = cy - radiusHeight
        rectF.right = cx + radiusWidth
        rectF.bottom = cy + radiusHeight
        canvas.drawArc(rectF, startAngle, sizeDegree, false, paint)
    }

}
