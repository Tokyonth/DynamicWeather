package com.tokyonth.weather.ui.activity

import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.biubiu.eventbus.post.postEvent
import com.google.android.material.snackbar.Snackbar

import com.tokyonth.weather.databinding.ActivityCityBinding
import com.tokyonth.weather.base.BaseActivity
import com.tokyonth.weather.ui.adapter.CityManageAdapter
import com.tokyonth.weather.view.widget.CitySheetDialog
import com.tokyonth.weather.utils.ktx.lazyBind
import com.tokyonth.weather.utils.ktx.snack
import com.tokyonth.weather.ui.viewmodel.CityViewModel
import com.tokyonth.weather.R
import com.tokyonth.weather.data.entity.SavedLocationEntity
import com.tokyonth.weather.data.event.CitySelectEvent
import com.tokyonth.weather.utils.ktx.string

class CityActivity : BaseActivity() {

    private val binding: ActivityCityBinding by lazyBind()

    private val model: CityViewModel by viewModels()

    private var cityAdapter = CityManageAdapter()

    override fun setVbRoot() = binding

    override fun setBarTitle() = string(R.string.title_city_manager)

    override fun initData() {
        model.getAllSavedCity()
    }

    override fun initView() {
        binding.fabAddCity.setOnClickListener { addCity() }
        binding.rvCityManage.apply {
            layoutManager = GridLayoutManager(this@CityActivity, 1)
            adapter = cityAdapter
        }
        cityAdapter.setOnItemClickListener(object : CityManageAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                postEvent(CitySelectEvent(position))
                finish()
            }

            override fun onLongClick(view: View, position: Int) {
                if (position == 0) {
                    snack("默认城市不能删除!")
                } else {
                    deleteCity(position)
                }
            }
        })
    }

    override fun initObserve() {
        model.allSavedCityLiveData.observe(this) {
            it.add(0, SavedLocationEntity(0, "", "", "默认"))
            cityAdapter.setData(it)
        }

        model.savedCityLiveData.observe(this) {
            if (it == null) {
                snack("已经存在!")
            } else {
                cityAdapter.getData().add(it)
                cityAdapter.notifyItemChanged(cityAdapter.getData().size)

                model.getManagerCityWeather(cityAdapter.getData().size - 1, it.locationId)
            }
        }

        model.deleteCityLiveData.observe(this) {
            if (it != -1) {
                cityAdapter.getData().removeAt(it)
                cityAdapter.notifyItemRemoved(it)
            } else {
                snack("删除失败!")
            }
        }

        model.foldWeatherLiveData.observe(this) {
            model.fillWeather(cityAdapter.getData()[it.first], it.second)
            cityAdapter.notifyItemChanged(it.first)
        }
    }

    private fun deleteCity(position: Int) {
        Snackbar.make(binding.root, "是否删除?", Snackbar.LENGTH_SHORT)
            .setAction("删除") {
                model.deleteCity(
                    position,
                    cityAdapter.getData()[position]
                )
            }.show()
    }

    private fun addCity() {
        CitySheetDialog().apply {
            setCitySelectionCallBack {
                model.saveCity(it)
                dismiss()
            }
        }.showDialog(this)
    }

}
