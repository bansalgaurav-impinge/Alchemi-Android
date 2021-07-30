package com.app.alchemi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.interfaceintercation.OnItemClickListener
import com.app.alchemi.views.fragments.dashboardFeatures.TrackFragment
import kotlinx.android.synthetic.main.spinner_item_list.view.*


internal class TrackCoinSpinnerAdapter(
        optionList: Array<String>,
        trackFragment: TrackFragment) : RecyclerView.Adapter<TrackCoinSpinnerAdapter.ViewHolder>() {
    val optionList: Array<String> = optionList
    private var mContext: Context? = null
    var onItemClickListener: OnItemClickListener =trackFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.spinner_item_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.itemName.text=optionList[position]
        holder.itemView.animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation)
        holder.itemView.setOnClickListener {
            onItemClickListener.getItemClicked(""+position)
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
