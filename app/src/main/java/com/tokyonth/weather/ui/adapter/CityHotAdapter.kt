package com.tokyonth.weather.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokyonth.weather.data.entity.LocationEntity
import com.tokyonth.weather.data.db.DbManager
import com.tokyonth.weather.databinding.ItemHotCityBinding

class CityHotAdapter : RecyclerView.Adapter<CityHotAdapter.HotCityViewHolder>() {

    private val hotCityList: MutableList<String> = ArrayList()

    private var hotCityClick: ((LocationEntity) -> Unit)? = null

    init {
        hotCityList.add("定位中")
        hotCityList.add("北京")
        hotCityList.add("上海")
        hotCityList.add("深圳")
        hotCityList.add("广州")
        hotCityList.add("武汉")
        hotCityList.add("长沙")
        hotCityList.add("南京")
        hotCityList.add("苏州")
        hotCityList.add("西安")
        hotCityList.add("济南")
        hotCityList.add("沈阳")
        hotCityList.add("重庆")
        hotCityList.add("郑州")
        hotCityList.add("成都")
        hotCityList.add("杭州")
    }

    fun setItemClickListener(hotCityClick: (LocationEntity) -> Unit) {
        this.hotCityClick = hotCityClick
    }

    fun setFirstCity(name: String) {
        hotCityList[0] = name
        notifyItemChanged(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotCityViewHolder {
        val vb = ItemHotCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotCityViewHolder(vb)
    }

    override fun onBindViewHolder(holder: HotCityViewHolder, position: Int) {
        holder.bind(hotCityClick!!, composeCity(hotCityList[position]))
    }

    override fun getItemCount(): Int {
        return hotCityList.size
    }

    private fun composeCity(name: String): LocationEntity {
        return if (name == hotCityList[0]) {
            LocationEntity(
                0, "", "",
                name, "", "",
                "", "",
                "", "",
                "", "", "",
                "", ""
            )
        } else {
            DbManager.db.queryCityByName(name)!!
        }
    }

    class HotCityViewHolder(private val vb: ItemHotCityBinding) : RecyclerView.ViewHolder(vb.root) {

        fun bind(
            hotCityClick: (LocationEntity) -> Unit,
            locationEntity: LocationEntity
        ) {
            if (bindingAdapterPosition == 0) {
                vb.itemHotCityLocation.visibility = View.VISIBLE
            }
            vb.itemHotCityName.text = locationEntity.locationNameZH
            vb.root.setOnClickListener {
                //if (bindingAdapterPosition != 0) {
                hotCityClick.invoke(locationEntity)
                // }
            }
        }

    }

}
