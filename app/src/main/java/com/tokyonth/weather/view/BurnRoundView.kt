package com.tokyonth.weather.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

import com.tokyonth.weather.R
import com.tokyonth.weather.utils.BitmapUtils

import kotlin.math.min

class BurnRoundView : View {

    companion object {

        private const val DEFAULT_SIZE = 72

    }

    private var mWidth = 0
    private var mHeight = 0
    private var burnColor = 0

    private var isBurn = false
    private var burnSrc: Bitmap? = null
    private var burnRes: Int = R.mipmap.ic_launcher_round

    private var mPaint: Paint? = null
    private var overlayPaint: Paint? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (attrs != null) {
            initAttr(attrs)
        }
        initView()
    }

    private fun initView() {
        overlayPaint = Paint()
        mPaint = Paint()
        setBurnSrc(burnRes, burnColor, isBurn)
    }

    private fun initAttr(attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.BurnRoundView)
        burnRes = array.getResourceId(R.styleable.BurnRoundView_burnSrc, R.mipmap.ic_launcher_round)
        burnColor = array.getColor(R.styleable.BurnRoundView_burnColor, Color.RED)
        isBurn = array.getBoolean(R.styleable.BurnRoundView_burnOpen, true)
        array.recycle()
    }

    private fun replaceImageColor(imageId: Int, color: Int) {
        burnSrc = BitmapUtils.getBitmapFromDrawable(imageId)
        overlayPaint!!.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    private fun burnColor(color: Int, isBurn: Boolean) {
        burnColor = if (isBurn) {
            color and 0x20FFFFFF
        } else {
            color
        }
    }

    fun setBurnSrc(burnSrc: Int, color: Int, isBurn: Boolean) {
        replaceImageColor(burnSrc, color)
        burnColor(color, isBurn)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //设置画笔的样式，空心STROKE
        mPaint!!.style = Paint.Style.FILL
        //设置抗锯齿
        mPaint!!.isAntiAlias = true
        mPaint!!.color = burnColor
        //width >> 1 与 height >> 1为圆心位置
        canvas.drawBitmap(
            burnSrc!!, (width shr 1) - (burnSrc!!.width shr 1).toFloat(),
            (height shr 1) - (burnSrc!!.height shr 1).toFloat(), overlayPaint
        )
        canvas.drawCircle(
            (width shr 1.toFloat().toInt()).toFloat(),
            (height shr 1.toFloat().toInt()).toFloat(), (measuredWidth / 2.5).toFloat(), mPaint!!
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            measureWidthAndHeight(widthMeasureSpec),
            measureWidthAndHeight(heightMeasureSpec)
        )
        mWidth = measureWidthAndHeight(widthMeasureSpec)
        mHeight = measureWidthAndHeight(heightMeasureSpec)
    }

    private fun measureWidthAndHeight(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = DEFAULT_SIZE
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }

}
