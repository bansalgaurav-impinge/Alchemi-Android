package com.app.alchemi.views.adapters

import Constants
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.models.TopGainer
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.fragments.dashboardFeatures.CoinDetailFragment
import com.app.alchemi.views.fragments.dashboardFeatures.TradeListFragment
import kotlinx.android.synthetic.main.top_gainers_card.view.*
import java.math.RoundingMode


internal class TopGainersAdapter(
    topGainersList: List<TopGainer>,
    val tradeListFragment: TradeListFragment
) : RecyclerView.Adapter<TopGainersAdapter.ViewHolder>() {
    val topGainersList: List<TopGainer> = topGainersList
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.top_gainers_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.itemView.tvCoin.text=topGainersList[position].FULLNAME
        holder.itemView.tvCoinName.text=topGainersList[position].FROMSYMBOL
        holder.itemView.ivNotifications.isChecked=topGainersList[position].coin_notification
        val d=topGainersList[position].CHANGEPCT24HOUR.toDouble()
        val rounded = d.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        ViewUtils.loadImage(Constants.IMAGE_URL + "" + topGainersList[position].IMAGEURL, holder.itemView.ivIcon, mContext!!)
        holder.itemView.tvCoinAmount.text=""+ ViewUtils.getFormattedCurrency(ViewUtils.roundOffValue(2, topGainersList[position].PRICE))
        if (ViewUtils.roundOffValue(2, topGainersList[position].CHANGEPCT24HOUR.toDouble())>0){
            holder.itemView.tvCoinAmountVariation.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorGreen))
            holder.itemView.tvCoinAmountVariation.text="+"+ViewUtils.roundOffValue(2, topGainersList[position].CHANGEPCT24HOUR)+"%"
        }else{
            holder.itemView.tvCoinAmountVariation.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorRed))
            holder.itemView.tvCoinAmountVariation.text=""+ViewUtils.roundOffValue(2, topGainersList[position].CHANGEPCT24HOUR)+"%"
        }
        holder.itemView.ivNotifications.setOnCheckedChangeListener { _, isChecked ->
            tradeListFragment.updateCoinNotifications(isChecked,topGainersList[position].FROMSYMBOL, holder.itemView.ivNotifications)
        }

        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation)

        /**
         *  click get detail screen
         */
        holder.itemView.setOnClickListener {
            val  bundle= Bundle()
            bundle.putSerializable(Constants.KEY_TITLE, topGainersList[position].FULLNAME)
            bundle.putSerializable(Constants.KEY_DESCRIPTION, topGainersList[position].FROMSYMBOL)
            bundle.putSerializable(Constants.KEY_PRICE, topGainersList[position].PRICE)
            bundle.putSerializable(Constants.KEY_PRICE_VARIATIONS, topGainersList[position].CHANGEPCT24HOUR.toDouble())
            bundle.putSerializable(Constants.KEY_RANK, (position+1))
            val activity = mContext as Activity
            (activity as HomeActivity).replaceFragment(CoinDetailFragment(), "" + CoinDetailFragment, bundle)
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
