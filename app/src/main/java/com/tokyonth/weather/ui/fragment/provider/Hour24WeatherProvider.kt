package com.tokyonth.weather.ui.fragment.provider

import androidx.recyclerview.widget.LinearLayoutManager

import com.tokyonth.weather.data.hf.Weather24Hour
import com.tokyonth.weather.databinding.FragmentWeatherBinding
import com.tokyonth.weather.ui.adapter.WeatherTrendAdapter

/**
 * 24小时天气
 */
class Hour24WeatherProvider : BaseWeatherProvider<Weather24Hour, FragmentWeatherBinding>() {

    override fun fillView() {
        binding.rvWeatherTrend.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = WeatherTrendAdapter(getData().hourly)
        }
    }

}
