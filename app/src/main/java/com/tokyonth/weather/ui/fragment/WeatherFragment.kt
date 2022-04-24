package com.tokyonth.weather.ui.fragment

import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import com.tokyonth.weather.Constants
import com.tokyonth.weather.base.BaseFragment
import com.tokyonth.weather.databinding.FragmentWeatherBinding
import com.tokyonth.weather.ui.viewmodel.WeatherViewModel
import com.tokyonth.weather.ui.fragment.provider.*
import com.tokyonth.weather.ui.viewmodel.DynamicViewModel
import com.tokyonth.weather.utils.ktx.lazyBind

class WeatherFragment : BaseFragment() {

    private val binding: FragmentWeatherBinding by lazyBind()

    private val model: WeatherViewModel by viewModels()

    private val dyModel: DynamicViewModel by activityViewModels()

    private var isLoaded = false

    private var cityCode: String? = null

    private var cityName: String? = null

    override fun setVbRoot() = binding

    override fun initData() {
        cityCode = arguments?.getString(Constants.INTENT_CITY_CODE)
        cityName = arguments?.getString(Constants.INTENT_CITY_NAME)
    }

    override fun initView() {
/*        binding.nestRoot.post {
            binding.consTemp.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, binding.nestRoot.height
            )
        }*/
    }

    override fun onResume() {
        super.onResume()
        if (!isLoaded) {
            model.getCityWeather(cityCode!!)
            model.get24HourWeather(cityCode!!)
            model.get7DayWeather(cityCode!!)
            model.getAirWeather(cityCode!!)
            model.getLifeWeather(cityCode!!, "0")
            //model.getSunWeather(cityCode!!, "2022421")
            isLoaded = true
        }

        dyModel.cityChange.value = cityName
    }

    override fun initObserve() {
        model.nowLiveData.observe(viewLifecycleOwner) {
            //  dyModel.backgroundChange.value = it
            TempWeatherProvider().attach(it, binding)
        }

        model.hour24LiveData.observe(viewLifecycleOwner) {
            Hour24WeatherProvider().attach(it, binding)
        }

        model.dailyLiveData.observe(viewLifecycleOwner) {
            DailyWeatherProvider().attach(it, binding)
        }

        model.airLiveData.observe(viewLifecycleOwner) {
            AirWeatherProvider().attach(it, binding)
        }

        model.lifeLiveData.observe(viewLifecycleOwner) {
            LifeWeatherProvider().attach(it, binding)
        }

        model.sunLiveData.observe(viewLifecycleOwner) {
            SsvWeatherProvider().attach(it, binding)
        }
    }

}
