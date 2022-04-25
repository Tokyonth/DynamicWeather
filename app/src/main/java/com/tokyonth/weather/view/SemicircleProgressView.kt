package com.tokyonth.weather.view

import android.text.TextUtils
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator

import com.tokyonth.weather.R
import com.tokyonth.weather.utils.ktx.color
import com.tokyonth.weather.utils.ktx.dp2px
import com.tokyonth.weather.utils.ktx.sp2px

import kotlin.math.min

class SemicircleProgressView : View {

    companion object {
        //  圆环起始角度
        private const val mStartAngle = 125f

        // 圆环结束角度
        private const val mEndAngle = 290f
    }

    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs)
    }

    var semicircleTitleColor = 0

    //外层圆环画笔
    private var mMiddleArcPaint: Paint? = null

    //title文本画笔
    private var mTextPaint: Paint? = null

    //subtitle文本画笔
    private var mTextPaint2: Paint? = null

    //进度圆环画笔
    private var mArcProgressPaint: Paint? = null

    //半径
    private var radius = 0

    //外层矩形
    private var mMiddleRect: RectF? = null

    //进度矩形
    private var mMiddleProgressRect: RectF? = null

    // 最小数字
    private var mMinNum = 0

    // 最大数字
    private var mMaxNum = 40

    // 当前进度
    private var mCurrentAngle = 0f

    //总进度
    private var mTotalAngle = 290f

    //等级
    private var sesameLevel = ""

    //标题
    var title: String? = ""

    //副标题
    var subTile: String? = ""

    //当前点的实际位置
    private lateinit var pos: FloatArray

    //当前点的tangent值
    private lateinit var tan: FloatArray

    //矩阵
    private var mMatrix: Matrix? = null
    private var semicircleSize = 0
    var frontLineColor = 0
    private var maxWidth = 0
    private var maxHeight = 0
    private var semicircleTitleSize = 0
    private var semicircleSubTitleSize = 0

    /**
     * 初始化
     */
    private fun initView(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SemicircleProgressView)
        semicircleSize = typedArray.getDimensionPixelSize(
            R.styleable.SemicircleProgressView_semicircleSize,
            100.dp2px().toInt()
        )
        val semicircleLineSize = typedArray.getDimensionPixelSize(
            R.styleable.SemicircleProgressView_semicircleLineSize,
            3.dp2px().toInt()
        )
        val backgroundLineColor = typedArray.getColor(
            R.styleable.SemicircleProgressView_semicircleBackgroundLineColor,
            color(android.R.color.darker_gray)
        )
        frontLineColor = typedArray.getColor(
            R.styleable.SemicircleProgressView_semicircleFrontLineColor,
            color(android.R.color.holo_orange_dark)
        )
        val titleColor = typedArray.getColor(
            R.styleable.SemicircleProgressView_semicircleTitleColor,
            color(android.R.color.holo_orange_dark)
        )
        val subtitleColor = typedArray.getColor(
            R.styleable.SemicircleProgressView_semicircleSubtitleColor,
            color(android.R.color.darker_gray)
        )
        semicircleTitleSize = typedArray.getDimensionPixelSize(
            R.styleable.SemicircleProgressView_semicircleTitleSize,
            20.sp2px().toInt()
        )
        semicircleSubTitleSize = typedArray.getDimensionPixelSize(
            R.styleable.SemicircleProgressView_semicircleSubtitleSize,
            17.sp2px().toInt()
        )
        title = typedArray.getString(R.styleable.SemicircleProgressView_semicircleTitleText)
        subTile = typedArray.getString(R.styleable.SemicircleProgressView_semicircleSubtitleText)
        if (TextUtils.isEmpty(title)) {
            title = ""
        }
        if (TextUtils.isEmpty(subTile)) {
            subTile = ""
        }

        //外层圆环画笔
        mMiddleArcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mMiddleArcPaint!!.strokeWidth = semicircleLineSize.toFloat()
        mMiddleArcPaint!!.color = backgroundLineColor
        mMiddleArcPaint!!.style = Paint.Style.STROKE
        mMiddleArcPaint!!.strokeCap = Paint.Cap.ROUND
        mMiddleArcPaint!!.alpha = 90

        //正中间字体画笔
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.color = titleColor
        mTextPaint!!.textAlign = Paint.Align.CENTER

        //hour字体画笔
        mTextPaint2 = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint2!!.color = subtitleColor
        mTextPaint2!!.textAlign = Paint.Align.CENTER

        //外层进度画笔
        mArcProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mArcProgressPaint!!.strokeWidth = semicircleLineSize.toFloat()
        mArcProgressPaint!!.style = Paint.Style.STROKE
        mArcProgressPaint!!.strokeCap = Paint.Cap.ROUND
        pos = FloatArray(2)
        tan = FloatArray(2)
        mMatrix = Matrix()
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minimumWidth = suggestedMinimumWidth
        val minimumHeight = suggestedMinimumHeight
        val computedWidth = resolveMeasured(widthMeasureSpec, minimumWidth)
        val computedHeight = resolveMeasured(heightMeasureSpec, minimumHeight)
        setMeasuredDimension(computedWidth, computedHeight)
    }

    private fun resolveMeasured(measureSpec: Int, desired: Int): Int {
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (MeasureSpec.getMode(measureSpec)) {
            MeasureSpec.UNSPECIFIED -> desired
            MeasureSpec.AT_MOST -> min(specSize, desired)
            MeasureSpec.EXACTLY -> specSize
            else -> specSize
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        maxWidth = w
        maxHeight = h
        // view宽度
        val width = semicircleSize
        radius = width / 2
        mMiddleRect = RectF(
            ((maxWidth shr 1) - radius).toFloat(),
            ((maxHeight shr 1) - radius).toFloat(),
            ((maxWidth shr 1)
                    + radius).toFloat(),
            ((maxHeight shr 1) + radius).toFloat()
        )
        mMiddleProgressRect = RectF(
            ((maxWidth shr 1) - radius).toFloat(),
            ((maxHeight shr 1) - radius).toFloat(),
            ((maxWidth shr 1)
                    + radius).toFloat(),
            ((maxHeight shr 1) + radius).toFloat()
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawMiddleArc(canvas)
        drawCenterText(canvas)
        drawRingProgress(canvas)
    }

    /**
     * 绘制外层圆环进度和小圆点
     */
    private fun drawRingProgress(canvas: Canvas) {
        val path = Path()
        path.addArc(mMiddleProgressRect!!, mStartAngle, mCurrentAngle)
        val pathMeasure = PathMeasure(path, false)
        pathMeasure.getPosTan(pathMeasure.length * 1, pos, tan)
        matrix!!.reset()
        //matrix.postTranslate(pos[0] - bitmap.getWidth() / 2, pos[1] - bitmap.getHeight() / 2);
        canvas.drawPath(path, mArcProgressPaint!!)
    }

    /**
     * 绘制中间文本
     */
    private fun drawCenterText(canvas: Canvas) {
        //绘制Title
        mTextPaint!!.textSize = semicircleTitleSize.toFloat()
        mTextPaint!!.color = semicircleTitleColor
        canvas.drawText(
            title!!,
            (maxWidth shr 1).toFloat(),
            (maxHeight shr 1) - 10.dp2px(),
            mTextPaint!!
        )
        //绘制SubTile
        mTextPaint2!!.textSize = semicircleSubTitleSize.toFloat()
        canvas.drawText(
            subTile!!,
            (maxWidth shr 1).toFloat(),
            (maxHeight shr 1) + 10.dp2px(),
            mTextPaint2!!
        )
        canvas.drawText(
            sesameLevel,
            (maxWidth shr 1).toFloat(),
            (radius + (maxHeight shr 1)).toFloat(),
            mTextPaint2!!
        )
    }

    /**
     * 绘制外层圆环
     */
    private fun drawMiddleArc(canvas: Canvas) {
        mArcProgressPaint!!.color = frontLineColor
        canvas.drawArc(mMiddleRect!!, mStartAngle, mEndAngle, false, mMiddleArcPaint!!)
    }

    fun setSesameValues(values: Int, total: Int) {
        if (values >= 0) {
            mMaxNum = values
            //  mTotalAngle = 290f;
            sesameLevel = "$values/$total"
            // sesameLevel = values + "";
            mTotalAngle = values.toFloat() / total.toFloat() * 290f
            startAnim()
        }
    }

    private fun startAnim() {
        val mAngleAnim = ValueAnimator.ofFloat(mCurrentAngle, mTotalAngle)
        mAngleAnim.interpolator = AccelerateDecelerateInterpolator()
        mAngleAnim.duration = 2000
        mAngleAnim.addUpdateListener { valueAnimator: ValueAnimator ->
            mCurrentAngle = valueAnimator.animatedValue as Float
            postInvalidate()
        }
        mAngleAnim.start()
        // mMinNum = 350;
        val mNumAnim = ValueAnimator.ofInt(mMinNum, mMaxNum)
        mNumAnim.duration = 2000
        mNumAnim.interpolator = LinearInterpolator()
        mNumAnim.addUpdateListener { valueAnimator: ValueAnimator ->
            mMinNum = valueAnimator.animatedValue as Int
            postInvalidate()
        }
        mNumAnim.start()
    }

}
