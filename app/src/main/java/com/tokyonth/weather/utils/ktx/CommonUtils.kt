package com.tokyonth.weather.utils.ktx

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar

import com.tokyonth.weather.App
import com.tokyonth.weather.base.BaseActivity
import com.tokyonth.weather.base.BaseFragment

object CommonUtils {

    fun getScreenHeight(): Int {
        val metrics = App.context.resources.displayMetrics
        return metrics.heightPixels
    }

    fun getScreenWidth(): Int {
        val metrics = App.context.resources.displayMetrics
        return metrics.widthPixels
    }

}

inline fun <T : View> T.visibleOrGone(boolean: Boolean, onVisible: (T.() -> Unit) = {}) {
    visibility = if (boolean) {
        View.VISIBLE
    } else {
        View.GONE
    }
    if (boolean) {
        onVisible.invoke(this)
    }
}

fun color(colorId: Int): Int {
    return ResourcesCompat.getColor(App.context.resources, colorId, null)
}

fun string(stringId: Int, vararg args: Any?): String {
    return App.context.getString(stringId, *args)
}

fun toast(text: String) {
    Toast.makeText(App.context, text, Toast.LENGTH_SHORT).show()
}

fun BaseActivity.snack(text: String) {
    val rootView = this.window.decorView.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    Snackbar.make(
        rootView,
        text,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun BaseFragment.snack(text: String) {
    val rootView = this.requireView()
    Snackbar.make(
        rootView,
        text,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun Float.sp2px(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this,
        Resources.getSystem().displayMetrics
    )
}

fun Float.dp2px(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this,
        Resources.getSystem().displayMetrics
    )
}

fun Int.sp2px(): Float {
    return this.toFloat().sp2px()
}

fun Int.dp2px(): Float {
    return this.toFloat().dp2px()
}
