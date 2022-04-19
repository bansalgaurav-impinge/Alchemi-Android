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

        }
        if (chatList[position].reply.isNotEmpty()){
            holder.itemView.chatLayout.visibility=View.VISIBLE
            holder.itemView.tvMessage.text=chatList[position].reply

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


