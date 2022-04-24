package com.tokyonth.weather.ui.activity

import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager

import com.tokyonth.weather.R
import com.tokyonth.weather.base.BaseActivity
import com.tokyonth.weather.data.entity.WarningEntity
import com.tokyonth.weather.ui.adapter.WarningAdapter
import com.tokyonth.weather.databinding.ActivityWarningBinding
import com.tokyonth.weather.ui.viewmodel.WarningViewModel
import com.tokyonth.weather.utils.ktx.lazyBind
import com.tokyonth.weather.utils.ktx.string

class WarningActivity : BaseActivity() {

    companion object {

        private val NMC_URL = arrayOf(
            "http://www.nmc.cn/publish/country/warning/megatemperature.html",
            "http://www.nmc.cn/publish/country/warning/downpour.html",
            "http://www.nmc.cn/publish/country/warning/dust.html",
            "http://www.nmc.cn/publish/country/warning/strong_convection.html"
        )

    }

    private val binding: ActivityWarningBinding by lazyBind()

    private val model: WarningViewModel by viewModels()

    private val warningAdapter = WarningAdapter()

    private val warningList: MutableList<WarningEntity> = ArrayList()

    override fun setVbRoot() = binding

    override fun setBarTitle() = string(R.string.title_warning)

    override fun initData() {
        warningList.apply {
            add(WarningEntity("高温预警", "", 0))
            add(WarningEntity("暴雨预警", "", 0))
            add(WarningEntity("沙尘暴预警", "", 0))
            add(WarningEntity("强对流预警", "", 0))
        }

        warningAdapter.apply {
            setData(warningList)
            setItemExpandListener {
                model.getWarningContent(it, NMC_URL[it])
            }
        }
    }

    override fun initView() {
        binding.rvWarning.apply {
            layoutManager = GridLayoutManager(this@WarningActivity, 1)
            adapter = warningAdapter
        }
    }

    override fun initObserve() {
        model.warningInfoLiveData.observe(this) {
            val position = it.first
            warningList[position].color = it.second
            warningList[position].content = it.third
            warningAdapter.notifyItemChanged(position)
        }
    }

}
