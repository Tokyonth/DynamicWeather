package com.tokyonth.weather.ui.activity

import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.tokyonth.weather.Constants
import com.tokyonth.weather.base.BaseActivity
import com.tokyonth.weather.databinding.ActivityMainBinding
import com.tokyonth.weather.ui.adapter.WeatherPagerAdapter
import com.tokyonth.weather.data.WeatherHelper
import com.tokyonth.weather.utils.ktx.lazyBind
import com.tokyonth.weather.R
import com.tokyonth.weather.ui.viewmodel.MainViewModel
import com.tokyonth.weather.utils.SPUtils.getSP
import com.tokyonth.weather.utils.ktx.string

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by lazyBind()

    private val model: MainViewModel by viewModels()

    private var cityLauncher: ActivityResultLauncher<Intent>? = null

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

        cityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val position = it.data?.getIntExtra(Constants.CITY_SELECT_RESULT, -1)
                if (position != null && position != -1) {
                    binding.vpWeatherPage.currentItem = position
                }
            }
        }
    }

    override fun initView() {
        binding.toolbar.overflowIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_more)
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun initObserve() {
        model.savedAllCityLiveData.observe(this) {
            binding.vpWeatherPage.adapter = WeatherPagerAdapter(it, this@MainActivity)
            binding.indicatorPager.attachToViewPager2(binding.vpWeatherPage)
        }

        model.cityChangeLiveData.observe(this) {
            Log.e("打印-->", it)
            binding.tvCityName.text = it
        }

        model.backgroundChangeLiveData.observe(this) {
            val drawType = WeatherHelper.getDrawerType(it)
            binding.weatherView.setDrawerType(drawType)
        }
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
            R.id.action_city -> cityLauncher?.launch(
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
        cityLauncher?.unregister()
    }

}
