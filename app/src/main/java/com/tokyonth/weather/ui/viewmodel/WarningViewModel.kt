package com.tokyonth.weather.ui.viewmodel

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.MutableLiveData

import com.tokyonth.weather.base.BaseViewModel
import com.tokyonth.weather.network.HttpUtils

import org.jsoup.Jsoup

class WarningViewModel(application: Application) : BaseViewModel(application) {

    val warningInfoLiveData = MutableLiveData<Triple<Int, Int, String>>()

    fun getWarningContent(position: Int, url: String) {
        HttpUtils.doGetAsynchronous(url) { result: String? ->
            if (result == null) {
                return@doGetAsynchronous
            }
            val document = Jsoup.parse(result)
            val textElement = document.getElementById("text")
            val writingElement = textElement.getElementsByClass("writing").first()
            val ps = writingElement.getElementsByTag("p")
            val title = ps[0].text()
            val content = ps[1].text()
            val index = title.lastIndexOf("色")
            val textColor = when (title.substring(index - 1, index)) {
                "蓝" -> Color.parseColor("#2962FF")
                "黄" -> Color.parseColor("#FFD600")
                "橙" -> Color.parseColor("#FF6D00")
                "红" -> Color.parseColor("#D50000")
                else -> Color.BLACK
            }
            warningInfoLiveData.value = Triple(position, textColor, content)
        }
    }

}
