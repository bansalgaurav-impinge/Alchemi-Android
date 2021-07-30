package com.app.alchemi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import kotlinx.android.synthetic.main.allocation_adapter.view.*


internal class EarnAdapter(
        topGainersList: List<HashMap<String,String>>) : RecyclerView.Adapter<EarnAdapter.ViewHolder>() {
    val topGainersList: List<HashMap<String,String>> = topGainersList
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.allocation_adapter, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.itemView.tvCoin.text=topGainersList[position][Constants.KEY_FIRST_NAME]
        holder.itemView.tvCoinAmount.text=topGainersList[position][Constants.KEY_LAST_NAME]
        holder.itemView.tvCoinName.text="2% P.A."
        holder.itemView.view.visibility=View.VISIBLE
        if (position==topGainersList.size-1){
            holder.itemView.view.visibility=View.GONE
            holder.itemView.cl_parent.background=ContextCompat.getDrawable(mContext!!,R.drawable.allocation_card_bg_bottom)
        }
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation)

    }

    override fun getItemCount(): Int {
        return topGainersList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init { mContext = itemView.context
        }
    }
}
