package com.app.alchemi.views.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.FragmentFocusListener
import com.app.alchemi.utils.FragmentNavigation
import com.app.alchemi.views.fragments.signup.*
import kotlinx.android.synthetic.main.toolbar_layout_11.*


class SignUpActivity: AppCompatActivity(),FragmentFocusListener {
    private var isBackNavigationDisabled = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_activity)
        showSignupEmailScreen()
        if (Constants.isSignIn){
            toolbar_title.text = getString(R.string.signIn)
        }else{
            toolbar_title.text = getString(R.string.signup)
        }

        ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    /***
     * Display signup email screen
     */
    private fun showSignupEmailScreen() {
        if ( AlchemiApplication.alchemiApplication?.getIndex()?.trim()!="" && AlchemiApplication.alchemiApplication?.getIndex()?.trim()!!.isNotEmpty() && AlchemiApplication.alchemiApplication?.getIndex()?.trim()!=null) {
            Constants.KEY_Index = AlchemiApplication.alchemiApplication?.getIndex().toString()
            if (Constants.KEY_Index=="1"){
                val fragment = EmailConfirmationFragment.newInstance()
                FragmentNavigation
                        .Builder(supportFragmentManager, R.id.fragmentContainer, fragment)
                        .execute()
            }else if (Constants.KEY_Index=="2"){
                if (Constants.isEditProfile){
                    val fragment = EmailVerificationFragment.newInstance()
                    FragmentNavigation
                            .Builder(supportFragmentManager, R.id.fragmentContainer, fragment)
                            .execute()
                  //  finish()
                }else {
                    val fragment = EmailVerificationFragment.newInstance()
                    FragmentNavigation
                            .Builder(supportFragmentManager, R.id.fragmentContainer, fragment)
                            .execute()
                }
            }
            else if (Constants.KEY_Index=="3"){
                val fragment = PhoneNumberFragment.newInstance()
                FragmentNavigation
                        .Builder(supportFragmentManager, R.id.fragmentContainer, fragment)
                        .execute()
            }
            else if (Constants.KEY_Index=="4"){
                val fragment = VerifyYourIdentity.newInstance()
                FragmentNavigation
                        .Builder(supportFragmentManager, R.id.fragmentContainer, fragment)
                        .execute()
            }
            else if (Constants.KEY_Index=="5") {
                val fragment = UploadDocumentFragment.newInstance()
                FragmentNavigation
                    .Builder(supportFragmentManager, R.id.fragmentContainer, fragment)
                    .execute()
            }
            else if (Constants.KEY_Index=="6") {
                val fragment = ThanksFragment.newInstance()
                FragmentNavigation
                    .Builder(supportFragmentManager, R.id.fragmentContainer, fragment)
                    .execute()
            } else{
                navigateToSignUpEmail()
            }
        }else{
          navigateToSignUpEmail()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            item.itemId==android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Back Pressed
     */
    override fun onBackPressed() {
        super.onBackPressed()
        val fragmentList: List<*> = supportFragmentManager.fragments
        if(fragmentList.isNotEmpty()){
        if (fragmentList.size>0){
            isBackNavigationDisabled=true

        if (isBackNavigationDisabled) {

               // supportFragmentManager.popBackStack()
                overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_out_right)
            }
            // show dialog
        }
        } else {
            super.onBackPressed()
        }

        overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_out_right)
    }

    override fun backDisabledFragmentAttached() {
        this.isBackNavigationDisabled = true
    }

    override fun backDisabledFragmentDetached() {
       // this.isBackNavigationDisabled = false
    }

    /***
     *  Navigate next screen
     */
  fun navigateToSignUpEmail(){
     var referralCode=""
     if (intent!=null&& intent.getStringExtra(Constants.KEY_REFERRAL_CODE)!=""){
         referralCode= ""+intent.getStringExtra(Constants.KEY_REFERRAL_CODE)
     }
     val fragment = SignUpEmailFragment.newInstance()
     val bundle= Bundle()
     bundle.putString(Constants.KEY_REFERRAL_CODE,""+referralCode)
     fragment.arguments=bundle
     FragmentNavigation
             .Builder(supportFragmentManager, R.id.fragmentContainer, fragment)
             .execute()
 }


}