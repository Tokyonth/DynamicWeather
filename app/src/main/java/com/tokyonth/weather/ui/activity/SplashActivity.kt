package com.tokyonth.weather.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.amap.api.location.AMapLocationClient

import com.tokyonth.weather.Constants
import com.tokyonth.weather.databinding.ActivitySplashBinding
import com.tokyonth.weather.ui.viewmodel.CityImportViewModel
import com.tokyonth.weather.utils.SPUtils.putSP
import com.tokyonth.weather.utils.ktx.delay
import com.tokyonth.weather.utils.ktx.lazyBind
import com.tokyonth.weather.base.BaseActivity
import com.tokyonth.weather.R
import com.tokyonth.weather.utils.ktx.snack
import com.tokyonth.weather.utils.ktx.string

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private val binding: ActivitySplashBinding by lazyBind()

    private val model: CityImportViewModel by viewModels()

    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null

    override fun setVbRoot() = binding

    override fun initData() {
        /*  if (!NetworkUtil.isNetworkAvailable) {
              MaterialAlertDialogBuilder(this)
                  .setTitle(string(R.string.text_tips))
                  .setMessage(string(R.string.text_network_not_connection))
                  .setPositiveButton(string(R.string.text_exit)) { _, _ -> finish() }
                  .setCancelable(false)
                  .create()
                  .show()
          }*/
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                it.filter { map ->
                    !map.value
                }.isEmpty().let { all ->
                    if (all) {
                        model.importCityData()
                    } else {
                        snack(string(R.string.text_allow_permission))
                    }
                }
            }
    }

    override fun initView() {
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_anim)
        binding.ivSplashLoading.startAnimation(rotateAnimation)
        MaterialAlertDialogBuilder(this)
            .setTitle(string(R.string.text_permission_title))
            .setMessage(string(R.string.text_permission_msg))
            .setNegativeButton(string(R.string.text_definite)) { _, _ -> requestPermission() }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun initObserve() {
        model.importCityLiveData.observe(this) {
            val msg = if (it) {
                startHomeActivity()
                string(R.string.text_import_success)
            } else {
                string(R.string.text_import_failed)
            }
            snack(msg)
        }
    }

    private fun requestPermission() {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).let {
            permissionLauncher?.launch(it)
        }
    }

    private fun startHomeActivity() {
        AMapLocationClient.updatePrivacyShow(this, true, true)
        AMapLocationClient.updatePrivacyAgree(this, true)
        putSP(Constants.IMPORT_DATA, false)
        delay(1000) {
            Intent(this, MainActivity::class.java).let {
                startActivity(it)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionLauncher?.unregister()
    }

}
