package com.tokyonth.weather.ui.fragment

import android.content.Intent
import android.provider.Settings
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.tokyonth.weather.Constants
import com.tokyonth.weather.base.BaseFragment
import com.tokyonth.weather.databinding.FragmentWeatherBinding
import com.tokyonth.weather.ui.fragment.provider.*
import com.tokyonth.weather.ui.viewmodel.MainViewModel
import com.tokyonth.weather.ui.viewmodel.WeatherViewModel
import com.tokyonth.weather.utils.ktx.lazyBind
import com.tokyonth.weather.utils.manager.AmapLocationManager

class WeatherFragment : BaseFragment() {

    private val binding: FragmentWeatherBinding by lazyBind()

    private val model: WeatherViewModel by viewModels()

    private val dyModel: MainViewModel by activityViewModels()

    private var isLoaded = false

    private var cityName: String? = null

    private var locationCode: String? = null

    private var isDefaultLocation: Boolean? = null

    override fun setVbRoot() = binding

    override fun initData() {
        cityName = arguments?.getString(Constants.INTENT_LOCATION_NAME)
        locationCode = arguments?.getString(Constants.INTENT_LOCATION_CODE)
        isDefaultLocation = arguments?.getBoolean(Constants.INTENT_IS_DEFAULT_LOCATION, false)

        if (isDefaultLocation == true) {
            AmapLocationManager.INSTANCE.currentLocal(this) {
                if (it != null) {
                    model.pickLocation(it.first)
                    cityName = it.second
                    dyModel.cityChangeLiveData.value = cityName
                } else {
                    cannotFoundLocation()
                }
                AmapLocationManager.INSTANCE.stop()
            }
        }
    }

    override fun initView() {
        binding.refreshWeatherView.setOnRefreshListener {
            binding.refreshWeatherView.isRefreshing = true
            model.refreshWeather()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isDefaultLocation == true) {
            AmapLocationManager.INSTANCE.start()
        } else {
            model.setLocation(locationCode!!)
            if (!isLoaded) {
                model.refreshWeather()
                isLoaded = true
            }
        }

        if (!cityName.isNullOrEmpty()) {
            dyModel.cityChangeLiveData.value = cityName
        }
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

    private fun cannotFoundLocation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("开启定位服务")
            .setMessage("天气需要开启定位服务, 以获取当前位置天气")
            .setNegativeButton("取消", null)
            .setPositiveButton("设置") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setCancelable(false)
            .create()
            .show()
    }

}
