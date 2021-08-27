package com.app.alchemi.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.Target
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.utils.navigateTo
import com.app.alchemi.viewModel.CheckUserPinViewModel
import kotlinx.android.synthetic.main.referral_code_activity.*
import kotlinx.android.synthetic.main.toolbar_layout_11.*

class ReferralCodeActivity: AppCompatActivity() {
    private lateinit var checkUserPinViewModel: CheckUserPinViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.referral_code_activity)
        toolbar_title.text = getString(R.string.refer_code)
        checkUserPinViewModel = ViewModelProvider(this).get(CheckUserPinViewModel::class.java)

        ViewUtils.showAnimation(this,etReferralCode, R.anim.slide_in_right)
        ivBack.setOnClickListener {
            onBackPressed()
        }
        tvVerifyCode.setOnClickListener {
            checkReferralCode()

        }
       // etReferralCode.setText("eb63d8")

    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_out_right)
    }

    /***
     *  Hit web service to check referral code
     */
    fun checkReferralCode() {
        ViewUtils.hideKeyBoard(this)
        val referralCode = etReferralCode.text.toString().trim()

        if (referralCode.isEmpty()) {
            ViewUtils.showSnackBar(ll_layout, getString(R.string.enter_your_referal_code))
        }
        else if (referralCode.length<6) {
            ViewUtils.showSnackBar(ll_layout, getString(R.string.enter_valid_referral_code))
        }
        else if (!ViewUtils.verifyAvailableNetwork(this)) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(this)
            val hashMap= HashMap<String,String>()
            //hashMap[Constants.KEY_USER]="71b7c3a4-d52d-4996-97c5-4d4c8beb63d8"
            hashMap[Constants.KEY_REFERRAL_CODE]=referralCode
            checkUserPinViewModel.verifyReferralCode(hashMap, scrollView,Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(this, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code==Constants.CODE_200){
                    val bundle= Bundle()
                    bundle.putString(Constants.KEY_TITLE,this.javaClass.simpleName)
                    bundle.putString(Constants.KEY_REFERRAL_CODE,referralCode)
                    navigateTo(this, Target.START,bundle,Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    finish()
                }else{
                    ViewUtils.showSnackBar(scrollView,""+emailConfirmationRequestModel.message)
                }

            })
        }
    }
}