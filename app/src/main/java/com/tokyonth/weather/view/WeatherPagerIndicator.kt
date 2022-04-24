package com.tokyonth.weather.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback

import com.tokyonth.weather.utils.ktx.dp2px

class WeatherPagerIndicator : View {

    // 指示器之间的间距
    private var mIndicatorItemDistance = 0

    //选中与为选中的颜色
    private var mColorSelected = 0
    private var mColorUnSelected = 0

    // 圆点半径大小
    private var circleCircleRadius = 0

    //画笔
    private var mUnSelectedPaint: Paint? = null
    private var mSelectedPaint: Paint? = null

    //指示器item的区域
    private var mIndicatorItemRectF: RectF? = null

    //指示器大小
    private var mIndicatorItemWidth = 0
    private var mIndicatorItemHeight = 0

    //指示器item个数
    private var mIndicatorItemCount = 6

    //当前选中的位置
    private var mCurrentSelectedPosition = 1

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
        intPaint()
        verifyItemCount()
    }

    private fun init() {
        mColorSelected = Color.WHITE
        mColorUnSelected = Color.parseColor("#33FFFFFF")
        mIndicatorItemDistance = 6F.dp2px().toInt()
        circleCircleRadius = 2F.dp2px().toInt()
    }

    private fun intPaint() {
        mUnSelectedPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = mColorUnSelected
        }

        mSelectedPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = mColorSelected
        }

        mIndicatorItemRectF = RectF()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mIndicatorItemWidth = (2 * circleCircleRadius * mIndicatorItemCount
                + (mIndicatorItemCount - 1) * mIndicatorItemDistance)
        mIndicatorItemHeight = 2 * circleCircleRadius
        setMeasuredDimension(mIndicatorItemWidth, mIndicatorItemHeight * 4)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cy = height / 2
        for (i in 0 until mIndicatorItemCount) {
            val cx = (i + 1) * circleCircleRadius + i * mIndicatorItemDistance
            canvas.drawCircle(
                cx.toFloat(), cy.toFloat(), circleCircleRadius.toFloat(),
                (if (i == mCurrentSelectedPosition) mSelectedPaint else mUnSelectedPaint)!!
            )
        }
    }

    fun attachToViewPager2(vp2: ViewPager2) {
        val pagerAdapter = vp2.adapter
        if (pagerAdapter != null) {
            mIndicatorItemCount = pagerAdapter.itemCount
            mCurrentSelectedPosition = vp2.currentItem
            verifyItemCount()
        }
        vp2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (pagerAdapter != null) {
                    mCurrentSelectedPosition = vp2.currentItem
                }
                postInvalidate()
            }
        })
    }

    private fun verifyItemCount() {
        if (mCurrentSelectedPosition >= mIndicatorItemCount) {
            mCurrentSelectedPosition = mIndicatorItemCount - 1
        }
        visibility = if (mIndicatorItemCount <= 1) GONE else VISIBLE
    }

}
