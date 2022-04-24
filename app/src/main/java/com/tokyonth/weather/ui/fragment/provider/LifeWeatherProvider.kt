package com.tokyonth.weather.ui.fragment.provider

import androidx.recyclerview.widget.LinearLayoutManager
import com.tokyonth.weather.data.hf.WeatherLife
import com.tokyonth.weather.databinding.FragmentWeatherBinding
import com.tokyonth.weather.ui.adapter.WeatherIndexAdapter

/**
 * 生活指数
 */
class LifeWeatherProvider : BaseWeatherProvider<WeatherLife, FragmentWeatherBinding>() {

    override fun fillView() {
        binding.weatherIndexRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = WeatherIndexAdapter(getData().daily)
        }
    }

}
