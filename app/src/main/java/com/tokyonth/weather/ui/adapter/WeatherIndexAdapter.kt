package com.tokyonth.weather.ui.adapter

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView

import com.tokyonth.weather.data.WeatherHelper
import com.tokyonth.weather.databinding.ItemIndexWeatherBinding
import com.tokyonth.weather.data.hf.WeatherLife

class WeatherIndexAdapter(private val indexList: List<WeatherLife.DailyItemLife>) :
    RecyclerView.Adapter<WeatherIndexAdapter.WeatherIndexViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherIndexViewHolder {
        val vb = ItemIndexWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherIndexViewHolder(vb)
    }

    override fun onBindViewHolder(holder: WeatherIndexViewHolder, position: Int) {
        holder.bind(indexList[position])
    }

    override fun getItemCount(): Int {
        return indexList.size
    }

    inner class WeatherIndexViewHolder(private val vb: ItemIndexWeatherBinding) :
        RecyclerView.ViewHolder(vb.root) {

        fun bind(dailyItemLife: WeatherLife.DailyItemLife) {
            val icon = WeatherHelper.getLifeIndexIcon(dailyItemLife.name)
            vb.ivItemIndex.setImageResource(icon)
            vb.tvItemIndexValue.text = dailyItemLife.category
            vb.tvItemIndexTitle.text = dailyItemLife.name
            vb.tvItemIndexDetail.text = dailyItemLife.text
        }

    }

}
