package com.tokyonth.weather.network.interceptor

import okhttp3.*
import android.util.Log
import java.nio.charset.Charset
import java.lang.Exception
import java.lang.StringBuilder

import com.tokyonth.weather.Constants

class LogInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val charset = Charset.forName("UTF-8")
        val request = chain.request()
        val response: Response
        try {
            response = chain.proceed(request)
            if (Constants.IS_PRINT_API_LOG) {
                val source = response.body!!.source()
                source.request(Long.MAX_VALUE)
                val buffer = source.buffer
                val logStr = StringBuilder().apply {
                    append("\n{")
                    append("url->")
                    append(request.url)
                    append("\n")
                    append("code->")
                    append(response.code)
                    append("\n")
                    append("method->")
                    append(request.method)
                    append("\n")
                    append("body->")
                    append(buffer.clone().readString(charset))
                    append("\n}")
                }
                Log.e("LogInterceptor", logStr.toString())
            }
        } catch (e: Exception) {
            throw e
        }
        return response
    }

}
