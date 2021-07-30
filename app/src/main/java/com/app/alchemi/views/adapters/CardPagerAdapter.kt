package com.app.alchemi.views.adapters

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.fragments.dashboardFeatures.ApplyingCardFragment
import kotlinx.android.synthetic.main.card_pager_layout.view.*

class CardPagerAdapter (private val context: Context, private val words: List<String>): RecyclerView.Adapter<CardPagerAdapter.PageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageHolder =
            PageHolder(LayoutInflater.from(context).inflate(R.layout.card_pager_layout, parent, false))

    override fun onBindViewHolder(holder: PageHolder, position: Int) {
      //  holder.itemView.textView.text = words[position]
        changeTextColor(holder.itemView.tvCashBack,"2% Back\u00B9",ContextCompat.getColor(context,R.color.colorGreen),0,2,1.3f)
        changeTextColor(holder.itemView.tvCashBack2,"100% \nBack½",ContextCompat.getColor(context,R.color.colorGreen),0,4,1.3f)
        changeTextColor(holder.itemView.tvCashBack3,"10% \nACO Stake Rewards¹",ContextCompat.getColor(context,R.color.colorGreen),0,3,1.3f)
        ViewUtils.changeTextColorSize(holder.itemView.tvNote,"\u2022 Notes: This offer is launched by Alchemi.com independently & there is no partnership between Alchemi.com & the merchants in this offer. Alchemi.com has the sole discretion to modify this offer at anytime.",ContextCompat.getColor(context,R.color.colorYellow),1,8,false)
        val str="Purchase & Hold 50,000 ACO token for 180 days to receive the Royal Indigo Card. Learn More"
        changeTextColor(holder.itemView.tvTokenPurchase,""+str,ContextCompat.getColor(context,R.color.colorYellow),str.length-11,str.length,1f)
        holder.itemView.tvName.text=words[position]+" "+context.getString(R.string.privileges)
        holder.itemView.tvSelect.text=context.getString(R.string.select)+" "+words[position]
        holder.itemView.tvSelect.setOnClickListener {
            val bundle= Bundle()
            bundle.putString(Constants.KEY_TITLE,words[position])
            (context as HomeActivity).replaceFragment(ApplyingCardFragment(),""+ApplyingCardFragment,bundle)
        }

    }

    override fun getItemCount(): Int = words.size

    inner class PageHolder(view: View) : RecyclerView.ViewHolder(view) {
      //  val textView: TextView = view.findViewById<TextView>(R.id.textView)
    }
    /***
     *  Change text color
     */
    fun changeTextColor(textView: TextView, text: String, color: Int, startLength: Int, endLength: Int,size: Float){
        val spannable = SpannableString(text)
        spannable.setSpan(RelativeSizeSpan(size), startLength, endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(color), startLength, endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannable
    }
}