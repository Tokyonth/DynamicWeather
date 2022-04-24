package com.tokyonth.weather.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

import java.lang.Exception
import java.util.HashMap

import com.tokyonth.weather.R

class FontTextView : AppCompatTextView {

    companion object {

        const val ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android"

    }

    private val fontCache = HashMap<String, Typeface?>()

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        applyFontStyle(attrs)
    }

    private fun applyFontStyle(attrs: AttributeSet) {
        val textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL)
        val attr = context.obtainStyledAttributes(attrs, R.styleable.FontTextView)
        val fontType = attr.getInt(R.styleable.FontTextView_fontType, -1)
        attr.recycle()
        if (fontType == 0) {
            val tf = getTypeface("fonts/Roboto-Regular.ttf")
            typeface = tf
        } else if (fontType == 1) {
            val tf = selectTypeface(textStyle)
            typeface = tf
        }
    }

    private fun selectTypeface(textStyle: Int): Typeface {
        return when (textStyle) {
            Typeface.BOLD -> getTypeface("fonts/DINPro_Medium.otf")
            Typeface.NORMAL -> getTypeface("fonts/DINPro_Regular.otf")
            else -> getTypeface("fonts/DINPro_Regular.otf")
        }
    }

    private fun getTypeface(fontName: String): Typeface {
        var typeface = fontCache[fontName]
        if (typeface == null) {
            typeface = try {
                Typeface.createFromAsset(context.assets, fontName)
            } catch (e: Exception) {
                throw IllegalStateException("cannot find font: $fontName")
            }
            fontCache[fontName] = typeface
        }
        return typeface!!
    }

}
