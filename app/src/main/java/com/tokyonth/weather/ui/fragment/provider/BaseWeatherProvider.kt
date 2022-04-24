package com.tokyonth.weather.ui.fragment.provider

import androidx.viewbinding.ViewBinding

abstract class BaseWeatherProvider<T, B : ViewBinding> {

    abstract fun fillView()

    private var data: T? = null

    protected lateinit var binding: B

    fun attach(
        data: T,
        binding: B
    ) {
        this.data = data
        this.binding = binding
        fillView()
    }

    fun getData(): T {
        if (data == null) {
            throw IllegalStateException("data is null!")
        }
        return data!!
    }

}
