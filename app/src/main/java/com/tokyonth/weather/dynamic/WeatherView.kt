package com.tokyonth.weather.dynamic

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

import com.tokyonth.weather.dynamic.data.DynamicDrawerType
import com.tokyonth.weather.dynamic.data.FpsType
import com.tokyonth.weather.dynamic.data.SkyBackground
import com.tokyonth.weather.dynamic.drawer.CloudyDrawer

class WeatherView(context: Context, attributeSet: AttributeSet?, defaultStyle: Int) :
    SurfaceView(context, attributeSet, defaultStyle), SurfaceHolder.Callback {

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context) : this(context, null, 0)

    // 低级并发
    private val lock = Object()
    private var canvas: Canvas? = null

    private var mWidth = 0
    private var mHeight = 0

    private var sensorX = 0F
    private var sensorY = 0F
    private var sensorZ = 0F

    @Volatile
    private var canRun = false

    @Volatile
    private var threadQuit = false

    private var fpsType = FpsType.AUTO

    private var baseDrawer: BaseDrawer<*> = CloudyDrawer(false)

    private var thread = Thread {
        while (!threadQuit) {
            if (!canRun) {
                synchronized(lock) {
                    try {
                        lock.wait()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            try {
                val startTime = System.currentTimeMillis()
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    canvas!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                    drawWeather(canvas!!)
                }
                holder.unlockCanvasAndPost(canvas)
                val sleep = when (fpsType) {
                    FpsType.AUTO -> {
                        val drawTime = System.currentTimeMillis() - startTime
                        if (drawTime < 16) {
                            16 - drawTime
                        } else {
                            0
                        }
                    }
                    FpsType.FPS60 -> {
                        16
                    }
                    FpsType.FPS120 -> {
                        8
                    }
                }
                Thread.sleep(sleep)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    init {
        holder.addCallback(this)
        holder.setFormat(PixelFormat.RGBA_8888)
        thread.start()
    }

    fun setDrawerType(type: DynamicDrawerType) {
        baseDrawer = SkyBackground.makeDrawerByType(type)
    }

    fun setFpsType(type: FpsType) {
        this.fpsType = type
    }

    fun setSensorData(vararg axis: Float) {
        sensorX = axis[0]
        sensorY = axis[1]
        sensorZ = axis[2]
    }

    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        holder.removeCallback(this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.mWidth = w
        this.mHeight = h
    }

    private fun drawWeather(canvas: Canvas) {
        baseDrawer.setSize(mWidth, mHeight)
        baseDrawer.startDraw(canvas, sensorX, 1F)
    }

    private fun blockNotify() {
        try {
            // 如果没有执行wait的话，这里notify会抛异常
            synchronized(lock) {
                lock.notify()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onResume() {
        blockNotify()
        threadQuit = false
        canRun = true
    }

    fun onPause() {
        blockNotify()
        threadQuit = false
        canRun = false
    }

    fun onDestroy() {
        blockNotify()
        threadQuit = true
        canRun = false
    }

}
