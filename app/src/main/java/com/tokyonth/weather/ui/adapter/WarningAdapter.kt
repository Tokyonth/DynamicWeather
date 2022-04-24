package com.tokyonth.weather.ui.adapter

import android.view.ViewGroup
import android.view.LayoutInflater
import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView

import com.tokyonth.weather.data.entity.WarningEntity
import com.tokyonth.weather.databinding.ItemWarningBinding
import com.tokyonth.weather.ui.adapter.WarningAdapter.WarningViewHolder

import java.util.ArrayList

class WarningAdapter : RecyclerView.Adapter<WarningViewHolder>() {

    private val warningList: MutableList<WarningEntity> = ArrayList()

    private var itemExpand: ((Int) -> Unit)? = null

    fun setItemExpandListener(itemExpand: (Int) -> Unit) {
        this.itemExpand = itemExpand
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: MutableList<WarningEntity>) {
        warningList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarningViewHolder {
        val vb = ItemWarningBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WarningViewHolder(vb)
    }

    override fun onBindViewHolder(holder: WarningViewHolder, position: Int) {
        holder.bind(itemExpand!!, warningList[position])
    }

    override fun getItemCount(): Int {
        return warningList.size
    }

    class WarningViewHolder(val binding: ItemWarningBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemExpand: (Int) -> Unit, data: WarningEntity) {
            binding.tvItemWarningTitle.text = data.title
            if (data.content.isNotEmpty()) {
                binding.run {
                    tvItemWarningContent.text = String.format("\t\t\t%s", data.content)
                    tvItemWarningContent.setTextColor(data.color)
                    pbItemWarningLoad.visibility = View.GONE
                    ivItemWarningArrow.visibility = View.VISIBLE
                    ivItemWarningArrow.animate().rotation(90F).start()
                }
                binding.tvItemWarningContent.visibility = View.VISIBLE
            }

            binding.root.setOnClickListener {
                val vis = binding.tvItemWarningContent.visibility
                if (vis == View.GONE) {
                    binding.apply {
                        ivItemWarningArrow.visibility = View.GONE
                        pbItemWarningLoad.visibility = View.VISIBLE
                    }
                    itemExpand.invoke(bindingAdapterPosition)
                } else if (vis == View.VISIBLE) {
                    binding.tvItemWarningContent.visibility = View.GONE
                    binding.ivItemWarningArrow.animate().rotation(0F).start()
                }
            }
        }

    }

}
