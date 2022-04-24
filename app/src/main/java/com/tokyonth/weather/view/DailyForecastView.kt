package com.tokyonth.weather.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.view.ViewCompat

import java.lang.Exception

import com.tokyonth.weather.data.hf.Weather7Day
import com.tokyonth.weather.utils.DateUtils
import kotlin.math.abs

class DailyForecastView : View {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private var mWidth = 0
    private var mHeight = 0
    private var percent = 0f
    private var density = 0f

    private val tmpMaxPath = Path()
    private val tmpMinPath = Path()

    private var forecastList: List<Weather7Day.DailyItem>? = null
    private var datas: Array<Data?>? = null

    private val paint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    inner class Data {
        var minOffsetPercent = 0f
        var maxOffsetPercent // 差值%
                = 0f
        var tmp_max = 0
        var tmp_min = 0
        var date: String? = null
        var wind_sc: String? = null
        var cond_txt_d: String? = null
    }

    fun resetAnimation() {
        percent = 0f
        invalidate()
    }

    private fun initView(context: Context) {
        density = context.resources.displayMetrics.density
        if (isInEditMode) {
            return
        }
        paint.color = Color.WHITE
        paint.strokeWidth = density
        paint.textSize = 12f * density
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isInEditMode) {
            return
        }
        paint.style = Paint.Style.FILL
        //一共需要 顶部文字2(+图占8行)+底部文字2 + 【间距1 + 日期1 + 间距0.5 +　晴1 + 间距0.5f + 微风1 + 底部边距1f 】 = 18行
        //                                  12     13       14      14.5    15.5      16      17       18
        val textSize = mHeight / 18f
        paint.textSize = textSize
        val textOffset = getTextPaintOffset(paint)
        val dH = textSize * 8f
        val dCenterY = textSize * 6f
        if (datas == null || datas!!.size <= 1) {
            canvas.drawLine(0f, dCenterY, mWidth.toFloat(), dCenterY, paint) //没有数据的情况下只画一条线
            return
        }
        val dW = mWidth * 1f / datas!!.size
        tmpMaxPath.reset()
        tmpMinPath.reset()
        val length = datas!!.size
        val x = FloatArray(length)
        val yMax = FloatArray(length)
        val yMin = FloatArray(length)
        val textPercent = if (percent >= 0.6f) (percent - 0.6f) / 0.4f else 0f
        val pathPercent = if (percent >= 0.6f) 1f else percent / 0.6f

        //画底部的三行文字和标注最高最低温度
        paint.alpha = (255 * textPercent).toInt()
        for (i in 0 until length) {
            val d = datas!![i]
            x[i] = i * dW + dW / 2f
            yMax[i] = dCenterY - d!!.maxOffsetPercent * dH
            yMin[i] = dCenterY - d.minOffsetPercent * dH
            canvas.drawText(
                d.tmp_max.toString() + "°",
                x[i],
                yMax[i] - textSize + textOffset,
                paint
            ) // - textSize
            canvas.drawText(
                d.tmp_min.toString() + "°",
                x[i],
                yMin[i] + textSize + textOffset,
                paint
            )
            canvas.drawText(d.date!!, x[i], textSize * 13.5f + textOffset, paint);//日期d.date.substring(5)
            canvas.drawText(d.cond_txt_d + "", x[i], textSize * 15f + textOffset, paint) //“晴"
            canvas.drawText(d.wind_sc!!, x[i], textSize * 16.5f + textOffset, paint) //微风
        }
        paint.alpha = 255
        for (i in 0 until length - 1) {
            val midX = (x[i] + x[i + 1]) / 2f
            val midYMax = (yMax[i] + yMax[i + 1]) / 2f
            val midYMin = (yMin[i] + yMin[i + 1]) / 2f
            if (i == 0) {
                tmpMaxPath.moveTo(0f, yMax[i])
                tmpMinPath.moveTo(0f, yMin[i])
            }
            tmpMaxPath.cubicTo(x[i] - 1, yMax[i], x[i], yMax[i], midX, midYMax)
            //			tmpMaxPath.quadTo(x[i], yMax[i], midX, midYMax);
            tmpMinPath.cubicTo(x[i] - 1, yMin[i], x[i], yMin[i], midX, midYMin)
            //			tmpMinPath.quadTo(x[i], yMin[i], midX, midYMin);
            if (i == length - 2) {
                tmpMaxPath.cubicTo(
                    x[i + 1] - 1,
                    yMax[i + 1],
                    x[i + 1],
                    yMax[i + 1],
                    mWidth.toFloat(),
                    yMax[i + 1]
                )
                tmpMinPath.cubicTo(
                    x[i + 1] - 1,
                    yMin[i + 1],
                    x[i + 1],
                    yMin[i + 1],
                    mWidth.toFloat(),
                    yMin[i + 1]
                )
            }
        }
        //draw max_tmp and min_tmp path
        paint.style = Paint.Style.STROKE
        val needClip = pathPercent < 1f
        if (needClip) {
            canvas.save()
            canvas.clipRect(0f, 0f, mWidth * pathPercent, mHeight.toFloat())
            //canvas.drawColor(0x66ffffff);
        }
        canvas.drawPath(tmpMaxPath, paint)
        canvas.drawPath(tmpMinPath, paint)
        if (needClip) {
            canvas.restore()
        }
        if (percent < 1) {
            percent += 0.025f
            percent = percent.coerceAtMost(1f)
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    fun setData(weather: Weather7Day) {
        if (forecastList === weather.daily) {
            percent = 0f
            invalidate()
            return
        }
        forecastList = weather.daily
        datas = arrayOfNulls(forecastList!!.size)
        try {
            var allMax = Int.MIN_VALUE
            var allMin = Int.MAX_VALUE
            for (i in forecastList!!.indices) {
                val max = forecastList!![i].tempMax.toInt()
                val min = forecastList!![i].tempMin.toInt()
                if (allMax < max) {
                    allMax = max
                }
                if (allMin > min) {
                    allMin = min
                }
                val data = Data()
                data.tmp_max = max
                data.tmp_min = min
                data.date = DateUtils.format(forecastList!![i].fxDate)
                data.wind_sc = forecastList!![i].windDirDay
                data.cond_txt_d = forecastList!![i].textDay
                datas!![i] = data
            }
            val allDistance = abs(allMax - allMin).toFloat()
            val averageDistance = (allMax + allMin) / 2f
            for (d in datas!!) {
                d!!.maxOffsetPercent = (d.tmp_max - averageDistance) / allDistance
                d.minOffsetPercent = (d.tmp_min - averageDistance) / allDistance
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        percent = 0f
        invalidate()
    }

    private fun getTextPaintOffset(paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        return -(fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.top
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

}
