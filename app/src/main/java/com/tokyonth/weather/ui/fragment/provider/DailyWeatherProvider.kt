package com.tokyonth.weather.ui.fragment.provider

import com.tokyonth.weather.data.hf.Weather7Day
import com.tokyonth.weather.databinding.FragmentWeatherBinding

class DailyWeatherProvider : BaseWeatherProvider<Weather7Day, FragmentWeatherBinding>() {

    override fun fillView() {
        binding.dailyView.setData(getData())

        binding.ssvView.run {
            setSsvTime(getData().daily[0].sunrise, getData().daily[0].sunset)
            startAnimate()
        }
    }

}
