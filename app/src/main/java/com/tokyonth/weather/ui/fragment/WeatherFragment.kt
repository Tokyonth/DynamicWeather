package com.tokyonth.weather.ui.fragment

import android.content.Intent
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.tokyonth.weather.Constants
import com.tokyonth.weather.R
import com.tokyonth.weather.base.BaseFragment
import com.tokyonth.weather.databinding.FragmentWeatherBinding
import com.tokyonth.weather.ui.activity.CityActivity
import com.tokyonth.weather.ui.viewmodel.WeatherViewModel
import com.tokyonth.weather.ui.fragment.provider.*
import com.tokyonth.weather.ui.viewmodel.MainViewModel
import com.tokyonth.weather.utils.ktx.lazyBind
import com.tokyonth.weather.utils.ktx.string
import com.tokyonth.weather.utils.manager.AmapLocationManager

class WeatherFragment : BaseFragment() {

    private val binding: FragmentWeatherBinding by lazyBind()

    private val model: WeatherViewModel by viewModels()

    private val dyModel: MainViewModel by activityViewModels()

    private var isLoaded = false

    private var cityName: String? = null

    override fun setVbRoot() = binding

    override fun initData() {
        val locationCode = arguments?.getString(Constants.INTENT_LOCATION_CODE)
        val isDefaultLocation = arguments?.getBoolean(Constants.INTENT_IS_DEFAULT_LOCATION, false)
        cityName = arguments?.getString(Constants.INTENT_LOCATION_NAME)

        if (isDefaultLocation == true) {
            AmapLocationManager.INSTANCE.currentLocal {
                if (it != null) {
                    model.pickLocation(it.first)
                    cityName = it.second
                    dyModel.cityChangeLiveData.value = cityName
                } else {
                    cannotFoundDefaultCity()
                }
                AmapLocationManager.INSTANCE.stop()
            }
        } else {
            model.setLocation(locationCode!!)
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
        if (!isLoaded) {
            model.refreshWeather()
            isLoaded = true
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

    private fun cannotFoundDefaultCity() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(string(R.string.text_tips))
            .setMessage(string(R.string.text_failed_location))
            .setNegativeButton(
                string(R.string.text_manual_selection)
            ) { _, _ ->
                startActivity(
                    Intent(
                        requireContext(),
                        CityActivity::class.java
                    )
                )
            }
            .setPositiveButton(string(R.string.text_cancel)) { _, _ -> requireActivity().finish() }
            .setCancelable(false)
            .create()
            .show()
    }

}
