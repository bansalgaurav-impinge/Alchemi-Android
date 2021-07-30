package com.app.alchemi.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.alchemi.R
import com.app.alchemi.utils.FragmentNavigation
import com.app.alchemi.views.fragments.onboarding.intro.OnBoardLandingFragment

class OnBoardingActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_activity_layout)
        showFirstScreen()

    }

    /**
     * Show the initial screen of the onboarding process
     */
    private fun showFirstScreen() {
        val fragment = OnBoardLandingFragment.newInstance()

        FragmentNavigation
                .Builder(supportFragmentManager, R.id.fragmentContainer, fragment)
            .addToBackstack()
            .execute()

    }

}