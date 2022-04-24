package com.tokyonth.weather.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.view.animation.LinearInterpolator
import android.graphics.*
import android.util.AttributeSet
import android.view.View

import com.tokyonth.weather.R
import com.tokyonth.weather.utils.ktx.dp2px

import kotlin.math.*

/**
 * Created by wyg on 2017/8/1.
 * Modify by Tokyonth on 2022/3/11
 */
class WindmillView : View {

    private var mWindmillPaint //支柱画笔
            : Paint? = null
    private var mWidth = 0
    private var mHeight = 0
    private var mWindmillColor //风车颜色
            = 0
    private var mWindLengthPercent //扇叶长度
            = 0f
    private var mCenterPoint //圆心
            : Point? = null
    private var x1 = 0f
    private var y1 = 0f
    private var x2 = 0f
    private var y2 = 0f
    private var x3 = 0f
    private var y3 = 0f
    private var x4 = 0f
    private var y4 = 0f
    private var x5 = 0f
    private var y5 //扇叶的点
            = 0f
    private var rad1 = 0.0
    private var rad2 = 0.0
    private var rad3 = 0.0
    private var rad4 //弧度
            = 0.0
    private var r1 = 0.0
    private var r2 = 0.0
    private var r3 = 0.0
    private var r4 //斜边
            = 0.0
    private var mAnimator //动画
            : ObjectAnimator? = null
    private var angle //旋转弧度
            = 0f
    private var windSpeed = 1.0
    private var mDefaultSize = 0

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

    private fun initView() {
        mCenterPoint = Point()
        mWindmillPaint = Paint().apply {
            style = Paint.Style.FILL
            strokeWidth = 2F
            isAntiAlias = true
            color = mWindmillColor
        }

        mAnimator = ObjectAnimator.ofFloat(
            this, "angle",
            0F, (2 * Math.PI).toFloat()
        ).apply {
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
    }

    private fun initAttrs(attrs: AttributeSet?) {
        mDefaultSize = 120.dp2px().toInt()
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.WindmillView)
            mWindLengthPercent =
                typedArray.getFloat(R.styleable.WindmillView_windLengthParent, 0.33F)
            mWindmillColor =
                typedArray.getColor(R.styleable.WindmillView_windmillColors, Color.WHITE)
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            measureSize(widthMeasureSpec, mDefaultSize),
            measureSize(heightMeasureSpec, mDefaultSize)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //w , h 即为onMeasure计算出来的值
        mWidth = w
        mHeight = h
        mCenterPoint!!.x = (w * mWindLengthPercent).toInt()
        mCenterPoint!!.y = (w * mWindLengthPercent).toInt()
        setBladeLocate()
    }

    private fun setBladeLocate() {
        x1 = mCenterPoint!!.x.toFloat()
        y1 = mCenterPoint!!.y.toFloat()

        //radian(弧度)
        rad1 =
            atan((mWidth / 15f / (mWidth / 30f)).toDouble()) //x1,y1与x2,y2形成的角,以圆点为坐标原点,返回角度为-pi/2 至 pi/2  artan（y/x）
        rad2 = atan((mWidth / 6f / (mWidth / 30f)).toDouble()) //x1,y1 与 x3,y3
        rad3 = Math.PI / 2 //tan90 不存在
        rad4 =
            atan((mCenterPoint!!.y / 2f / (-mWidth / 30f)).toDouble()) + Math.PI //因为返回角度为 -pi/2 至pi,所以加PI

        //r 为斜边长度,与上面要对应
        r1 = hypot((mWidth / 30f).toDouble(), (mWidth / 15f).toDouble())
        r2 = hypot((mWidth / 30f).toDouble(), (mWidth / 6f).toDouble())
        r3 = hypot(0.0, mCenterPoint!!.y.toDouble())
        r4 = hypot((mWidth / 30f).toDouble(), (mCenterPoint!!.y / 2f).toDouble())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        drawWind(canvas)
        drawPillar(canvas)
        canvas.restore()
    }

    private fun drawPillar(canvas: Canvas) {
        val mPillarPath = Path()
        mPillarPath.moveTo(mCenterPoint!!.x - mWidth / 90f, mCenterPoint!!.y - mWidth / 90f)
        mPillarPath.lineTo(mCenterPoint!!.x + mWidth / 90f, mCenterPoint!!.y - mWidth / 90f) //连线
        mPillarPath.lineTo(mCenterPoint!!.x + mWidth / 35f, mHeight - mHeight / 35f)
        mPillarPath.quadTo(
            mCenterPoint!!.x.toFloat(),
            mHeight.toFloat(),
            mCenterPoint!!.x - mWidth / 35f,
            mHeight - mHeight / 35f
        ) //贝塞尔曲线，控制点和终点
        mPillarPath.close() //闭合图形
        canvas.drawPath(mPillarPath, mWindmillPaint!!)
    }

    private fun drawWind(canvas: Canvas) {
        val mWindPath = Path()
        canvas.drawCircle(
            mCenterPoint!!.x.toFloat(),
            mCenterPoint!!.y.toFloat(),
            mWidth / 40f,
            mWindmillPaint!!
        )
        mWindPath.moveTo(x1, y1)
        x2 = mCenterPoint!!.x + (r1 * cos(rad1 + angle)).toFloat()
        y2 = mCenterPoint!!.y + (r1 * sin(rad1 + angle)).toFloat()
        x3 = mCenterPoint!!.x + (r2 * cos(rad2 + angle)).toFloat()
        y3 = mCenterPoint!!.y + (r2 * sin(rad2 + angle)).toFloat()
        x4 = mCenterPoint!!.x + (r3 * cos(rad3 + angle)).toFloat()
        y4 = mCenterPoint!!.y + (r3 * sin(rad3 + angle)).toFloat()
        x5 = mCenterPoint!!.x + (r4 * cos(rad4 + angle)).toFloat()
        y5 = mCenterPoint!!.y + (r4 * sin(rad4 + angle)).toFloat()
        mWindPath.cubicTo(x2, y2, x3, y3, x4, y4)
        mWindPath.quadTo(x5, y5, x1, y1)
        canvas.drawPath(mWindPath, mWindmillPaint!!)
        canvas.rotate(120f, mCenterPoint!!.x.toFloat(), mCenterPoint!!.y.toFloat())
        canvas.drawPath(mWindPath, mWindmillPaint!!)
        canvas.rotate(120f, mCenterPoint!!.x.toFloat(), mCenterPoint!!.y.toFloat())
        canvas.drawPath(mWindPath, mWindmillPaint!!)
        canvas.rotate(120f, mCenterPoint!!.x.toFloat(), mCenterPoint!!.y.toFloat())
    }

    private fun measureSize(measureSpec: Int, defaultSize: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        var result = defaultSize
        if (mode == MeasureSpec.EXACTLY) {
            result = specSize
        } else if (mode == MeasureSpec.AT_MOST) {
            result = min(specSize, defaultSize)
        }
        return result
    }

    fun setWindSpeed(mWindSpeed: Double) {
        this.windSpeed = if (mWindSpeed == 0.0) {
            1.0
        } else {
            mWindSpeed
        }
    }

    fun setAngle(angle: Float) {
        this.angle = angle
        invalidate()
    }

    fun stopAnimation() {
        clearAnimation()
    }

    fun startAnimation() {
        mAnimator!!.duration = (10000 / (windSpeed * 0.80)).toLong() //乘以小于1的系数降低影响
        mAnimator!!.start()
    }

}
