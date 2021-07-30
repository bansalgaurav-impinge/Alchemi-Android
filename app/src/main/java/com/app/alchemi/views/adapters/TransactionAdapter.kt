package com.app.alchemi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import kotlinx.android.synthetic.main.transaction_adapter_card.view.*


internal class TransactionAdapter(
        topGainersList: List<HashMap<String,String>>) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    val topGainersList: List<HashMap<String,String>> = topGainersList
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.transaction_adapter_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.itemView.tvCoin.text=topGainersList[position][Constants.KEY_FIRST_NAME]
        holder.itemView.tvCoinAmount.text=topGainersList[position][Constants.KEY_LAST_NAME]
        if (topGainersList[position][Constants.KEY_STATUS]=="1"){
            holder.itemView.tvStatus.text=mContext!!.getString(R.string.successful)
            holder.itemView.tvStatus.setTextColor(ContextCompat.getColor(mContext!!,R.color.colorGreen))
        }
        else if (topGainersList[position][Constants.KEY_STATUS]=="2"){
            holder.itemView.tvStatus.text=mContext?.getString(R.string.pending)
            holder.itemView.tvStatus.setTextColor(ContextCompat.getColor(mContext!!,R.color.colorYellow))
        }
        else if (topGainersList[position][Constants.KEY_STATUS]=="3"){
            holder.itemView.tvStatus.text=mContext?.getString(R.string.failed)
            holder.itemView.tvStatus.setTextColor(ContextCompat.getColor(mContext!!,R.color.colorPieRed))
        }
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
