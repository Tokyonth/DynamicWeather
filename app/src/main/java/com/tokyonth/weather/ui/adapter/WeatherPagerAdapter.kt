package com.tokyonth.weather.ui.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.tokyonth.weather.Constants
import com.tokyonth.weather.data.entity.SavedCityEntity
import com.tokyonth.weather.ui.fragment.WeatherFragment

import java.util.ArrayList

class WeatherPagerAdapter(
    private val cityList: List<SavedCityEntity>,
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList: MutableList<Fragment> = ArrayList()

    init {
        cityList.forEach { _ ->
            fragmentList.add(WeatherFragment())
        }
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position].apply {
            arguments = bundleOf(
                Pair(Constants.INTENT_CITY_CODE, cityList[position].locationId),
                Pair(Constants.INTENT_CITY_NAME, cityList[position].locationName)
            )
        }
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

}
