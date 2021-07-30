package com.app.alchemi.views.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.alchemi.views.fragments.onboarding.intro.OnBoardingTutorialPage

private const val NUM_PAGES = 4

class OnboardingTutorialPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return OnBoardingTutorialPage.newInstances(position)
    }

}