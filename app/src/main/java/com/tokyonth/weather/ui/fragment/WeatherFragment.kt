package com.tokyonth.weather.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import com.tokyonth.weather.Constants
import com.tokyonth.weather.base.BaseFragment
import com.tokyonth.weather.databinding.FragmentWeatherBinding
import com.tokyonth.weather.ui.viewmodel.WeatherViewModel
import com.tokyonth.weather.ui.fragment.provider.*
import com.tokyonth.weather.ui.viewmodel.MainViewModel
import com.tokyonth.weather.utils.ktx.lazyBind

class WeatherFragment : BaseFragment() {

    private val binding: FragmentWeatherBinding by lazyBind()

    private val model: WeatherViewModel by viewModels()

    private val dyModel: MainViewModel by activityViewModels()

    private var isLoaded = false

    private var cityName: String? = null

    override fun setVbRoot() = binding

    override fun initData() {
        val cityCode = arguments?.getString(Constants.INTENT_CITY_CODE)
        model.setLocation(cityCode!!)
        cityName = arguments?.getString(Constants.INTENT_CITY_NAME)
    }

    override fun initView() {
        binding.refreshWeatherView.setOnRefreshListener {
            binding.refreshWeatherView.isRefreshing = true
            model.refreshWeather()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isLoaded) {
            model.getCityWeather()
            model.get24HourWeather()
            model.get7DayWeather()
            model.getAirWeather()
            model.getLifeWeather()
            isLoaded = true
        }

        dyModel.cityChangeLiveData.value = cityName
    }

    override fun initObserve() {
        model.nowLiveData.observe(viewLifecycleOwner) {
            ProviderFactory.create(TempWeatherProvider::class.java).attach(it, binding)
        }

        model.hour24LiveData.observe(viewLifecycleOwner) {
            ProviderFactory.create(Hour24WeatherProvider::class.java).attach(it, binding)
        }

        model.dailyLiveData.observe(viewLifecycleOwner) {
            ProviderFactory.create(DailyWeatherProvider::class.java).attach(it, binding)
        }

        model.airLiveData.observe(viewLifecycleOwner) {
            ProviderFactory.create(AirWeatherProvider::class.java).attach(it, binding)
        }

        model.lifeLiveData.observe(viewLifecycleOwner) {
            ProviderFactory.create(LifeWeatherProvider::class.java).attach(it, binding)
        }

        model.refreshLiveData.observe(viewLifecycleOwner) {
            binding.refreshWeatherView.isRefreshing = false
            //snack("刷新完成!")
        }
    }

}
