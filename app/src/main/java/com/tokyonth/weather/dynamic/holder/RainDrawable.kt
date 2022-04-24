package com.tokyonth.weather.dynamic.holder

import android.graphics.Canvas
import android.graphics.Paint

class RainDrawable {

    var x = 0f
    var y = 0f
    var length = 0f
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.style = Paint.Style.STROKE
    }

    fun setColor(color: Int) {
        paint.color = color
    }

    fun setStrokeWidth(strokeWidth: Float) {
        paint.strokeWidth = strokeWidth
    }

    fun setLocation(x: Float, y: Float, length: Float) {
        this.x = x
        this.y = y
        this.length = length
    }

    fun draw(canvas: Canvas) {
        canvas.drawLine(x, y - length, x, y, paint)
    }

}
