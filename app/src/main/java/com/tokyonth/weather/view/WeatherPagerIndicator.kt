package com.tokyonth.weather.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback

class WeatherPagerIndicator : View {

    // 指示器之间的间距
    private var mIndicatorItemDistance = 20F

    //选中与为选中的颜色
    private var mColorSelected = Color.WHITE
    private var mColorUnSelected = Color.parseColor("#33FFFFFF")

    // 圆点半径大小
    private var circleCircleRadius = 8F

    //指示器大小
    //  private var mIndicatorItemWidth = 0
    private var mIndicatorItemHeight = 0

    //指示器item个数
    private var mIndicatorItemCount = 6

    //当前选中的位置
    private var mCurrentSelectedPosition = 1

    private var mIndicatorPaint = Paint()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        mIndicatorPaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = mColorUnSelected
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        /*mIndicatorItemWidth = (2 * circleCircleRadius * mIndicatorItemCount
                + (mIndicatorItemCount - 1) * mIndicatorItemDistance)*/
        mIndicatorItemHeight = (2 * circleCircleRadius).toInt()
        val mWidth = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(mWidth, mIndicatorItemHeight * 4)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cy = (height / 2).toFloat()
        for (i in 0 until mIndicatorItemCount) {
            val cx = (i + 1) * circleCircleRadius + i * mIndicatorItemDistance
            val color = (if (i == mCurrentSelectedPosition) mColorSelected else mColorUnSelected)
            mIndicatorPaint.color = color
            canvas.drawCircle(cx, cy, circleCircleRadius, mIndicatorPaint)
        }
    }

    fun attachToViewPager2(vp2: ViewPager2) {
        vp2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mCurrentSelectedPosition = position
                postInvalidate()
            }
        })
    }

    fun setCount(count: Int) {
        mIndicatorItemCount = count
        postInvalidate()
    }

}
