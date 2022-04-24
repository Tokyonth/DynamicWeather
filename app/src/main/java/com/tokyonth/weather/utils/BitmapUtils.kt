package com.tokyonth.weather.utils

import androidx.annotation.DrawableRes
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import java.lang.IllegalArgumentException

import com.tokyonth.weather.App

object BitmapUtils {

    fun getBitmapFromDrawable(@DrawableRes drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(App.context, drawableId)
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else if (drawable is VectorDrawable || drawable is VectorDrawableCompat) {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } else {
            throw IllegalArgumentException("unsupported drawable type")
        }
    }

}
