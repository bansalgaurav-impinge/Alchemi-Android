package com.app.alchemi.views.activities


import Constants

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.Target
import com.app.alchemi.utils.ViewUtils.Companion.hideKeyBoard
import com.app.alchemi.utils.navigateTo
import com.app.alchemi.views.adapters.ViewPagerAdapter
import com.app.alchemi.views.adapters.getTabIndicator
import com.app.alchemi.views.fragments.dashboardFeatures.*
import com.app.alchemi.views.fragments.signup.EmailVerificationFragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.tab_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.util.*


class HomeActivity : AppCompatActivity() {
    var tab_layout: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_layout)
        initView()
        if ( AlchemiApplication.alchemiApplication?.getIndex()?.trim()!="" && AlchemiApplication.alchemiApplication?.getIndex()?.trim()!!.isNotEmpty() && AlchemiApplication.alchemiApplication?.getIndex()?.trim()!=null) {
            Constants.KEY_Index = AlchemiApplication.alchemiApplication?.getIndex().toString()
           if (Constants.KEY_Index == "2") {
                if (Constants.isEditProfile) {
                    val bundle= Bundle()
                    replaceFragment(EmailVerificationFragment(),""+EmailVerificationFragment,bundle)
                }
            }
        }
        ivChat.setOnClickListener {
            val  bundle= Bundle()
        }
        ivChat.visibility = View.VISIBLE
        rlAcx.visibility = View.VISIBLE
        toolbar_title.text = getString(R.string.home)
        ivBack.isEnabled=true
        ivSettings.visibility=View.VISIBLE
        ivBack.visibility=View.GONE
        ivScan.visibility=View.GONE
        ivChart.visibility=View.GONE
        /***
         *  Button click listeners
         */
        ivSettings.setOnClickListener {
            val currentFragment = supportFragmentManager.fragments.last()
            val bundle = Bundle()
            replaceFragment(SettingsFragment(), "SettingsFragment", bundle)
        }
        rlAcx.setOnClickListener {
            val bundle = Bundle()
            replaceFragment(ACXPayFragment(), ""+ACXPayFragment, bundle)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
        }
        ivNotification.setOnClickListener {
            navigateTo(this, Target.NOTIFICATIONS)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
        }
        ivCrop.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.KEY_TITLE,"1")
            navigateTo(this, Target.ACX_PAY)
//            replaceFragment(QRCodeFragment(), ""+QRCodeFragment, bundle)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
        }


    }

    private fun initView() {
        tab_layout = findViewById(R.id.tab_layout)
        tab_layout!!.setTabRippleColorResource(android.R.color.transparent)
        tab_layout!!.setupWithViewPager(viewpager)
        clToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightBlack))
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightBlack))
        ivChat.visibility= View.VISIBLE
        YoYo.with(Techniques.BounceInDown).duration(300).playOn(ivChat)
        YoYo.with(Techniques.ZoomIn).duration(300).playOn(ivSettings)
        rlAcx.visibility= View.VISIBLE
        toolbar_title.text  = getString(R.string.home)
      //  ivBack.setImageResource(R.drawable.setting)
        setupViewPager(viewpager)
        setupTabIcons()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        Constants.isSignIn=false
        resetToolbarView()


        tab_layout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                (tab?.customView?.findViewById(R.id.tvText) as TextView).setTextColor(ContextCompat.getColor(this@HomeActivity,R.color.colorYellow))
                Constants.selectedTab=tab.position
                hideKeyBoard(this@HomeActivity)
                if (tab.position == 0) {
                    hideView(0)
                    ivChat.visibility = View.VISIBLE
                    rlAcx.visibility = View.VISIBLE
                    toolbar_title.text = getString(R.string.home)
                    ivSettings.visibility=View.VISIBLE
                    ivBack.visibility=View.GONE
                    ivScan.visibility=View.GONE
                    ivChart.visibility=View.GONE
                    YoYo.with(Techniques.BounceInDown).duration(300).playOn(ivChat)
                    YoYo.with(Techniques.ZoomIn).duration(300).playOn(ivSettings)
                    resetToolbarView()

                } else if (tab.position == 1) {
                    hideView(1)
                 //   ivBack.isEnabled=false
                    toolbar_title.text = getString(R.string.accounts)
                   // ivBack.setImageResource(R.drawable.chart)
                    ivChat.visibility = View.GONE
                    rlAcx.visibility = View.GONE
                    ivSettings.visibility=View.GONE
                    ivBack.visibility=View.GONE
                    ivScan.visibility=View.VISIBLE
                    ivChart.visibility=View.VISIBLE
                    resetToolbarView()
                    YoYo.with(Techniques.BounceInDown)
                            .duration(300)
                            .playOn(ivScan)
                    YoYo.with(Techniques.ZoomIn).duration(300).playOn(ivChat)
                } else if (tab.position == 2) {
                    hideView(2)
                   // ivBack.isEnabled=false
                    toolbar_title.text = getString(R.string.acx)
                    ivBack.setImageResource(R.drawable.ic_back)
                    ivChat.visibility = View.GONE
                    rlAcx.visibility = View.GONE
                    ivSettings.visibility=View.GONE
                    ivBack.visibility=View.VISIBLE
                    ivScan.visibility=View.GONE
                    ivChart.visibility=View.GONE
                    resetToolbarView()
                } else if (tab.position == 3) {
                    hideView(3)
                   // ivBack.isEnabled=false
                    toolbar_title.text = getString(R.string.track)
                    ivBack.setImageResource(R.drawable.ic_back)
                    ivChat.visibility = View.GONE
                    rlAcx.visibility = View.GONE
                    ivSettings.visibility=View.GONE
                    ivBack.visibility=View.VISIBLE
                    ivScan.visibility=View.GONE
                    ivChart.visibility=View.GONE
                    changeToolbarViewTrackCoin(3)
                } else if (tab.position == 4) {
                    hideView(4)
                   // ivBack.isEnabled=false
                    toolbar_title.text = getString(R.string.card_details)
                    ivBack.setImageResource(R.drawable.ic_back)
                    ivChat.visibility = View.GONE
                    rlAcx.visibility = View.GONE
                    ivSettings.visibility=View.GONE
                    ivBack.visibility=View.VISIBLE
                    ivScan.visibility=View.GONE
                    ivChart.visibility=View.GONE
                    changeToolbarViewTrackCoin(4)
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                (tab?.customView?.findViewById(R.id.tvText) as TextView).setTextColor(ContextCompat.getColor(this@HomeActivity,R.color.colorGrey))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                if (tab.position==0){
                    clearStack(1)
                }
                else if (tab.position==1){
                    clearStack(1)
                }

                else if (tab.position==2){
                    clearStack(1)
                }
                else if (tab.position==3){
                    clearStack(1)
                }
                else if (tab.position==4){
                    clearStack(1)
                }

            }
        })

    }

    /***
     * Pager setup
     */
    fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(HomeFragment.newInstance(), "Home")
        adapter.addFrag(AccountsFragment.newInstance(), "Accounts")
        adapter.addFrag(ACXFragment.newInstance(), "ACX")
        adapter.addFrag(TrackFragment.newInstance(), "Track")
        adapter.addFrag(CardsFragment.newInstance(), "Card")
        viewPager.adapter = adapter
    }

    fun setupTabIcons() {
        tab_layout!!.getTabAt(0)!!.customView =
            getTabIndicator(
                    tab_layout!!.context,
                    R.drawable.home_tab_bg,
                    getString(R.string.home)
            )
        tab_layout!!.getTabAt(1)!!.customView =
            getTabIndicator(tab_layout!!.context, R.drawable.accounts_tab_bg, getString(R.string.accounts))
        tab_layout!!.getTabAt(2)!!.customView =
            getTabIndicator(
                    tab_layout!!.context,
                    R.drawable.acx, ""
            )
        tab_layout!!.getTabAt(3)!!.customView =
            getTabIndicator(
                    tab_layout!!.context,
                    R.drawable.track_tab_bg, getString(R.string.track)
            )
        tab_layout!!.getTabAt(4)!!.customView =
            getTabIndicator(
                    tab_layout!!.context,
                    R.drawable.card_tab_bg, getString(R.string.card)
            )


    }

    /**
     *  Replace fragments
     */
     fun replaceFragment(fragment: Fragment, tag: String, bundle: Bundle) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
            .addToBackStack(tag)
            .commit()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

     fun selectPage(pageIndex: Int) {
        viewpager.currentItem = pageIndex
       overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
    }
    fun navigateToMainScreen(){
        val bundle= Bundle()
        navigateTo(this, Target.START, bundle)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
       finish()
    }

    fun clearStack( index:Int){
        val count = supportFragmentManager.backStackEntryCount
        Log.e("test>>>",">>>"+supportFragmentManager.fragments.last()+" +==="+count)
        for (i in 0 until index) {
            supportFragmentManager.popBackStack()
        }

    }

    /**
     *
     *  Change tool bar view
     */
    fun changeToolbarViewTrackCoin(index:Int){
    toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorYellow))
    clToolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorYellow))
    toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorYellow))
    ivBack.setColorFilter(ContextCompat.getColor(this, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY)
    toolbar_title.setTextColor(ContextCompat.getColor(this,R.color.black))
    if (index==3) {
        ivNotification.visibility = View.VISIBLE
        ivCrop.visibility = View.VISIBLE
    }else{
        ivNotification.visibility = View.GONE
        ivCrop.visibility = View.GONE
    }
    YoYo.with(Techniques.Shake)
            .duration(500)
            .playOn(ivNotification)
}

    /**
     *
     *  Reset View
     */
    fun resetToolbarView(){
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorLightBlack))
        appbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorLightBlack))
        ivBack.setColorFilter(ContextCompat.getColor(this, R.color.colorGrey), android.graphics.PorterDuff.Mode.MULTIPLY)
        toolbar_title.setTextColor(ContextCompat.getColor(this,R.color.colorGrey))
        ivNotification.visibility=View.GONE
        ivCrop.visibility=View.GONE

    }

    /**
     * Hide View
     */
    fun hideView(postiton: Int){
        if (supportFragmentManager.fragments.last().toString().contains("CoinDetailFragment")||supportFragmentManager.fragments.last().toString().contains("AccountAllocationFragment")||supportFragmentManager.fragments.last().toString().contains("EarnFragment")||supportFragmentManager.fragments.last().toString().contains("ApplyingCardFragment")) {
            onBackPressed()
            selectPage(postiton)
        }else if (supportFragmentManager.fragments.last().toString().contains("FlexibleAssetsFragment")||supportFragmentManager.fragments.last().toString().contains("TermAssetsFragment")){
           clearStack(1)
            onBackPressed()
            selectPage(postiton)
        }
    }

}



