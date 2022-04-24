package com.tokyonth.weather.view.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

import java.util.ArrayList

import com.tokyonth.weather.R
import com.tokyonth.weather.data.entity.LocationEntity
import com.tokyonth.weather.data.db.DbManager
import com.tokyonth.weather.databinding.LayoutCityAddsBinding
import com.tokyonth.weather.utils.ktx.CommonUtils
import com.tokyonth.weather.utils.ktx.visibleOrGone
import com.tokyonth.weather.ui.adapter.CityHotAdapter
import com.tokyonth.weather.ui.adapter.CitySearchAdapter
import com.tokyonth.weather.utils.ktx.lazyBind
import com.tokyonth.weather.utils.manager.AmapLocationManager

class CitySheetDialog : BottomSheetDialogFragment() {

    private val binding: LayoutCityAddsBinding by lazyBind()

    private var behavior: BottomSheetBehavior<FrameLayout>? = null

    private val cityHotAdapter = CityHotAdapter()
    private val location = AmapLocationManager.INSTANCE

    private var foundCityEntryList: MutableList<LocationEntity> = ArrayList()
    private var citySearchAdapter = CitySearchAdapter(foundCityEntryList)

    private var citySelectionCallBack: ((LocationEntity) -> Unit)? = null

    fun setCitySelectionCallBack(citySelectionCallBack: (LocationEntity) -> Unit) {
        this.citySelectionCallBack = citySelectionCallBack
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (context == null) {
            super.onCreateDialog(savedInstanceState)
        } else {
            BottomSheetDialog(requireContext(), R.style.Theme_BottomDialog)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val d = dialog as BottomSheetDialog
        val bottomSheet: FrameLayout =
            d.delegate.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = CommonUtils.getScreenHeight()

        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    fun showDialog(fragmentActivity: FragmentActivity) {
        super.show(fragmentActivity.supportFragmentManager, "cityDialog")
        /*location.currentLocal {
            cityHotAdapter.setFirstCity(it!!.districtName)
        }*/
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
      //  location.stop()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {
        binding.rvHotCity.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = cityHotAdapter.apply {
                setItemClickListener {
                    if (it.locationId.isEmpty()) {

                    } else {
                        citySelectionCallBack?.invoke(it)
                    }
                }
            }
        }

        binding.rvSearchCity.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = citySearchAdapter.apply {
                setOnItemClickListener {
                    citySelectionCallBack?.invoke(it)
                }
            }
        }

        binding.etSearchCity.doOnTextChanged { text, _, _, _ ->
            foundCityEntryList.clear()
            val strTrim = text.toString().trim { it <= ' ' }
            if (strTrim.isNotEmpty()) {
                val sqlData = DbManager.db.dimQueryLocationByName(strTrim)
                if (!sqlData.isNullOrEmpty()) {
                    foundCityEntryList.addAll(sqlData)
                }
            }

            citySearchAdapter.notifyDataSetChanged()
            binding.rvSearchCity.visibleOrGone(!foundCityEntryList.isNullOrEmpty())
        }
    }

}
