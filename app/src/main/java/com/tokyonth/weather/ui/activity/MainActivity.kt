package com.tokyonth.weather.ui.activity

import android.view.Menu
import android.view.MenuItem
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.tokyonth.weather.Constants
import com.tokyonth.weather.base.BaseActivity
import com.tokyonth.weather.databinding.ActivityMainBinding
import com.tokyonth.weather.ui.adapter.WeatherPagerAdapter
import com.tokyonth.weather.data.WeatherHelper
import com.tokyonth.weather.utils.ktx.lazyBind
import com.tokyonth.weather.R
import com.tokyonth.weather.data.event.CityChangeEvent
import com.tokyonth.weather.data.event.CitySelectEvent
import com.tokyonth.weather.ui.viewmodel.MainViewModel
import com.tokyonth.weather.utils.SPUtils.getSP
import com.tokyonth.weather.utils.event.LifecycleEventBus
import com.tokyonth.weather.utils.event.ThreadMode
import com.tokyonth.weather.utils.ktx.string

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by lazyBind()

    private val model: MainViewModel by viewModels()

    private var pagerAdapter = WeatherPagerAdapter(this)

    override fun setVbRoot() = binding

    override fun isDarkStatusBars() = false

    override fun setBarTitle() = ""

    override fun initData() {
        if (getSP(Constants.IMPORT_DATA, true)) {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
            return
        }

        model.getAllCityCount()
    }

    override fun initView() {
        binding.toolbar.overflowIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_more)
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.vpWeatherPage.adapter = pagerAdapter
        binding.indicatorPager.attachToViewPager2(binding.vpWeatherPage)
    }

    override fun initObserve() {
        model.savedAllCityLiveData.observe(this) {
            pagerAdapter.setData(it)
            binding.indicatorPager.setCount(it.size)
        }

        model.cityChangeLiveData.observe(this) {
            binding.tvCityName.text = it
        }

        model.backgroundChangeLiveData.observe(this) {
            val drawType = WeatherHelper.getDrawerType(it)
            binding.weatherView.setDrawerType(drawType)
        }

        LifecycleEventBus.observe(this, CityChangeEvent::class.java, {
            model.getAllCityCount()
        }, ThreadMode.MAIN)

        LifecycleEventBus.observe(this, CitySelectEvent::class.java, {
            binding.vpWeatherPage.currentItem = it.position
        }, ThreadMode.MAIN)
    }

    private fun cannotFoundDefaultCity() {
        MaterialAlertDialogBuilder(this)
            .setTitle(string(R.string.text_tips))
            .setMessage(string(R.string.text_failed_location))
            .setNegativeButton(
                string(R.string.text_manual_selection)
            ) { _, _ ->
                startActivity(
                    Intent(
                        this@MainActivity,
                        CityActivity::class.java
                    )
                )
            }
            .setPositiveButton(string(R.string.text_cancel)) { _, _ -> finish() }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_city -> startActivity(
                Intent(
                    this@MainActivity,
                    CityActivity::class.java
                )
            )
            R.id.action_settings -> startActivity(
                Intent(
                    this@MainActivity,
                    SettingsActivity::class.java
                )
            )
            R.id.action_warning -> startActivity(
                Intent(
                    this@MainActivity,
                    WarningActivity::class.java
                )
            )
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        binding.weatherView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.weatherView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.weatherView.onDestroy()
    }

}
