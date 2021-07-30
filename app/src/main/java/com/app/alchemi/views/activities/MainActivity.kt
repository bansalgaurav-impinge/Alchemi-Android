package com.app.alchemi.views.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.Target
import com.app.alchemi.utils.navigateTo

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var referralCode=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // AlchemiApplication.alchemiApplication?.saveScreenIndex("4")

        if (AlchemiApplication.alchemiApplication?.getToken().toString().isNotEmpty() && AlchemiApplication.alchemiApplication?.getToken()!=null) {
            val data: Uri = intent?.data!!
            if (data.toString().contains(Constants.APP_URL)) {
                Constants.isEditProfile = true
                AlchemiApplication.alchemiApplication?.saveScreenIndex("2")
                finish()
                startActivity(Intent(this, HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT))
            }
        }else {
            try {
                if (AlchemiApplication.alchemiApplication?.getIndex()?.trim() != "" && AlchemiApplication.alchemiApplication?.getIndex()?.trim()!!.isNotEmpty() && AlchemiApplication.alchemiApplication?.getIndex()?.trim() != null) {
                    Constants.KEY_Index = AlchemiApplication.alchemiApplication?.getIndex().toString()
                    if (Constants.KEY_Index.toInt() > 0) {
                        navigateTo(this, Target.SIGN_UP_EMAIL)
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


            try {

                val data: Uri = intent?.data!!
                if (data.toString().contains("user=")) {
                    Constants.isSignIn = false
                    AlchemiApplication.alchemiApplication?.saveUserDetails(data.toString().split("user=")[1])

                } else if (data.toString().contains("jwt_token=")) {
                    Constants.isSignIn = true

                    AlchemiApplication.alchemiApplication?.saveUserToken(data.toString().split("jwt_token=")[1])

                } else {
                    Constants.isEditProfile = true

                }
                navigateUser()

            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (intent!=null&& intent.getStringExtra(Constants.KEY_TITLE)=="ReferralCodeActivity"){
                referralCode= ""+intent.getStringExtra(Constants.KEY_REFERRAL_CODE)
                navigateToSignUp()
            }
        }
        tvReferralCode.setOnClickListener {
        navigateTo(this,Target.REFERRAL_CODE)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)

        }
     //   eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjcxYjdjM2E0LWQ1MmQtNDk5Ni05N2M1LTRkNGM4YmViNjNkOCIsImVtYWlsIjoidXBwYWwuaGFycnkxMUBnbWFpbC5jb20iLCJqd3Rfc2VjcmV0IjoiZGNjYjI5N2ItMjNhYS00ZDBmLWE2ZDEtZTNiYjVmMzNmZGIzIn0.kjzcdo1w_xvAGsPPzTBqo2UC78-Cnqg8Wibr9Z1DwMw
        var str="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjcxYjdjM2E0LWQ1MmQtNDk5Ni05N2M1LTRkNGM4YmViNjNkOCIsImVtYWlsIjoidXBwYWwuaGFycnkxMUBnbWFpbC5jb20iLCJqd3Rfc2VjcmV0IjoiZGNjYjI5N2ItMjNhYS00ZDBmLWE2ZDEtZTNiYjVmMzNmZGIzIn0.kjzcdo1w_xvAGsPPzTBqo2UC78-Cnqg8Wibr9Z1DwMw"
        AlchemiApplication.alchemiApplication?.saveUserToken(str)
        AlchemiApplication.alchemiApplication?.saveUserDetails("71b7c3a4-d52d-4996-97c5-4d4c8beb63d8")
        tvSignUp.setOnClickListener {
            navigateToSignUp()

        }
        tvLogin.setOnClickListener {
            val bundle= Bundle()
            bundle.putString(Constants.KEY_REFERRAL_CODE,"")
            Constants.isSignIn=true
            AlchemiApplication.alchemiApplication?.clearAllData()
            AlchemiApplication.alchemiApplication?.saveScreenIndex("0")
            navigateTo(this,Target.SIGN_UP_EMAIL)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
        }

    }

    override fun onResume() {
        super.onResume()
       // Constants.isSignIn=false

    }
    fun navigateUser(){
        AlchemiApplication.alchemiApplication?.saveScreenIndex("2")
        navigateTo(this, Target.SIGN_UP_EMAIL)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
    }
    fun navigateToSignUp(){
        Constants.isSignIn=false
        Constants.KEY_Index="0"
        AlchemiApplication.alchemiApplication?.clearAllData()
        AlchemiApplication.alchemiApplication?.saveScreenIndex("0")
        val bundle= Bundle()
        bundle.putString(Constants.KEY_REFERRAL_CODE,referralCode)
        navigateTo(this,Target.SIGN_UP_EMAIL,bundle)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
    }


}