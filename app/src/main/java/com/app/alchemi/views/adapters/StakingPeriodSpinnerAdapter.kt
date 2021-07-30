package com.app.alchemi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.interfaceintercation.OnItemClickListener
import com.app.alchemi.models.Option
import com.app.alchemi.views.fragments.dashboardFeatures.StakingPeriodFragment
import kotlinx.android.synthetic.main.spinner_item_list.view.itemName
import kotlinx.android.synthetic.main.staking_adapter.view.*


internal class StakingPeriodSpinnerAdapter(
        optionList: List<Option>,
        stakingPeriodFragment: StakingPeriodFragment) : RecyclerView.Adapter<StakingPeriodSpinnerAdapter.ViewHolder>() {
    val optionList: List<Option> = optionList
    private var mContext: Context? = null
    var onItemClickListener: OnItemClickListener =stakingPeriodFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.staking_adapter, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.itemName.text= optionList?.get(position).name
        holder.itemView.animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation)
        if (optionList[position].name==Constants.KEY_SELECTED_SPINNER_VALUE){
            holder.itemView.itemName.setTextColor(ContextCompat.getColor(mContext!!,R.color.colorYellow))
        }else{
            holder.itemView.itemName.setTextColor(ContextCompat.getColor(mContext!!,R.color.black))
        }
        holder.itemView.setOnClickListener {
            onItemClickListener.getItemClicked(""+position)
            Constants.KEY_SELECTED_SPINNER_VALUE=optionList[position].name
            notifyDataSetChanged()
        }
        if (position==optionList.size-1){
            holder.itemView.view.visibility=View.GONE
        }
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init { mContext = itemView.context
        }
    }
}
