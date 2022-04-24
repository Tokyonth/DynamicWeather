package com.tokyonth.weather.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView

class AutoFullLayout : LinearLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount > 0) {
            val parent = parent
            if (parent != null && parent is NestedScrollView) {
                val height = parent.measuredHeight
                if (height > 0) {
                    val firstChild = getChildAt(0)
                    val layoutParams: LayoutParams =
                        firstChild.layoutParams as LayoutParams
                    layoutParams.height = height
                    firstChild.layoutParams = layoutParams
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}
