package com.tokyonth.weather.network.interceptor

import com.tokyonth.weather.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AppendParamInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedUrl = originalRequest
            .url
            .newBuilder()
            .addQueryParameter("key", BuildConfig.APP_KEY)
            .build()
        val request = originalRequest
            .newBuilder()
            .url(modifiedUrl)
            .build()
        return chain.proceed(request)
    }

}
