package com.tokyonth.weather.base

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding
import androidx.appcompat.app.AppCompatActivity

import com.tokyonth.weather.R

abstract class BaseActivity : AppCompatActivity() {

    protected abstract fun setVbRoot(): ViewBinding

    protected abstract fun initData()

    protected abstract fun initView()

    open fun initObserve() {}

    open fun isDarkStatusBars(): Boolean = true

    open fun setBarTitle(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView)?.apply {
            isAppearanceLightStatusBars = isDarkStatusBars()
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(setVbRoot().root)
        initToolbar()
        initData()
        initView()
        initObserve()
    }

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = setBarTitle()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}
