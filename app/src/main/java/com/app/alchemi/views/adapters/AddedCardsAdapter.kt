package com.app.alchemi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.roomdatabase.CardDetail
import com.app.alchemi.views.fragments.dashboardFeatures.HomeFragment
import kotlinx.android.synthetic.main.added_cards_adapter_layout.view.*


internal class AddedCardsAdapter(
    cardList: List<CardDetail>?,
   val homeFragment: HomeFragment
) : RecyclerView.Adapter<AddedCardsAdapter.ViewHolder>() {
    private val cardList: List<CardDetail>? = cardList
    private var mContext: Context? = null
    var selectedIndex:Int?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.added_cards_adapter_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.itemView.tvCardNumber.text= homeFragment.maskCardNumber(cardList?.get(position)?.card_number!!,"#### XXXX XXXX ####")
        if(cardList[position].cardType=="0"){
            holder.itemView.ivCard.setImageResource(R.drawable.ic_matercard)
        }else{
            holder.itemView.ivCard.setImageResource(R.drawable.visa_card)
        }
        if (selectedIndex==position){
           holder.itemView.ivSelected.visibility=View.VISIBLE
       }else{
           holder.itemView.ivSelected.visibility=View.GONE
       }
        holder.itemView.setOnClickListener {
            selectedIndex=position

            notifyDataSetChanged()
        }
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation)

    }

    override fun getItemCount(): Int {
        return cardList!!.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init { mContext = itemView.context
        }
    }
}
