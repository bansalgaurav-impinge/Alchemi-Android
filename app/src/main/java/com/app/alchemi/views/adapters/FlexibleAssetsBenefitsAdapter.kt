package com.app.alchemi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import kotlinx.android.synthetic.main.assets_adapter_card.view.*


internal class FlexibleAssetsBenefitsAdapter(
        topGainersList: List<HashMap<String,String>>) : RecyclerView.Adapter<FlexibleAssetsBenefitsAdapter.ViewHolder>() {
    val topGainersList: List<HashMap<String,String>> = topGainersList
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.assets_adapter_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tvCoin.text=topGainersList[position][Constants.KEY_FIRST_NAME]
        holder.itemView.tvCoinAmount.text=topGainersList[position][Constants.KEY_LAST_NAME]
        holder.itemView.ivIcon.visibility= View.GONE
        if (position==topGainersList.size-1){
            holder.itemView.view.visibility=View.GONE
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
