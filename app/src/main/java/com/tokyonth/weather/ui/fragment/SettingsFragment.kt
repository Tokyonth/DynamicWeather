package com.tokyonth.weather.ui.fragment

import android.os.Bundle
import android.content.Intent
import android.content.DialogInterface
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import androidx.preference.PreferenceFragmentCompat

import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.tokyonth.weather.R
import com.tokyonth.weather.Constants
import com.tokyonth.weather.ui.activity.AboutActivity
import com.tokyonth.weather.utils.SPUtils.getSP
import com.tokyonth.weather.utils.SPUtils.putSP

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener,
    Preference.OnPreferenceChangeListener {

    companion object {
        val PREFER_KEY = arrayOf(
            "pf_notifications_weather_style",
            "pf_notifications_weather",
            "pf_lock_screen_weather",
            "pf_use_blur",
            "pf_use_picture",
            "pf_choice_picture",
            "pf_data_form",
            "pf_open_source_code",
            "pf_about",
            "pf_blur"
        )
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey)
        initView()
    }

    private fun initView() {
        findPreference("pf_data_form")?.summary = "极速数据"

        requireContext().apply {
            getSP(Constants.SP_PICTURE_PATH_KEY, "").let {
                if (it.isNotEmpty()) {
                    findPreference("pf_choice_picture")?.summary = it
                }
            }
            blurSet(getSP(Constants.SP_USE_PICTURE_BACKGROUND_KEY, false))
        }

        PREFER_KEY.forEachIndexed { index, s ->
            if (index in 1..4) {
                findPreference(s)?.onPreferenceChangeListener = this
            } else {
                findPreference(s)?.onPreferenceClickListener = this
            }
        }
    }

    private fun findPreference(key: String): Preference? {
        return findPreference<Preference>(key)
    }

    private fun blurSet(bool: Boolean) {
        findPreference("pf_set_blur_radius")?.isEnabled = bool
        findPreference<SwitchPreferenceCompat>("pf_use_blur")?.apply {
            isChecked = bool
            isEnabled = bool
        }
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            PREFER_KEY[0] -> MaterialAlertDialogBuilder(requireContext())
                .setTitle("请选择")
                .setSingleChoiceItems(
                    arrayOf("天气图标", "软件图标"),
                    requireContext().getSP(Constants.SP_NOTIFICATION_WEATHER_STYLE_KEY, 0)
                ) { dialog: DialogInterface, which: Int ->
                    //SPUtils.putData(Constants.SP_NOTIFICATION_WEATHER_STYLE_KEY, which)
                    requireContext().putSP(Constants.SP_NOTIFICATION_WEATHER_STYLE_KEY, which)
                    if (requireContext().getSP(Constants.SP_NOTIFICATION_WEATHER_KEY, false)) {
                       // NotificationWeather(requireContext(), true)
                    }
                    dialog.dismiss()
                }
                .create().show()

            PREFER_KEY[5] -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.putExtra("crop", true)
                intent.putExtra("return-data", true)
                startActivityForResult(intent, 0)
            }
            PREFER_KEY[6] -> MaterialAlertDialogBuilder(requireContext())
                .setTitle("请选择")
                .setSingleChoiceItems(
                    arrayOf("极速数据"),
                    0
                ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                .setNegativeButton("确定", null)
                .create().show()
            PREFER_KEY[7] -> MaterialAlertDialogBuilder(requireContext())
                .setTitle("开源")
                .setPositiveButton("确定", null)
                .create().show()
            PREFER_KEY[8] -> startActivity(Intent(requireContext(), AboutActivity::class.java))

            PREFER_KEY[9] -> {

            }
        }
        return false
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        when (preference.key) {
            PREFER_KEY[1] -> {
              //  WeatherNotification.with(requireContext()).start(newValue as Boolean)
                requireContext().putSP(Constants.SP_NOTIFICATION_WEATHER_KEY, newValue)
            }
            PREFER_KEY[2] -> requireContext().putSP(
                Constants.SP_LOCK_SCREEN_WEATHER_KEY,
                newValue
            )
            PREFER_KEY[3] -> requireContext().putSP(Constants.SP_USE_BLUR_KEY, newValue)
            PREFER_KEY[4] -> {
                requireContext().putSP(Constants.SP_USE_PICTURE_BACKGROUND_KEY, newValue)
                blurSet(newValue as Boolean)
            }
        }
        return true
    }

}
