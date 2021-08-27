package com.app.alchemi.views.activities

import Constants
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.viewModel.UserDetailViewModel
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity: AppCompatActivity() {
    // Splash screen timer
    private val SPLASH_TIME_OUT = 3000L
    private val sharedPrefFile = "sharedpreference"
    internal lateinit var userDetailViewModel: UserDetailViewModel
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       setContentView(R.layout.activity_splash)
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        val animFadeIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        userDetailViewModel = ViewModelProvider(this).get(UserDetailViewModel::class.java)
       imageAppLogo.startAnimation(animFadeIn)
        val animfadeout = AnimationUtils.loadAnimation(this,
                R.anim.slide_in_right)
        imageAppLogoText.startAnimation(animfadeout)

        landingScreen()

    }


   fun landingScreen(){
       val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
       var intentMain = Intent(this, OnBoardingActivity::class.java)
       val sharedIdValue = sharedPreferences.getBoolean("intro",false)

       if (sharedIdValue){
           if (AlchemiApplication.alchemiApplication?.getToken().toString().isNotEmpty() && AlchemiApplication.alchemiApplication?.getToken()!=null) {
               if (AlchemiApplication.alchemiApplication?.getPassCodeSettings().toString().isEmpty() || AlchemiApplication.alchemiApplication?.getPassCodeSettings()==null) {
                   Handler(Looper.getMainLooper()).postDelayed({
                   if (getUserDetail()){
                       intentMain = Intent(this, EnterPasscodeActivity::class.java)
                   }else{
                       intentMain = Intent(this, HomeActivity::class.java)
                   }},100
                       )
               }   else if (AlchemiApplication.alchemiApplication?.getPassCodeSettings().toString().isNotEmpty() && AlchemiApplication.alchemiApplication?.getPassCodeSettings()!=null && AlchemiApplication.alchemiApplication?.getPassCodeSettings().toBoolean()) {
                   intentMain = Intent(this, EnterPasscodeActivity::class.java)
               }
               else{
                   intentMain = Intent(this, HomeActivity::class.java)
               }

           }else{
               intentMain = Intent(this, MainActivity::class.java)
           }
       }else{
           val editor:SharedPreferences.Editor =  sharedPreferences.edit()
           editor.putBoolean("intro",true)
           editor.apply()
           editor.commit()
           intentMain = Intent(this, OnBoardingActivity::class.java)

       }

        Handler(Looper.getMainLooper()).postDelayed({
            intentMain.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intentMain)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
            finish()


        }, SPLASH_TIME_OUT)
    }
    fun getUserDetail():Boolean{
        var lockScreenEnable=false
            userDetailViewModel.getUserDetail(Constants.Token+ AlchemiApplication.alchemiApplication?.getToken(),cl_layout)!!.observe(this, Observer { userDetailModel ->
                if (userDetailModel.code==Constants.CODE_200) {
                    if (userDetailModel.data.pin!=null &&userDetailModel.data.pin!="null") {
                        AlchemiApplication.alchemiApplication?.savePasscode(userDetailModel.data.pin)
                        AlchemiApplication.alchemiApplication?.savePassCodeSettings("" + userDetailModel.data.pin_status)
                            if (userDetailModel.data.pin_status) {
                              lockScreenEnable = true
                            }
                    }
                }

            })
            return lockScreenEnable
    }
}