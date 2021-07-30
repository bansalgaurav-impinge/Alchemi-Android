package com.app.alchemi.views.fragments.onboarding.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.alchemi.R
import com.app.alchemi.utils.FragmentNavigation

class OnBoardLandingFragment private constructor(): Fragment() {

    companion object {
        fun newInstance() = OnBoardLandingFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.onboarding_landing_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBoardingScreens()

    }
    /***
     *  show Onboarding screens
     */
    fun onBoardingScreens(){
        FragmentNavigation
            .Builder(activity?.supportFragmentManager, R.id.fragmentContainer, OnboardingTutorialFragment.newInstance())
            //.addToBackstack()
            .execute()
    }

}