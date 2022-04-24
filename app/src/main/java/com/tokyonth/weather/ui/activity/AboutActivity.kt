package com.tokyonth.weather.ui.activity

import android.os.StrictMode.VmPolicy
import android.os.StrictMode
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuItem

import java.io.File

import com.tokyonth.weather.BuildConfig
import com.tokyonth.weather.base.BaseActivity
import com.tokyonth.weather.utils.ktx.lazyBind
import com.tokyonth.weather.databinding.ActivityAboutBinding
import com.tokyonth.weather.R
import com.tokyonth.weather.utils.ktx.string

class AboutActivity : BaseActivity() {

    private val binding: ActivityAboutBinding by lazyBind()

    override fun setVbRoot() = binding

    override fun setBarTitle() = string(R.string.title_about)

    override fun initData() {
        VmPolicy.Builder().let {
            StrictMode.setVmPolicy(it.build())
            it.detectFileUriExposure()
        }
    }

    override fun initView() {
        binding.tvVersion.append(BuildConfig.VERSION_NAME)
    }

    override fun initObserve() {}

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_about, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_about_shard) {
            val apkFile = File(packageResourcePath)
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "file/*"
                putExtra(Intent.EXTRA_STREAM, Uri.fromFile(apkFile))
            }
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}
