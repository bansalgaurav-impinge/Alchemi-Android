package com.app.alchemi.views.fragments.onboarding.intro

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.alchemi.R
import com.app.alchemi.utils.ViewUtils
import kotlinx.android.synthetic.main.onboarding_layout.*
import kotlin.math.roundToInt


class OnBoardingTutorialPage private constructor(): Fragment() {

    companion object {
        fun newInstances(page: Int) = OnBoardingTutorialPage().apply {
            this.page = page
        }
    }

    private var page: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.onboarding_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = activity?.display
            display?.getRealMetrics(displayMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = activity?.windowManager?.defaultDisplay
            @Suppress("DEPRECATION")
            display?.getMetrics(displayMetrics)
        }


        var height = displayMetrics.heightPixels
        //1384
        if (height>=1800){
            imageCardFeature.layoutParams.height = ViewUtils.dpToPx(380)
        }else{
            imageCardFeature.layoutParams.height = ViewUtils.dpToPx(300)
        }


        when(page) {
            0 -> {
                ViewUtils.changeTextColor(tvCashbackBenefits, getString(R.string.upto_cashback_on_your_spending), ContextCompat.getColor(requireContext(), R.color.colorYellow), 6, 8, false)
                tvCashback.setText(R.string.your_metal_card_best_cashback_ever)
                imageCardFeature.setImageResource(R.drawable.feature_1)
            }
            1 -> {
                ViewUtils.changeTextColor(tvCashbackBenefits, getString(R.string.invite_and_share_with_friends_and_get_25aco_rewards), ContextCompat.getColor(requireContext(), R.color.colorYellow), 43, 46, true)
                tvCashback.setText(R.string.invite_and_share_with_friends_get_rewards)
                imageCardFeature.setImageResource(R.drawable.feature_2)
            }
            2 -> {
                ViewUtils.changeTextColor(tvCashbackBenefits, getString(R.string.with_alchemi_earn_upto_12_pa_on_alchemi_assets), ContextCompat.getColor(requireContext(), R.color.colorYellow), 23, 30, false)
                tvCashback.setText(R.string.get_more_interest_on_alchemi_earn)
                imageCardFeature.setImageResource(R.drawable.feature_3)
            }
            3 -> {
                ViewUtils.changeTextColor(tvCashbackBenefits, getString(R.string.the_best_place_to_buy_sell_pay_with_alchemi), ContextCompat.getColor(requireContext(), R.color.colorYellow), 40, 50, false)
                tvCashback.setText(R.string.your_alchemi_wallet_made_simple)
                imageCardFeature.setImageResource(R.drawable.feature_4)
            }
        }
    }

}