package com.tokyonth.weather.network

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL

import com.tokyonth.weather.utils.ktx.doAsync
import com.tokyonth.weather.utils.ktx.onUI

object HttpUtils {

    private const val TIME_OUT = 8000

    fun doGetAsynchronous(urlStr: String, callBack: (String?) -> Unit) {
        doAsync {
            val result = doGet(urlStr)
            onUI {
                callBack.invoke(result)
            }
        }
    }

    private fun doGet(urlStr: String): String? {
        val url: URL
        var connection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        var byteArrayOutputStream: ByteArrayOutputStream? = null
        try {
            url = URL(urlStr)
            connection = url.openConnection() as HttpURLConnection
            connection.apply {
                readTimeout = TIME_OUT
                connectTimeout = TIME_OUT
                requestMethod = "GET"
                setRequestProperty("accept", "*/*")
                setRequestProperty("connection", "Keep-Alive")
                setRequestProperty(
                    "User-Agent",
                    "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52"
                )
            }

            return if (connection.responseCode == 200) {
                inputStream = connection.inputStream
                byteArrayOutputStream = ByteArrayOutputStream()
                var len: Int
                val buf = ByteArray(128)
                while (inputStream.read(buf).also { len = it } != -1) {
                    byteArrayOutputStream.write(buf, 0, len)
                }
                byteArrayOutputStream.run {
                    flush()
                    toString()
                }
            } else {
                throw RuntimeException("responseCode is not 200")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                byteArrayOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            connection?.disconnect()
        }
        return null
    }

}
