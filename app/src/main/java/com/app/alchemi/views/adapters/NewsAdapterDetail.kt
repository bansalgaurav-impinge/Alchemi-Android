package com.app.alchemi.views.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.models.TopNews
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.fragments.dashboardFeatures.NewsDetailFragment
import kotlinx.android.synthetic.main.news_card.view.*


internal class NewsAdapterDetail(
        newsList: List<TopNews>) : RecyclerView.Adapter<NewsAdapterDetail.ViewHolder>() {
    val newsList: List<TopNews> = newsList
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.news_card_detail, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.itemView.tvNewsTitle.text=newsList[position].title
        holder.itemView.tvNewsDescription.text=newsList[position].description
        holder.itemView.setOnClickListener {
            val bundle= Bundle()
            bundle.putSerializable(Constants.KEY_TITLE,newsList[position].title)
            bundle.putSerializable(Constants.KEY_DESCRIPTION,newsList[position].description)
            (mContext as HomeActivity).replaceFragment(NewsDetailFragment(),""+ NewsDetailFragment,bundle)
        }
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init { mContext = itemView.context
        }
    }
}
