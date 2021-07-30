package com.app.alchemi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView
.*
import com.app.alchemi.R
import com.app.alchemi.interfaceintercation.OnItemClickListener
import com.app.alchemi.views.fragments.signup.VerifyYourIdentity
import kotlinx.android.synthetic.main.user_verify_card.view.*


internal class UserVerifyAdapter(
    optionList: Array<String>, imageList: Array<Int>,
    verifyYourIdentity: VerifyYourIdentity) : RecyclerView.Adapter<UserVerifyAdapter.ViewHolder>() {
    val optionList: Array<String> = optionList
    val imageList: Array<Int> = imageList
    private var mContext: Context? = null
    var onItemClickListener: OnItemClickListener =verifyYourIdentity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_verify_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.itemView.tvOption.text=optionList[position]
        holder.itemView.ivIcon.setImageResource(imageList[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.getItemClicked(""+position)
        }
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init { mContext = itemView.context
        }
    }
}
