package com.tokyonth.weather.dynamic

import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable

import java.util.ArrayList
import kotlin.math.roundToInt

abstract class BaseDrawer<T : BaseHolder>(val isNight: Boolean) {

    abstract fun drawWeather(canvas: Canvas, sensorX: Float, alpha: Float)

    abstract fun makeSkyBackground(): Pair<IntArray, IntArray>

    abstract fun fillHolders(holders: ArrayList<T>)

    private var skyDrawable: GradientDrawable? = null

    private val holders = ArrayList<T>()

    private var width = 0
    private var height = 0

    fun startDraw(canvas: Canvas, sensorX: Float, alpha: Float) {
        drawSkyBackground(canvas, alpha)
        drawWeather(canvas, sensorX, alpha)
    }

    fun setSize(width: Int, height: Int) {
        if (this.width != width && this.height != height) {
            this.width = width
            this.height = height
            if (skyDrawable != null) {
                skyDrawable!!.setBounds(0, 0, width, height)
            }
        }
        if (holders.isEmpty()) {
            fillHolders(holders)
        }
    }

    fun getDefaultHolders(): ArrayList<T> {
        return holders
    }

    fun getWidth(): Float {
        return width.toFloat()
    }

    fun getHeight(): Float {
        return height.toFloat()
    }

    private fun drawSkyBackground(canvas: Canvas, mAlpha: Float) {
        if (skyDrawable == null) {
            val bg: IntArray = if (isNight) {
                makeSkyBackground().first
            } else {
                makeSkyBackground().second
            }
            skyDrawable =
                GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, bg)
        }
        skyDrawable?.run {
            setBounds(0, 0, width, height)
            alpha = (mAlpha * 255F).roundToInt()
            draw(canvas)
        }
    }

}
