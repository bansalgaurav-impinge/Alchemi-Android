package com.app.alchemi.views.adapters

import android.R.attr.button
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.models.Message
import kotlinx.android.synthetic.main.chat_card.view.*


internal class ContactSupportAdapter(
    val chatList: List<Message>
) : RecyclerView.Adapter<ContactSupportAdapter.ViewHolder>() {
//    val chatList: ArrayList<String> = chatList
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.chat_card, parent, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        if (chatList[position].message.isNotEmpty()){
            holder.itemView.chatLayoutSender.visibility=View.VISIBLE
            holder.itemView.tvMessageSender.text=chatList[position].message
//            holder.itemView.chatLayout.background=ContextCompat.getDrawable(mContext!!,
//                R.drawable.chat_sender_bubble)
//            holder.itemView.chatLayout.setHorizontalGravity(Gravity.END)
//            val params =  holder.itemView.chatLayout.getLayoutParams() as RelativeLayout.LayoutParams
//            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//
//
//            holder.itemView.chatLayout.setLayoutParams(params)
            //holder.itemView.chatLayout.gravity=
        }
        if (chatList[position].reply.isNotEmpty()){
            holder.itemView.chatLayout.visibility=View.VISIBLE
            holder.itemView.tvMessage.text=chatList[position].reply
//            holder.itemView.chatLayout.background=ContextCompat.getDrawable(mContext!!,
//                R.drawable.chat_sender_bubble)
//            holder.itemView.chatLayout.setHorizontalGravity(Gravity.END)
//            val params =  holder.itemView.chatLayout.getLayoutParams() as RelativeLayout.LayoutParams
//            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//
//
//            holder.itemView.chatLayout.setLayoutParams(params)
            //holder.itemView.chatLayout.gravity=
        }

//        //holder.itemView.tvMessage.gravity= Gravity.CENTER
//        if (position==0){
//            // val params= chatLayout.get
//            val params = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            ).apply {
//                weight = 1.0f
//                gravity = Gravity.END
//
//            }
//            holder.itemView.chatLayout.background= ContextCompat.getDrawable(
//                mContext!!,
//                R.drawable.chat_receiver_bubble
//            )
//
//        }else{
//            val params = RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT
//            ).apply {
//
//            }
//            holder.itemView.chatLayout.layoutParams = params
//           // holder.itemView.chatLayout.chatLayout.gravity=Gravity.END
//
//            val layoutParams = holder.itemView.rlParent.layoutParams
//            layoutParams.apply {RelativeLayout.ALIGN_PARENT_START  }
//            holder.itemView.chatLayout.layoutParams = params
//            holder.itemView.chatLayout.background=ContextCompat.getDrawable(
//                mContext!!,
//                R.drawable.chat_sender_bubble
//            )
//            holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation)
//        }
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


