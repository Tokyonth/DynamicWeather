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
    private val paint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private var simpleDataArray: ArrayList<Data> = ArrayList()

    private lateinit var x: FloatArray
    private lateinit var yMax: FloatArray
    private lateinit var yMin: FloatArray

    inner class Data {
        var minOffsetPercent = 0f
        var maxOffsetPercent = 0f // 差值%
        var tmp_max = 0
        var tmp_min = 0
        var date: String? = null
        var wind_sc: String? = null
        var cond_txt_d: String? = null
    }

    private fun initData() {
        val dataLength = simpleDataArray.size
        x = FloatArray(dataLength)
        yMax = FloatArray(dataLength)
        yMin = FloatArray(dataLength)
    }

    private fun initView(context: Context) {
        if (isInEditMode) {
            return
        }
        density = context.resources.displayMetrics.density
        paint.apply {
            color = Color.WHITE
            strokeWidth = density
            textSize = 12f * density
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isInEditMode) {
            return
        }
        if (simpleDataArray.isNullOrEmpty()) {
            return
        }

        drawLine(canvas)
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas) {
        //一共需要 顶部文字2(+图占8行)+底部文字2 + 【间距1 + 日期1 + 间距0.5 +　晴1 + 间距0.5f + 微风1 + 底部边距1f 】 = 18行
        //             12     13       14      14.5    15.5      16      17       18
        val mTextSize = mHeight / 18f
        paint.apply {
            paint.style = Paint.Style.FILL
            textSize = mTextSize
        }

        val fontMetrics = paint.fontMetrics
        val textOffset = -(fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.top
        val textPercent = if (percent >= 0.6f) (percent - 0.6f) / 0.4f else 0f
        val dH = mTextSize * 8f
        val dCenterY = mTextSize * 6f
        val dW = mWidth * 1f / simpleDataArray.size
        //画底部的三行文字和标注最高最低温度
        paint.alpha = (255 * textPercent).toInt()
        for (i in 0 until simpleDataArray.size) {
            val d = simpleDataArray[i]
            x[i] = i * dW + dW / 2f
            yMax[i] = dCenterY - d.maxOffsetPercent * dH
            yMin[i] = dCenterY - d.minOffsetPercent * dH
            canvas.drawText(
                d.tmp_max.toString() + "°",
                x[i],
                yMax[i] - mTextSize + textOffset,
                paint
            )
            canvas.drawText(
                d.tmp_min.toString() + "°",
                x[i],
                yMin[i] + mTextSize + textOffset,
                paint
            )
            canvas.drawText(
                d.date!!,
                x[i],
                mTextSize * 13.5f + textOffset,
                paint
            )
            canvas.drawText(d.cond_txt_d + "", x[i], mTextSize * 15f + textOffset, paint) //“晴"
            canvas.drawText(d.wind_sc!!, x[i], mTextSize * 16.5f + textOffset, paint) //微风
        }
    }

    private fun drawLine(canvas: Canvas) {
        tmpMaxPath.reset()
        tmpMinPath.reset()
        paint.apply {
            alpha = 255
            strokeWidth = 4f
            style = Paint.Style.STROKE
        }

        for (i in 0 until simpleDataArray.size - 1) {
            val midX = (x[i] + x[i + 1]) / 2f
            val midYMax = (yMax[i] + yMax[i + 1]) / 2f
            val midYMin = (yMin[i] + yMin[i + 1]) / 2f
            if (i == 0) {
                tmpMaxPath.moveTo(0f, yMax[i])
                tmpMinPath.moveTo(0f, yMin[i])
            }
            tmpMaxPath.cubicTo(x[i] - 1, yMax[i], x[i], yMax[i], midX, midYMax)
            tmpMinPath.cubicTo(x[i] - 1, yMin[i], x[i], yMin[i], midX, midYMin)
            if (i == simpleDataArray.size - 2) {
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
        val pathPercent = if (percent >= 0.6f) 1f else percent / 0.6f
        val needClip = pathPercent < 1f
        if (needClip) {
            canvas.save()
            canvas.clipRect(0f, 0f, mWidth * pathPercent, mHeight.toFloat())
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
        simpleDataArray.clear()
        val forecastList = weather.daily

        var allMax = Int.MIN_VALUE
        var allMin = Int.MAX_VALUE
        for (i in forecastList.indices) {
            val max = forecastList[i].tempMax.toInt()
            val min = forecastList[i].tempMin.toInt()
            if (allMax < max) {
                allMax = max
            }
            if (allMin > min) {
                allMin = min
            }
            val data = Data().apply {
                tmp_max = max
                tmp_min = min
                date = DateUtils.format(forecastList[i].fxDate)
                wind_sc = forecastList[i].windDirDay
                cond_txt_d = forecastList[i].textDay
            }
            simpleDataArray.add(data)
        }
        val allDistance = abs(allMax - allMin).toFloat()
        val averageDistance = (allMax + allMin) / 2f
        for (d in simpleDataArray) {
            d.maxOffsetPercent = (d.tmp_max - averageDistance) / allDistance
            d.minOffsetPercent = (d.tmp_min - averageDistance) / allDistance
        }

        percent = 0f
        initData()
        invalidate()
    }

    fun resetAnimation() {
        percent = 0f
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

}
