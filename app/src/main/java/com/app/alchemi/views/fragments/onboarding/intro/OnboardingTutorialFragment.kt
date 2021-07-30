package com.app.alchemi.views.fragments.onboarding.intro

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.app.alchemi.R
import com.app.alchemi.utils.Target
import com.app.alchemi.utils.navigateTo
import com.app.alchemi.views.adapters.OnboardingTutorialPagerAdapter
import kotlinx.android.synthetic.main.onboarding_landing_layout.*


class OnboardingTutorialFragment private constructor() : Fragment() {
    var pageItem = 0
    companion object {
        fun newInstance() = OnboardingTutorialFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.onboarding_landing_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBoardingTutorialPager.isUserInputEnabled = true
        onBoardingTutorialPager.adapter = OnboardingTutorialPagerAdapter(this)
        onBoardingTutorialPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        onBoardingTutorialPager.adapter = OnboardingTutorialPagerAdapter(this)
        dotsIndicator.setViewPager2(onBoardingTutorialPager)

        /***
         * Skip button Click
         */
        tvSkip.setOnClickListener {
            navigateToMainScreen()
        }
        onBoardingTutorialPager.currentItem
        onBoardingTutorialPager.setPageTransformer { page, position ->
            pageItem=onBoardingTutorialPager.currentItem
        }

                /***
         *  Continue button click
         */
        tvContinue.setOnClickListener {
            when (pageItem) {
                0 -> {
                    selectPage(1)
                }
                1 -> {
                    selectPage(2)
                }
                2 -> {
                    selectPage(3)
                }
                3 -> {
                    navigateToMainScreen()
                }
                else -> {
                    navigateToMainScreen()
                }
            }
        }
    }


    private fun selectPage(pageIndex: Int) {
        onBoardingTutorialPager.currentItem = pageIndex
        activity?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
    }
    fun navigateToMainScreen(){
        val bundle= Bundle()
        navigateTo(context, Target.START,bundle)
       activity?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
        requireActivity().finish()
    }

}