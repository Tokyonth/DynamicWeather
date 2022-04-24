package com.tokyonth.weather.dynamic.holder

import com.tokyonth.weather.dynamic.BaseHolder
import android.graphics.drawable.GradientDrawable
import kotlin.math.roundToInt

class HazeHolder(var x: Float, var y: Float, var w: Float, var h: Float) : BaseHolder() {

    fun updateRandom(
        drawable: GradientDrawable, minDX: Float, maxDX: Float,
        minDY: Float, maxDY: Float, minX: Float, minY: Float,
        maxX: Float, maxY: Float, alpha: Float
    ) {
        //alpha 还没用
        require(!(maxDX < minDX || maxDY < minDY)) { "max should bigger than min!!!!" }
        x += getRandom(minDX, maxDX) * w
        y += getRandom(minDY, maxDY) * h
        if (x > maxX) {
            x = minX
        } else if (x < minX) {
            x = maxX
        }
        if (y > maxY) {
            y = minY
        } else if (y < minY) {
            y = maxY
        }
        val left = (x - w / 2f).roundToInt()
        val right = (x + w / 2f).roundToInt()
        val top = (y - h / 2f).roundToInt()
        val bottom = (y + h / 2f).roundToInt()
        drawable.alpha = (255f * alpha).toInt()
        drawable.setBounds(left, top, right, bottom)
        drawable.gradientRadius = w / 2.2f
    }

}
