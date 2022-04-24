package com.tokyonth.weather.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

import com.tokyonth.weather.network.interceptor.AppendParamInterceptor
import com.tokyonth.weather.network.interceptor.LogInterceptor

class ApiRepository {

    companion object {

        private const val API_URL = "https://devapi.qweather.com/v7/"

        val api: ApiInterface by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiRepository().getApiGenerator()
        }

    }

    private fun getApiGenerator(): ApiInterface {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
        return setRetrofitBuilder(retrofitBuilder)
            .build()
            .create(ApiInterface::class.java)
    }

    private val okHttpClient: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder().apply {
                addInterceptor(AppendParamInterceptor())
                addInterceptor(LogInterceptor())
                connectTimeout(10, TimeUnit.SECONDS)
                readTimeout(5, TimeUnit.SECONDS)
                writeTimeout(5, TimeUnit.SECONDS)
            }
            return builder.build()
        }

    private fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        }
    }

}
