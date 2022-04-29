package com.tokyonth.weather.ui.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.tokyonth.weather.Constants
import com.tokyonth.weather.data.entity.SavedLocationEntity
import com.tokyonth.weather.ui.fragment.WeatherFragment

import java.util.ArrayList

class WeatherPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragmentList: MutableList<Fragment> = ArrayList()

    private var locationList: MutableList<SavedLocationEntity> = ArrayList()

    init {
        locationList.add(
            0, SavedLocationEntity(
                0, "", "", ""
            )
        )
        fragmentList.add(0, WeatherFragment())
    }

    fun setData(dataList: List<SavedLocationEntity>) {
        dataList.forEach {
            locationList.add(it)
            fragmentList.add(WeatherFragment())
        }
    }

    fun addData(savedLocationEntity: SavedLocationEntity) {
        locationList.add(savedLocationEntity)
        fragmentList.add(WeatherFragment())
    }

    fun removeData(position: Int) {
        locationList.removeAt(position)
        fragmentList.removeAt(position)
    }

    override fun createFragment(position: Int): Fragment {
        val isDefault = position == 0
        return fragmentList[position].apply {
            arguments = bundleOf(
                Pair(Constants.INTENT_IS_DEFAULT_LOCATION, isDefault),
                Pair(Constants.INTENT_LOCATION_CODE, locationList[position].locationId),
                Pair(Constants.INTENT_LOCATION_NAME, locationList[position].locationName)
            )
        }
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

}
