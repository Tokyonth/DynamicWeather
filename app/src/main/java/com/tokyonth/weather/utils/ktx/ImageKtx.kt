package com.tokyonth.weather.utils.ktx

import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load

fun ImageView.loadSvg(path: String) {
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    this.load(path, imageLoader)
}
