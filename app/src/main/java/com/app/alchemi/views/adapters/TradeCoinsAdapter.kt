package com.app.alchemi.views.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.models.CryptoLiveData
import com.app.alchemi.models.TopGainer
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.fragments.dashboardFeatures.CoinDetailFragment
import com.app.alchemi.views.fragments.settings.WebViewFragment
import kotlinx.android.synthetic.main.top_gainers_card.view.*
import kotlinx.android.synthetic.main.top_gainers_card.view.ivIcon
import kotlinx.android.synthetic.main.top_gainers_card.view.tvCoin
import kotlinx.android.synthetic.main.top_gainers_card.view.tvCoinAmount
import kotlinx.android.synthetic.main.top_gainers_card.view.tvCoinAmountVariation
import kotlinx.android.synthetic.main.top_gainers_card.view.tvCoinName
import kotlinx.android.synthetic.main.trade_coin_card.view.*


internal class TradeCoinsAdapter(
    tradeList: List<CryptoLiveData>
) : RecyclerView.Adapter<TradeCoinsAdapter.ViewHolder>() {
    val tradeList: List<CryptoLiveData> = tradeList
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.trade_coin_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        if ((position+1)%2==0){
            holder.itemView.clParent.background.setTint(ContextCompat.getColor(mContext!!,R.color.colorYellow))
            holder.itemView.ivGraph.drawable.setTint(ContextCompat.getColor(mContext!!,R.color.black))
        }else{
            holder.itemView.clParent.background.setTint(ContextCompat.getColor(mContext!!,R.color.white))
            holder.itemView.ivGraph.drawable.setTint(ContextCompat.getColor(mContext!!,R.color.colorYellow))
        }
        if (tradeList.isNotEmpty()){
            if (position==0){
                holder.itemView.tvCoin.text="Bitcoin"
            }
            else if (position==1){
                holder.itemView.tvCoin.text="Binance Coin"
            }
            if (position==2){
                holder.itemView.tvCoin.text="Tron"
            }
        }
        holder.itemView.tvCoinName.text=tradeList[position].FROMSYMBOL
        val d=tradeList[position].CHANGEPCT24HOUR
        ViewUtils.loadImage(Constants.IMAGE_URL+""+tradeList[position].IMAGEURL,holder.itemView.ivIcon,mContext!!)

        holder.itemView.tvCoinAmount.text=""+ ViewUtils.getFormattedCurrency(ViewUtils.roundOffValue(2,tradeList[position].PRICE))
        if (ViewUtils.roundOffValue(2,tradeList[position].CHANGEPCT24HOUR)>0){
            holder.itemView.tvCoinAmountVariation.setTextColor(ContextCompat.getColor(mContext!!,R.color.colorGreen))
            holder.itemView.tvCoinAmountVariation.text="+"+ViewUtils.roundOffValue(2,tradeList[position].CHANGEPCT24HOUR)+"%"

        }else{
            holder.itemView.tvCoinAmountVariation.setTextColor(ContextCompat.getColor(mContext!!,R.color.colorRed))
            holder.itemView.tvCoinAmountVariation.text=""+ViewUtils.roundOffValue(2,tradeList[position].CHANGEPCT24HOUR)+"%"

        }
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation)
        /**
         *  click get detail screen
         */
        holder.itemView.setOnClickListener {
          var title="Bitcoin"
            if (position==0){
                title ="Bitcoin"
            }
            else if (position==1){
                title="Binance Coin"
            }
            if (position==2){
                title="Tron"
            }
            val  bundle= Bundle()
            bundle.putSerializable(Constants.KEY_TITLE,title)
            bundle.putSerializable(Constants.KEY_DESCRIPTION, tradeList[position].FROMSYMBOL)
            bundle.putSerializable(Constants.KEY_PRICE, tradeList[position].PRICE)
            bundle.putSerializable(Constants.KEY_PRICE_VARIATIONS, tradeList[position].CHANGEPCT24HOUR)
            bundle.putSerializable(Constants.KEY_RANK, (position+1))
            val activity = mContext as Activity
            (activity as HomeActivity).replaceFragment(CoinDetailFragment(), "" + CoinDetailFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return tradeList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init { mContext = itemView.context
        }
    }
}
