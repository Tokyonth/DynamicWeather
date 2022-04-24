package com.tokyonth.weather.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.tokyonth.weather.R

import com.tokyonth.weather.data.WeatherHelper
import com.tokyonth.weather.data.hf.Weather24Hour
import com.tokyonth.weather.databinding.ItemWeatherTrendBinding
import com.tokyonth.weather.utils.DateUtils
import com.tokyonth.weather.utils.ktx.string

class WeatherTrendAdapter(private val hourlyItems: List<Weather24Hour.HourlyItem>) :
    RecyclerView.Adapter<WeatherTrendAdapter.WeatherTrendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherTrendViewHolder {
        val vb =
            ItemWeatherTrendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherTrendViewHolder(vb)
    }

    override fun onBindViewHolder(holder: WeatherTrendViewHolder, position: Int) {
        holder.bind(hourlyItems[position])
    }

    override fun getItemCount(): Int {
        return hourlyItems.size
    }

    class WeatherTrendViewHolder(private val vb: ItemWeatherTrendBinding) :
        RecyclerView.ViewHolder(vb.root) {

        @SuppressLint("SetTextI18n")
        fun bind(hourlyItem: Weather24Hour.HourlyItem) {
            val optTime = DateUtils.paresTime(hourlyItem.fxTime)
            vb.tvItemTrendWeather.text = hourlyItem.text
            vb.tvItemTrendTime.text = optTime
            vb.tvItemTrendTemp.text = hourlyItem.temp + string(R.string.celsius)
            vb.ivItemTrendInfo.setImageResource(WeatherHelper.getIconResId(hourlyItem.text))
        }

    }

}
