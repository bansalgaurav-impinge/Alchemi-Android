package com.app.alchemi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.interfaceintercation.OnItemClickListener
import com.app.alchemi.models.TopGainer
import com.app.alchemi.views.fragments.dashboardFeatures.HomeFragment
import kotlinx.android.synthetic.main.buy_crypto_adapter.view.*


internal class BuyCoinAdapter(
        topGainersList: List<TopGainer>,
        homeFragment: HomeFragment) : RecyclerView.Adapter<BuyCoinAdapter.ViewHolder>() {
    val topGainersList: List<TopGainer> = topGainersList
    private var mContext: Context? = null
    var onItemClickListener: OnItemClickListener =homeFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.buy_crypto_adapter, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.itemView.tvName.text=topGainersList[position].FULLNAME
        holder.itemView.tvNameShort.text= topGainersList[position].FULLNAME.first().toString()
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation)
        holder.itemView.setOnClickListener {
            onItemClickListener.getItemClicked(""+position)

        }
    }

    override fun getItemCount(): Int {
        return topGainersList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init { mContext = itemView.context
        }
    }
}
