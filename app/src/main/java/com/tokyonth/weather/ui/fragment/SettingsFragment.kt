package com.tokyonth.weather.ui.fragment

import android.os.Bundle
import android.content.Intent
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.tokyonth.weather.R
import com.tokyonth.weather.Constants
import com.tokyonth.weather.service.notification.WeatherNotification
import com.tokyonth.weather.ui.activity.AboutActivity
import com.tokyonth.weather.utils.SPUtils.putSP
import com.tokyonth.weather.utils.ktx.string

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener,
    Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey)
        initView()
    }

    private fun initView() {
        findPreference(string(R.string.key_preference_notification))?.onPreferenceChangeListener =
            this
        findPreference(string(R.string.key_preference_choose_image))?.onPreferenceClickListener =
            this
        findPreference(string(R.string.key_preference_open_source))?.onPreferenceClickListener =
            this
        findPreference(string(R.string.key_preference_about))?.onPreferenceClickListener = this
    }

    private fun findPreference(key: String): Preference? {
        return findPreference<Preference>(key)
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            string(R.string.key_preference_choose_image) -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.putExtra("crop", true)
                intent.putExtra("return-data", true)
                startActivityForResult(intent, 0)
            }
            string(R.string.key_preference_open_source) -> MaterialAlertDialogBuilder(requireContext())
                .setTitle("开源")
                .setPositiveButton("确定", null)
                .create().show()
            string(R.string.key_preference_about) -> startActivity(
                Intent(
                    requireContext(),
                    AboutActivity::class.java
                )
            )
        }
        return false
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        when (preference.key) {
            string(R.string.key_preference_notification) -> {
                WeatherNotification.with(requireContext()).start(newValue as Boolean)
                requireContext().putSP(Constants.SP_NOTIFICATION_WEATHER_KEY, newValue)
            }
        }
        return true
    }

}
