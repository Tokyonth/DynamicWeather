package com.tokyonth.weather.dynamic.holder

import android.graphics.drawable.GradientDrawable

import kotlin.math.roundToInt

import com.tokyonth.weather.dynamic.BaseHolder

class SunnyHolder(var x: Float, var y: Float, var w: Float, var h: Float) : BaseHolder() {

    private val maxAlpha = 1F
    private val minAlpha = 0.5F
    private var alphaIsGrowing = true
    private var curAlpha: Float

    init {
        curAlpha = getRandom(minAlpha, maxAlpha)
    }

    fun updateRandom(drawable: GradientDrawable, mAlpha: Float) {
        val delta = getRandom(0.002F * maxAlpha, 0.005F * maxAlpha)
        if (alphaIsGrowing) {
            curAlpha += delta
            if (curAlpha > maxAlpha) {
                curAlpha = maxAlpha
                alphaIsGrowing = false
            }
        } else {
            curAlpha -= delta
            if (curAlpha < minAlpha) {
                curAlpha = minAlpha
                alphaIsGrowing = true
            }
        }
        val left = (x - w / 2F).roundToInt()
        val right = (x + w / 2F).roundToInt()
        val top = (y - h / 2F).roundToInt()
        val bottom = (y + h / 2F).roundToInt()
        drawable.apply {
            setBounds(left, top, right, bottom)
            gradientRadius = w / 2.2F
            alpha = (255 * curAlpha * mAlpha).toInt()
        }
    }

}
