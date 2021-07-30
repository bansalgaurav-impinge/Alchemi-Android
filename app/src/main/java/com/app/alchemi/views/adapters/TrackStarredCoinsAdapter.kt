package com.app.alchemi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.models.TopGainer
import com.app.alchemi.utils.ViewUtils
import kotlinx.android.synthetic.main.card_track_coin_adapter.view.*

import java.math.RoundingMode


internal class TrackStarredCoinsAdapter(
        starredList: List<TopGainer>) : RecyclerView.Adapter<TrackStarredCoinsAdapter.ViewHolder>() {
    val starredList: List<TopGainer> = starredList
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_track_coin_adapter, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.itemView.tvCoin.text=starredList[position].FULLNAME

            holder.itemView.tvCoinName.text = starredList[position].FROMSYMBOL

        val d=starredList[position].CHANGEPCT24HOUR
        val rounded = d.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        holder.itemView.tvNumber.visibility=View.VISIBLE
        holder.itemView.tvNumber.text=""+(position+1)
        holder.itemView.tvCoinAmount.text=""+  ViewUtils.getFormattedCurrency(ViewUtils.roundOffValue(2,starredList[position].PRICE))
        if (ViewUtils.roundOffValue(2,starredList[position].CHANGEPCT24HOUR.toDouble())>0){
            holder.itemView.tvCoinAmountVariation.setTextColor(ContextCompat.getColor(mContext!!,R.color.colorGreen))
            holder.itemView.tvCoinAmountVariation.setBackgroundColor(ContextCompat.getColor(mContext!!,R.color.colorTextBgGreen))
            holder.itemView.tvCoinAmountVariation.text="+"+ViewUtils.roundOffValue(2,starredList[position].CHANGEPCT24HOUR)+"%"

        }else{
            holder.itemView.tvCoinAmountVariation.setTextColor(ContextCompat.getColor(mContext!!,R.color.white))
            holder.itemView.tvCoinAmountVariation.setBackgroundColor(ContextCompat.getColor(mContext!!,R.color.colorRedBg))
            holder.itemView.tvCoinAmountVariation.text=""+ViewUtils.roundOffValue(2,starredList[position] .CHANGEPCT24HOUR)+"%"

        }
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation)

    }

    override fun getItemCount(): Int {
        return starredList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init { mContext = itemView.context
        }
    }
}
