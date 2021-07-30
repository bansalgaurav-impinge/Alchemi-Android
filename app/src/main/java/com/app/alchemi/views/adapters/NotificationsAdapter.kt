package com.app.alchemi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import kotlinx.android.synthetic.main.notifications_adapter_layout.view.*


internal class NotificationsAdapter(topGainersList: List<HashMap<String,String>>) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {
    val notificationList: List<HashMap<String,String>> = topGainersList
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.notifications_adapter_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.itemView.tvDate.text=notificationList[position][Constants.KEY_FIRST_NAME]
        holder.itemView.tvNotification.text=notificationList[position][Constants.KEY_LAST_NAME]
        holder.itemView.tvNotification.text= notificationList[position][Constants.KEY_LAST_NAME]?.let { HtmlCompat.fromHtml(it, 0) }
        holder.itemView.tvTime.text=notificationList[position][Constants.KEY_STATUS]
        if (position==1||position==2||position==4) {
            holder.itemView.tvDate.visibility = View.GONE
        }
        if (position==notificationList.size-1){
            holder.itemView.view.visibility=View.GONE
        }
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation)

    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init { mContext = itemView.context
        }
    }

}
