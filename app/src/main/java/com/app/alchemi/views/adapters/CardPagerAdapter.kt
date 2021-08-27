package com.app.alchemi.views.adapters

import Constants
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.models.Card
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.fragments.dashboardFeatures.ApplyingCardFragment
import com.app.alchemi.views.fragments.dashboardFeatures.CardsFragment
import kotlinx.android.synthetic.main.card_pager_layout.view.*


class CardPagerAdapter(
    private val context: Context,
    private val cards: List<Card>?,
    private val cardsFragment: CardsFragment
): RecyclerView.Adapter<CardPagerAdapter.PageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageHolder =
            PageHolder(LayoutInflater.from(context).inflate(R.layout.card_pager_layout, parent, false))

    override fun onBindViewHolder(holder: PageHolder, position: Int) {
      //  holder.itemView.textView.text = words[position]
        changeTextColor(holder.itemView.tvCashBack,"2% Back\u00B9",ContextCompat.getColor(context,R.color.colorGreen),0,2,1.3f)
        changeTextColor(holder.itemView.tvCashBack2,"100% \nBack½",ContextCompat.getColor(context,R.color.colorGreen),0,4,1.3f)
        changeTextColor(holder.itemView.tvCashBack3,
            cards?.get(position)?.aco_stake_rewards+"\n"+context.getString(R.string.aco_stake_rewards)+"¹",ContextCompat.getColor(context,R.color.colorGreen),0,
            cards?.get(position)?.aco_stake_rewards.toString().length+1,1.3f)
//        changeTextColor(holder.itemView.tvCashBack3,
//            cards?.get(position)?.aco_stak_rewards+"\nACO Stake Rewards¹",ContextCompat.getColor(context,R.color.colorGreen),0,3,1.3f)
        ViewUtils.changeTextColorSize(holder.itemView.tvNote,"\u2022 Notes: This offer is launched by Alchemi.com independently & there is no partnership between Alchemi.com & the merchants in this offer. Alchemi.com has the sole discretion to modify this offer at anytime.",ContextCompat.getColor(context,R.color.colorYellow),1,8,false)
//        val str="Purchase & Hold 50,000 ACO token for 180 days to receive the Royal Indigo Card. Learn More"
//        changeTextColor(holder.itemView.tvTokenPurchase,""+str,ContextCompat.getColor(context,R.color.colorYellow),str.length-11,str.length,1f)
        holder.itemView.tvName.text= cards!![position].name +" "+context.getString(R.string.privileges)
        if (!cards[position].is_user_active) {
            holder.itemView.tvSelect.text =
                context.getString(R.string.select) + " " + cards[position].name
        }else{
            holder.itemView.tvSelect.text = context.getString(R.string.current_activated_card)
        }
        holder.itemView.tvDetail.text=cards[position].details
        val str=cards[position].aco_token_purchase+" Learn More"
        changeTextColor(holder.itemView.tvTokenPurchase,""+str,ContextCompat.getColor(context,R.color.colorYellow),str.length-11,str.length,1f)
       // holder.itemView.tvTokenPurchase.text=cards[position].aco_token_purchase
        ViewUtils.downloadImage(cards[position].card_image, holder.itemView.ivCard, context)
        if (cards[position].monthly_fee.toInt()>0){
            holder.itemView.tvMonthlyFee.text=context.getString(R.string.us_dollar,cards[position].monthly_fee.stripTrailingZeros().toPlainString())
        }
        if (cards[position].annual_fee.toInt()>0){
            holder.itemView.tvAnnualFee.text=context.getString(R.string.us_dollar,cards[position].annual_fee.stripTrailingZeros().toPlainString())
        }
        if (cards[position].delivery_fee.toInt()>0){
            holder.itemView.tvDeliveryFee.text=context.getString(R.string.us_dollar,cards[position].delivery_fee.stripTrailingZeros().toPlainString())
        }
        if (!cards[position].airport_lounge){
            holder.itemView.tvAirportLongue.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context,R.drawable.ic_cancel),null)

        }
        holder.itemView.switchTakingOptions.isChecked = cards[position].staking_status

        setValues(holder,position)
        holder.itemView.tvAmount.text=context.getString(R.string.us_dollar,cards[position].limits_fee.stripTrailingZeros().toPlainString())
        holder.itemView.tvTermsAndConditionsDetail.text=cards[position].terms_and_conditions


        holder.itemView.switchTakingOptions.setOnCheckedChangeListener { buttonView, isChecked ->
            (context as AppCompatActivity).runOnUiThread {
                //context.refreshInbox();
                cardsFragment.updateStakeStatus(isChecked,""+cards[position].id,position)
                setValues(holder,position)
            }

        }
        holder.itemView.tvSelect.setOnClickListener {
            val bundle= Bundle()
            bundle.putString(Constants.KEY_TITLE,cards[position].name)
            bundle.putString(Constants.KEY_DESCRIPTION, cards[position].is_user_active.toString())
            bundle.putString(Constants.KEY_IMAGE, cards[position].card_image)
            bundle.putString(Constants.KEY_CARD_ID, cards[position].id.toString())
            (context as HomeActivity).replaceFragment(ApplyingCardFragment(),""+ApplyingCardFragment,bundle)
        }

    }

    override fun getItemCount(): Int = cards?.size!!

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
    fun setValues(holder: PageHolder, position: Int) {
        holder.itemView.tvCardMaterial.text= cards!![position].material
         if (!cards[position].contactless_payment){
            holder.itemView.tvContactLessPayment.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context,R.drawable.ic_cancel),null)

        }
         if (!cards[position].is_pay_anywhere){
            holder.itemView.tvCardAcceptedAnywhere.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context,R.drawable.ic_cancel),null)

        }

        if(cards!![position].staking_status) {
            holder.itemView.tvCahsBackCard.text=cards[position].card_benefit_with_stake.card_cashback.stripTrailingZeros().toPlainString()+"%"
            changeTextColor(holder.itemView.tvCashBack,cards[position].card_benefit_with_stake.card_cashback.stripTrailingZeros().toPlainString()+"% Back\u00B9",ContextCompat.getColor(context,R.color.colorGreen),0,2,1.3f)
            changeTextColor(holder.itemView.tvCashBack2,"100% \nBack½",ContextCompat.getColor(context,R.color.colorGreen),0,4,1.3f)
            changeTextColor(holder.itemView.tvCashBack3,
                cards[position].aco_stake_rewards +"\n"+context.getString(R.string.aco_stake_rewards)+"¹",ContextCompat.getColor(context,R.color.colorGreen),0,cards.get(position).aco_stake_rewards.length+1,1.3f)
            if (!cards[position].card_benefit_with_stake.spotify_reimbursement){
                holder.itemView.tvSpotifyReimbursement.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context,R.drawable.ic_cancel),null)
                holder.itemView.tvSpotifyReimbursementDetail.text=cards[position].card_benefit_with_stake.spotify_reimbursement_detail
                holder.itemView.ivSpotify.visibility=View.GONE
            }
            if (!cards[position].card_benefit_with_stake.netflix_reimbursement){
                holder.itemView.tvNetFlixReimbursement.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context,R.drawable.ic_cancel),null)
                holder.itemView.tvNetFlixReimbursementDetail.text=cards[position].card_benefit_with_stake.netflix_reimbursement_detail
                holder.itemView.ivNetflix.visibility=View.GONE
            }
            if (!cards[position].card_benefit_with_stake.cro_stake_rewards){
                holder.itemView.tvACO.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context,R.drawable.ic_cancel),null)
                holder.itemView.tvACODetails.text=cards[position].card_benefit_with_stake.cro_stake_rewards_detail
            }else{
                holder.itemView.tvACO.setCompoundDrawablesWithIntrinsicBounds(null,null, null,null)
                holder.itemView.tvACO.text=cards[position].aco_stake_rewards
            }
        }else{
            holder.itemView.tvCahsBackCard.text=cards[position].card_benefit_with_out_stake.card_cashback.stripTrailingZeros().toPlainString()+"%"
            changeTextColor(holder.itemView.tvCashBack,cards[position].card_benefit_with_out_stake.card_cashback.stripTrailingZeros().toPlainString()+"% Back\u00B9",ContextCompat.getColor(context,R.color.colorGreen),0,2,1.3f)
            changeTextColor(holder.itemView.tvCashBack2,"100% \nBack½",ContextCompat.getColor(context,R.color.colorGreen),0,4,1.3f)
            changeTextColor(holder.itemView.tvCashBack3,
                cards[position].aco_stake_rewards +"\n"+context.getString(R.string.aco_stake_rewards)+"¹",ContextCompat.getColor(context,R.color.colorGreen),0,
                cards[position].aco_stake_rewards.length+1,1.3f)
            if (!cards[position].card_benefit_with_out_stake.spotify_reimbursement){
                holder.itemView.tvSpotifyReimbursement.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context,R.drawable.ic_cancel),null)
                holder.itemView.tvSpotifyReimbursementDetail.text=cards[position].card_benefit_with_out_stake.spotify_reimbursement_detail
                holder.itemView.ivSpotify.visibility=View.GONE
            }
            if (!cards[position].card_benefit_with_out_stake.netflix_reimbursement){
                holder.itemView.tvNetFlixReimbursement.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context,R.drawable.ic_cancel),null)
                holder.itemView.tvNetFlixReimbursementDetail.text=cards[position].card_benefit_with_out_stake.netflix_reimbursement_detail
                holder.itemView.ivNetflix.visibility=View.GONE
            }
            if (!cards[position].card_benefit_with_out_stake.cro_stake_rewards){
                holder.itemView.tvACO.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context,R.drawable.ic_cancel),null)
                holder.itemView.tvACODetails.text=cards[position].card_benefit_with_out_stake.cro_stake_rewards_detail
            }else{
                holder.itemView.tvACO.setCompoundDrawablesWithIntrinsicBounds(null,null, null,null)
                holder.itemView.tvACO.text=cards[position].aco_stake_rewards
            }
        }
        holder.itemView.tvLimitsDetails.text=cards[position].limits_detail
    }
}