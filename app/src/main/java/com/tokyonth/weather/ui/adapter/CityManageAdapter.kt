package com.tokyonth.weather.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View

import com.tokyonth.weather.R
import com.tokyonth.weather.databinding.ItemCityManageBinding
import com.tokyonth.weather.data.entity.SavedLocationEntity
import com.tokyonth.weather.utils.AssetsZipUtils
import com.tokyonth.weather.utils.ktx.loadSvg
import com.tokyonth.weather.utils.ktx.string

class CityManageAdapter : RecyclerView.Adapter<CityManageAdapter.CityManageViewHolder>() {

    interface OnItemClickListener {

        fun onClick(view: View, position: Int)

        fun onLongClick(view: View, position: Int)

    }

    private var listener: OnItemClickListener? = null

    private var savedLocationList: MutableList<SavedLocationEntity> = ArrayList()

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: MutableList<SavedLocationEntity>) {
        savedLocationList.addAll(list)
        notifyDataSetChanged()
    }

    fun getData(): MutableList<SavedLocationEntity> {
        return savedLocationList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityManageViewHolder {
        val vb = ItemCityManageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityManageViewHolder(vb)
    }

    override fun onBindViewHolder(holder: CityManageViewHolder, position: Int) {
        holder.bind(listener!!, savedLocationList[position])
    }

    override fun getItemCount(): Int {
        return savedLocationList.size
    }

    inner class CityManageViewHolder(private val vb: ItemCityManageBinding) :
        RecyclerView.ViewHolder(vb.root) {

        fun bind(listener: OnItemClickListener, savedLocationEntity: SavedLocationEntity) {
            setWeatherInfo(savedLocationEntity)
            vb.tvCityItemName.text = savedLocationEntity.locationName
            if (savedLocationEntity.locationId.isEmpty()) {
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

        private fun setWeatherInfo(savedLocationEntity: SavedLocationEntity) {
            if (savedLocationEntity.img.isNullOrEmpty()) {
                vb.tvCityItemTemp.text = string(R.string.celsius, "-")
                vb.tvAirAndTemp.text = "N/A"
                vb.ivCityItemWeather.setImageResource(R.drawable.ic_nothing)
            } else {
                vb.tvCityItemTemp.text = string(R.string.celsius, savedLocationEntity.temp)
                vb.tvAirAndTemp.text = savedLocationEntity.weather
                vb.ivCityItemWeather.loadSvg(AssetsZipUtils.getFilePathByName(savedLocationEntity.img!!))

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
