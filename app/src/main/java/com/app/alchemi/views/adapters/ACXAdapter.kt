package com.app.alchemi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.views.activities.HomeActivity
import kotlinx.android.synthetic.main.acx_view.view.*


internal class ACXAdapter(
        topGainersList: List<HashMap<String,String>>) : RecyclerView.Adapter<ACXAdapter.ViewHolder>() {
    val topGainersList: List<HashMap<String,String>> = topGainersList
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.acx_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.itemView.tvName.text=topGainersList[position][Constants.KEY_FIRST_NAME]
        if (position==0){
            holder.itemView.tvUpcoming.visibility=View.GONE
            holder.itemView.ivArrow.visibility=View.VISIBLE
            holder.itemView.ivIcon.setImageResource(R.drawable.account_white)
            holder.itemView.rlParent.background=ContextCompat.getDrawable(mContext!!,R.drawable.allocation_card_bg)
            holder.itemView.ivIcon.setColorFilter(ContextCompat.getColor(mContext!!,R.color.white),android.graphics.PorterDuff.Mode.MULTIPLY)
        }else if (position==1){
            holder.itemView.tvUpcoming.visibility=View.GONE
            holder.itemView.ivArrow.visibility=View.VISIBLE
            holder.itemView.ivIcon.setImageResource(R.drawable.track_white)
            holder.itemView.ivIcon.setColorFilter(ContextCompat.getColor(mContext!!,R.color.white),android.graphics.PorterDuff.Mode.MULTIPLY)
        }else{
            holder.itemView.ivArrow.visibility=View.GONE
            holder.itemView.tvUpcoming.visibility=View.VISIBLE
            holder.itemView.ivIcon.setImageResource(R.drawable.dollar)
        }
        if (position==topGainersList.size-1){
            holder.itemView.rlParent.background=ContextCompat.getDrawable(mContext!!,R.drawable.allocation_card_bg_bottom)
            holder.itemView.divider.visibility=View.GONE
        }
        holder.itemView.setOnClickListener {
            if (position==0){
                (mContext as HomeActivity).selectPage(1)
            }else if (position==1){
                (mContext as HomeActivity).selectPage(3)
            }
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
