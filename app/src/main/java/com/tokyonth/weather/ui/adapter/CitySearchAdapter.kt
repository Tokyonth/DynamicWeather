package com.tokyonth.weather.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater

import com.tokyonth.weather.data.entity.LocationEntity
import com.tokyonth.weather.databinding.ItemSearchCityBinding

class CitySearchAdapter(private val locationEntityList: List<LocationEntity>) :
    RecyclerView.Adapter<CitySearchAdapter.CitySearchViewHolder>() {

    private var onItemClick: ((LocationEntity) -> Unit)? = null

    fun setOnItemClickListener(onItemClick: (LocationEntity) -> Unit) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitySearchViewHolder {
        val vb = ItemSearchCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitySearchViewHolder(vb)
    }

    override fun onBindViewHolder(holder: CitySearchViewHolder, position: Int) {
        holder.bind(onItemClick!!, locationEntityList[position])
    }

    override fun getItemCount(): Int {
        return locationEntityList.size
    }

    inner class CitySearchViewHolder(private val vb: ItemSearchCityBinding) :
        RecyclerView.ViewHolder(vb.root) {

        fun bind(onItemClick: (LocationEntity) -> Unit, locationEntity: LocationEntity) {
            val result = "${locationEntity.countryNameZH}, " + "${locationEntity.adm1NameZH}, " +
                    "${locationEntity.adm2NameZN}, ${locationEntity.locationNameZH}"
            vb.tvItemSearchCity.text = result
            vb.root.setOnClickListener {
                onItemClick.invoke(locationEntity)
            }
        }

    }

}
