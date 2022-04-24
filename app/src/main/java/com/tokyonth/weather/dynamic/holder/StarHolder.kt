package com.tokyonth.weather.dynamic.holder

import android.graphics.drawable.GradientDrawable

import kotlin.math.roundToInt

import com.tokyonth.weather.dynamic.BaseHolder

class StarHolder(
    var x: Float, var y: Float, var w: Float, var h: Float, // [0,1]
    private val maxAlpha: Float
) : BaseHolder() {

    private var alphaIsGrowing = true
    private var curAlpha: Float

    init {
        curAlpha = getRandom(0F, maxAlpha)
    }

    fun updateRandom(drawable: GradientDrawable, mAlpha: Float) {
        val delta = getRandom(0.003F * maxAlpha, 0.012F * maxAlpha)
        if (alphaIsGrowing) {
            curAlpha += delta
            if (curAlpha > maxAlpha) {
                curAlpha = maxAlpha
                alphaIsGrowing = false
            }
        } else {
            curAlpha -= delta
            if (curAlpha < 0) {
                curAlpha = 0F
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
