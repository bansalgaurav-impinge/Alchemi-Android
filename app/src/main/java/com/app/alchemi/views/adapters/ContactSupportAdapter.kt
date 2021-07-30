package com.app.alchemi.views.adapters

import Constants
import android.R.attr.button
import android.R.attr.layout_gravity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import kotlinx.android.synthetic.main.chat_card.view.*


internal class ContactSupportAdapter(
    chatList: ArrayList<String>
) : RecyclerView.Adapter<ContactSupportAdapter.ViewHolder>() {
    val chatList: ArrayList<String> = chatList
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.chat_card, parent, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.itemView.tvMessage.text=chatList[position]
        //holder.itemView.tvMessage.gravity= Gravity.CENTER
        if (position==0){
            // val params= chatLayout.get
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1.0f
                gravity = Gravity.END

            }
            holder.itemView.chatLayout.background= ContextCompat.getDrawable(
                mContext!!,
                R.drawable.chat_receiver_bubble
            )

        }else{
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {

            }
            holder.itemView.chatLayout.layoutParams = params
           // holder.itemView.chatLayout.chatLayout.gravity=Gravity.END

            val layoutParams = holder.itemView.rlParent.layoutParams
            layoutParams.apply {RelativeLayout.ALIGN_PARENT_END  }
            holder.itemView.chatLayout.layoutParams = params
            holder.itemView.chatLayout.background=ContextCompat.getDrawable(
                mContext!!,
                R.drawable.chat_sender_bubble
            )
            holder.itemView.animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation)
        }
        holder.itemView.setOnClickListener {
            notifyDataSetChanged()

        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init { mContext = itemView.context
        }
    }
}


