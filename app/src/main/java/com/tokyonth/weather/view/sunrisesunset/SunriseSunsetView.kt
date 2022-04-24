package com.tokyonth.weather.view.sunrisesunset

import com.tokyonth.weather.utils.BitmapUtils.getBitmapFromDrawable
import com.tokyonth.weather.view.sunrisesunset.SunriseSunsetView
import android.text.TextPaint
import com.tokyonth.weather.view.sunrisesunset.formatter.SunriseSunsetLabelFormatter
import com.tokyonth.weather.view.sunrisesunset.formatter.SimpleSunriseSunsetLabelFormatter
import kotlin.jvm.JvmOverloads
import android.content.res.TypedArray
import com.tokyonth.weather.R
import android.view.View.MeasureSpec
import com.tokyonth.weather.utils.BitmapUtils
import android.graphics.Paint.FontMetricsInt
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.tokyonth.weather.view.sunrisesunset.model.Time
import java.lang.RuntimeException
import java.util.*

class SunriseSunsetView : View {

    companion object {
        private const val DEFAULT_TRACK_COLOR = Color.WHITE
        private const val DEFAULT_TRACK_WIDTH_PX = 4
        private const val DEFAULT_SUN_RADIUS_PX = 20
        private const val DEFAULT_LABEL_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_LABEL_TEXT_SIZE = 40
        private const val DEFAULT_LABEL_VERTICAL_OFFSET_PX = 5
        private const val DEFAULT_LABEL_HORIZONTAL_OFFSET_PX = 20

        /**
         * 当前日出日落比率, mRatio < 0: 未日出, mRatio > 1 已日落
         */
        private const val MINIMAL_TRACK_RADIUS_PX = 300 // 半圆轨迹最小半径
    }

    private var mTrackRadius // 轨迹圆的半径
            = 0f
    private var mRatio = 0f
    private var mTrackColor = DEFAULT_TRACK_COLOR // 轨迹的颜色
    private var mTrackWidth = DEFAULT_TRACK_WIDTH_PX // 轨迹的宽度
    var sunRadius = DEFAULT_SUN_RADIUS_PX // 太阳半径
        .toFloat()
    private var mLabelTextSize = DEFAULT_LABEL_TEXT_SIZE // 标签文字大小
    private var mLabelTextColor = DEFAULT_LABEL_TEXT_COLOR // 标签颜色
    private var mLabelVerticalOffset = DEFAULT_LABEL_VERTICAL_OFFSET_PX // 竖直方向间距
    private var mLabelHorizontalOffset = DEFAULT_LABEL_HORIZONTAL_OFFSET_PX // 水平方向间距
    private var mTrackPaint // 绘制半圆轨迹的Paint
            : Paint? = null
    private var mShadowPaint // 绘制日出日落阴影的Paint
            : Paint? = null
    private var mLine //绘制底部直线
            : Paint? = null
    private var mLabelPaint // 绘制日出日落时间的Paint
            : TextPaint? = null

    /**
     * 日出时间
     */
    private var sunriseTime: Time? = null

    /**
     * 日落时间
     */
    private var sunsetTime: Time? = null

    fun setSsvTime(ssvTime: Pair<String, String>) {
        val sunrise = ssvTime.first.split(":")
        sunriseTime = Time(sunrise[0].toInt(), sunrise[1].toInt())
        val sunset = ssvTime.second.split(":")
        sunsetTime = Time(sunset[0].toInt(), sunset[1].toInt())
    }

    /**
     * 绘图区域
     */
    private val mBoardRectF = RectF()

    var labelFormatter: SunriseSunsetLabelFormatter = SimpleSunriseSunsetLabelFormatter()

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(attrs)
        initView()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val paddingRight = paddingRight
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)

        // 处理wrap_content这种情况
        if (widthSpecMode == MeasureSpec.AT_MOST) {
            widthSpecSize =
                paddingLeft + paddingRight + MINIMAL_TRACK_RADIUS_PX * 2 + sunRadius.toInt() * 2
        }
        mTrackRadius = (widthSpecSize - paddingLeft - paddingRight - 2 * sunRadius) / 2
        val expectedHeight = (mTrackRadius + sunRadius + paddingBottom + paddingTop).toInt()
        mBoardRectF[paddingLeft + sunRadius, paddingTop + sunRadius, widthSpecSize - paddingRight - sunRadius] =
            (expectedHeight - paddingBottom).toFloat()
        setMeasuredDimension(widthSpecSize, expectedHeight)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.SunriseSunsetView)
        mTrackColor = a.getColor(R.styleable.SunriseSunsetView_ssvTrackColor, DEFAULT_TRACK_COLOR)
        mTrackWidth = a.getDimensionPixelSize(
            R.styleable.SunriseSunsetView_ssvTrackWidth,
            DEFAULT_TRACK_WIDTH_PX
        )
        sunRadius = a.getDimensionPixelSize(
            R.styleable.SunriseSunsetView_ssvSunRadius,
            DEFAULT_SUN_RADIUS_PX
        ).toFloat()
        mLabelTextColor =
            a.getColor(R.styleable.SunriseSunsetView_ssvLabelTextColor, DEFAULT_LABEL_TEXT_COLOR)
        mLabelTextSize = a.getDimensionPixelSize(
            R.styleable.SunriseSunsetView_ssvLabelTextSize,
            DEFAULT_LABEL_TEXT_SIZE
        )
        mLabelVerticalOffset = a.getDimensionPixelOffset(
            R.styleable.SunriseSunsetView_ssvLabelVerticalOffset,
            DEFAULT_LABEL_VERTICAL_OFFSET_PX
        )
        mLabelHorizontalOffset = a.getDimensionPixelOffset(
            R.styleable.SunriseSunsetView_ssvLabelHorizontalOffset,
            DEFAULT_LABEL_HORIZONTAL_OFFSET_PX
        )
        a.recycle()
    }

    private fun initView() {
        // 初始化半圆轨迹的画笔
        mTrackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTrackPaint!!.style = Paint.Style.STROKE // 画笔的样式为线条
        prepareTrackPaint()

        // 初始化日出日落阴影的画笔
        mShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mShadowPaint!!.style = Paint.Style.FILL_AND_STROKE
        mLabelPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        prepareLabelPaint()

        // 初始化日出日落阴影的画笔
        mLine = Paint(Paint.ANTI_ALIAS_FLAG)
        mLine!!.style = Paint.Style.FILL_AND_STROKE
        prepareLinePaint()
    }

    // 半圆轨迹的画笔
    private fun prepareTrackPaint() {
        mTrackPaint!!.color = mTrackColor
        mTrackPaint!!.strokeWidth = mTrackWidth.toFloat()
    }

    // 底部线条的画笔
    private fun prepareLinePaint() {
        mLine!!.color = Color.WHITE
        mLine!!.strokeWidth = mTrackWidth.toFloat()
    }

    // 标签的画笔
    private fun prepareLabelPaint() {
        mLabelPaint!!.color = mLabelTextColor
        mLabelPaint!!.textSize = mLabelTextSize.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawSunTrack(canvas)
        drawShadow(canvas)
        drawSun(canvas)
        drawSunriseSunsetLabel(canvas)
    }

    // 绘制太阳轨道（半圆）
    private fun drawSunTrack(canvas: Canvas) {
        prepareTrackPaint()
        canvas.save()
        val rectF = RectF(
            mBoardRectF.left,
            mBoardRectF.top,
            mBoardRectF.right,
            mBoardRectF.bottom + mBoardRectF.height()
        )
        canvas.drawArc(rectF, 180f, 180f, false, mTrackPaint!!)
        canvas.restore()
    }

    // 绘制日出日落阴影部分
    private fun drawShadow(canvas: Canvas) {
        canvas.save()
        val path = Path()
        val endY = mBoardRectF.bottom
        val rectF = RectF(
            mBoardRectF.left,
            mBoardRectF.top,
            mBoardRectF.right,
            mBoardRectF.bottom + mBoardRectF.height()
        )
        val curPointX = mBoardRectF.left + mTrackRadius - mTrackRadius * Math.cos(Math.PI * mRatio)
            .toFloat()
        path.moveTo(0f, endY)
        path.arcTo(rectF, 180f, 180 * mRatio)
        path.lineTo(curPointX, endY)
        path.close()
        val shader: Shader = LinearGradient(
            200F, 0F, 200F, 400F,
            Color.parseColor("#48EFEFF0"),
            Color.parseColor("#00000000"), Shader.TileMode.CLAMP
        )
        mShadowPaint!!.shader = shader
        canvas.drawPath(path, mShadowPaint!!)
        canvas.restore()
    }

    // 绘制太阳
    private fun drawSun(canvas: Canvas) {
        canvas.save()
        val curPointX = mBoardRectF.left + mTrackRadius - mTrackRadius * Math.cos(Math.PI * mRatio)
            .toFloat()
        val curPointY = mBoardRectF.bottom - mTrackRadius * Math.sin(Math.PI * mRatio)
            .toFloat()
        // canvas.drawCircle(curPointX, curPointY, mSunRadius, mSunPaint);
        var bitmap: Bitmap? = getBitmapFromDrawable(R.drawable.ic_sun)
        bitmap = Bitmap.createScaledBitmap(bitmap!!, 80, 80, true)
        canvas.drawBitmap(bitmap, curPointX - 40, curPointY - 40, null)
        canvas.restore()
    }

    // 绘制日出日落标签
    private fun drawSunriseSunsetLabel(canvas: Canvas) {
        if (sunriseTime == null || sunsetTime == null) {
            return
        }
        prepareLabelPaint()
        canvas.save()
        // 绘制日出时间
        val sunriseStr = labelFormatter.formatSunriseLabel(sunriseTime!!)
        mLabelPaint!!.textAlign = Paint.Align.LEFT
        val metricsInt = mLabelPaint!!.fontMetricsInt
        var baseLineX = mBoardRectF.left + sunRadius + mLabelHorizontalOffset
        val baseLineY = mBoardRectF.bottom - metricsInt.bottom - mLabelVerticalOffset
        var bitmapSunrise = getBitmapFromDrawable(R.drawable.ic_sunrise)
        bitmapSunrise = Bitmap.createScaledBitmap(bitmapSunrise, 40, 40, true)
        canvas.drawBitmap(
            bitmapSunrise,
            baseLineX,
            baseLineY - bitmapSunrise.height.toFloat() / 2,
            null
        )
        canvas.drawText(
            sunriseStr, baseLineX + bitmapSunrise.width * 2, baseLineY + bitmapSunrise.height
                .toFloat() / 2, mLabelPaint!!
        )

        // 绘制日落时间
        mLabelPaint!!.textAlign = Paint.Align.RIGHT
        val sunsetStr = labelFormatter.formatSunsetLabel(sunsetTime!!)
        baseLineX = mBoardRectF.right - sunRadius - mLabelHorizontalOffset
        val rect = Rect()
        mLabelPaint!!.getTextBounds(sunsetStr, 0, sunsetStr.length, rect)
        val sunsetStrWidth = rect.width().toFloat()
        var bitmapSunset = getBitmapFromDrawable(R.drawable.ic_sunset)
        bitmapSunset = Bitmap.createScaledBitmap(bitmapSunset, 40, 40, true)
        canvas.drawBitmap(
            bitmapSunset,
            baseLineX - sunsetStrWidth - bitmapSunset.width * 2,
            baseLineY - bitmapSunset.height
                .toFloat() / 2,
            null
        )
        canvas.drawText(
            sunsetStr,
            baseLineX,
            baseLineY + bitmapSunset.height.toFloat() / 2,
            mLabelPaint!!
        )
        canvas.restore()
    }

    fun setRatio(ratio: Float) {
        mRatio = ratio
        invalidate()
    }

    fun setTrackColor(trackColor: Int) {
        mTrackColor = trackColor
    }

    fun setTrackWidth(trackWidthInPx: Int) {
        mTrackWidth = trackWidthInPx
    }

    fun setLabelTextSize(labelTextSize: Int) {
        mLabelTextSize = labelTextSize
    }

    fun setLabelTextColor(labelTextColor: Int) {
        mLabelTextColor = labelTextColor
    }

    fun setLabelVerticalOffset(labelVerticalOffset: Int) {
        mLabelVerticalOffset = labelVerticalOffset
    }

    fun setLabelHorizontalOffset(labelHorizontalOffset: Int) {
        mLabelHorizontalOffset = labelHorizontalOffset
    }

    fun startAnimate() {
        if (sunriseTime == null || sunsetTime == null) {
            throw RuntimeException("You need to set both sunrise and sunset time before start animation")
        }
        val sunrise = sunriseTime!!.transformToMinutes()
        val sunset = sunsetTime!!.transformToMinutes()
        val calendar = Calendar.getInstance(Locale.getDefault())
        val currentHour = calendar[Calendar.HOUR_OF_DAY]
        val currentMinute = calendar[Calendar.MINUTE]
        val currentTime = currentHour * Time.MINUTES_PER_HOUR + currentMinute
        var ratio = 1.0f * (currentTime - sunrise) / (sunset - sunrise)
        ratio = if (ratio <= 0) 0F else if (ratio > 1.0f) 1F else ratio
        val animator = ObjectAnimator.ofFloat(this, "ratio", 0f, ratio)
        animator.duration = 1500L
        animator.interpolator = LinearInterpolator()
        animator.start()
    }

}
