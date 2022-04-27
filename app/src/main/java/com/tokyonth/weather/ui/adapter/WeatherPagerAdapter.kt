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

    fun setData(dataList: List<SavedLocationEntity>) {
        locationList.clear()
        fragmentList.clear()
        dataList.forEach {
            locationList.add(it)
            fragmentList.add(WeatherFragment())
        }
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position].apply {
            arguments = bundleOf(
                Pair(Constants.INTENT_CITY_CODE, locationList[position].locationId),
                Pair(Constants.INTENT_CITY_NAME, locationList[position].locationName)
            )
        }
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

}
