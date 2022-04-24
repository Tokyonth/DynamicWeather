package com.tokyonth.weather.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View

import com.tokyonth.weather.R
import com.tokyonth.weather.databinding.ItemCityManageBinding
import com.tokyonth.weather.data.entity.SavedCityEntity
import com.tokyonth.weather.data.WeatherHelper
import com.tokyonth.weather.utils.ktx.string

class CityManageAdapter : RecyclerView.Adapter<CityManageAdapter.CityManageViewHolder>() {

    interface OnItemClickListener {

        fun onClick(view: View, position: Int)

        fun onLongClick(view: View, position: Int)

    }

    private var listener: OnItemClickListener? = null

    private var savedCityList: MutableList<SavedCityEntity> = ArrayList()

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: MutableList<SavedCityEntity>) {
        savedCityList.addAll(list)
        notifyDataSetChanged()
    }

    fun getData(): MutableList<SavedCityEntity> {
        return savedCityList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityManageViewHolder {
        val vb = ItemCityManageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityManageViewHolder(vb)
    }

    override fun onBindViewHolder(holder: CityManageViewHolder, position: Int) {
        holder.bind(listener!!, savedCityList[position])
    }

    override fun getItemCount(): Int {
        return savedCityList.size
    }

    class CityManageViewHolder(private val vb: ItemCityManageBinding) :
        RecyclerView.ViewHolder(vb.root) {

        fun bind(listener: OnItemClickListener, savedCityEntity: SavedCityEntity) {
            setWeatherInfo(savedCityEntity)
            vb.tvCityItemName.text = savedCityEntity.locationName
            if (savedCityEntity.locationId.isEmpty()) {
                vb.ivCityItemLocal.visibility = View.VISIBLE
            }
            vb.root.setOnClickListener { v: View ->
                listener.onClick(v, absoluteAdapterPosition)
            }
            vb.root.setOnLongClickListener { v: View ->
                listener.onLongClick(v, absoluteAdapterPosition)
                true
            }
        }

        @SuppressLint("SetTextI18n")
        private fun setWeatherInfo(savedCityEntity: SavedCityEntity) {
            if (savedCityEntity.locationName.isEmpty()) {
                vb.tvCityItemWeather.text = "0"
                vb.tvAirAndTemp.text = "N/A"
                vb.ivCityItemWeather.setImageResource(R.drawable.ic_nothing)
            } else {
                vb.tvCityItemWeather.text = savedCityEntity.temp + string(R.string.celsius)
           //     val weatherImagePath = WeatherHelper.getWeatherImagePath(savedCityEntity.img!!)
//                vb.root.background = WeatherHelper.getWeatherBackground(savedCityEntity.img!!)
             //   vb.ivCityItemWeather.setImageResource(weatherImagePath)
               /* vb.tvAirAndTemp.text = ("空气" + savedCityEntity.quality +
                        "\t" + savedCityEntity.lowTemp
                        + "/" + savedCityEntity.highTemp + string(R.string.celsius))*/
            }
        }
    }

}
