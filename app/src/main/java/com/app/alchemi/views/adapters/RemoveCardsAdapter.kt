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
import kotlinx.android.synthetic.main.remove_cards_adapter_layout.view.*
import org.greenrobot.eventbus.EventBus


internal class RemoveCardsAdapter(
    cardList: List<CardDetail>?,
    val homeFragment: HomeFragment
) : RecyclerView.Adapter<RemoveCardsAdapter.ViewHolder>() {
    private val cardList: List<CardDetail>? = cardList
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.remove_cards_adapter_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.itemView.tvCardNumber.text= homeFragment.maskCardNumber(cardList?.get(position)?.card_number.toString().trim(),"#### XXXX XXXX ####")
        if(cardList!![position].cardType=="0"){
            holder.itemView.ivCard.setImageResource(R.drawable.ic_matercard)
        }else{
            holder.itemView.ivCard.setImageResource(R.drawable.visa_card)
        }
        if(position==cardList.size){
            holder.itemView.divider.visibility=View.GONE
        }
        holder.itemView.ivRemoveCard.setOnClickListener {
         //   OnItemClickListener.getItemClicked(""+position)
            homeFragment.deleteCard(position)
            homeFragment.getCardList()
             EventBus.getDefault().post(Constants.KEY_REFRESH_LOCAL_DATA)
//            Handler(Looper.getMainLooper()).postDelayed({
//                notifyDataSetChanged()
//            },10000
//            )
//           // notifyDataSetChanged()
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
