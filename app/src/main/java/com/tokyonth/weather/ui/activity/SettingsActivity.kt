package com.tokyonth.weather.ui.activity

import com.tokyonth.weather.base.BaseActivity
import com.tokyonth.weather.R
import com.tokyonth.weather.databinding.ActivitySettingsBinding
import com.tokyonth.weather.ui.fragment.SettingsFragment
import com.tokyonth.weather.utils.ktx.lazyBind
import com.tokyonth.weather.utils.ktx.string

class SettingsActivity : BaseActivity() {

    private val binding: ActivitySettingsBinding by lazyBind()

    override fun setVbRoot() = binding

    override fun setBarTitle() = string(R.string.title_settings)

    override fun initData() {}

    override fun initView() {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.frameSettingsContainer.id, SettingsFragment())
            .commit()
    }

    override fun initObserve() {}

}
