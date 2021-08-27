package com.app.alchemi.views.activities

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.CheckUserPinViewModel
import kotlinx.android.synthetic.main.enter_passcode_fragment_layout.*

class EnterPasscodeActivity : AppCompatActivity() {
    private lateinit var checkUserPinViewModel: CheckUserPinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enter_passcode_fragment_layout)
        ViewUtils.showAnimation(this,llBottoms, R.anim.slide_in_left)
        checkUserPinViewModel = ViewModelProvider(this).get(CheckUserPinViewModel::class.java)

        etPin.transformationMethod = HideReturnsTransformationMethod.getInstance()
        ivShow.setImageResource(R.drawable.ic_hide)
        tvShow.text = getString(R.string.hide)
        /***
         *  Click to Check Validations and hit API
         */
        tvEnter.setOnClickListener {
            checkvalidationsAndCreatPin()
        }
        llPassword.setOnClickListener {
            ShowHidePass(llPassword)
        }
        tvForgot.setOnClickListener {
            forgotPassCode()
        }

    }



    /***
     *  method to check validation and call api
     */
    fun checkvalidationsAndCreatPin(){
        ViewUtils.hideKeyBoard(this)
        val pin = etPin.text.toString().trim()
        if (pin.isEmpty()) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.enter_your_create_pin))
        }else if (pin.length<6){
            ViewUtils.showSnackBar(scrollView,getString(R.string.pin_should_be_six_digits))
        }
        else if (!ViewUtils.verifyAvailableNetwork(this)) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(this)
            val hashMap= HashMap<String,String>()
            hashMap[Constants.KEY_PIN]=pin.trim()
            checkUserPinViewModel.verifyUserPin(hashMap, scrollView,Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(this, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code==Constants.CODE_200){
                  val  intentMain = Intent(this, HomeActivity::class.java)
                    intentMain.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intentMain)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
                    finish()
                }else{
                    val msg =getString(R.string.incorrect_pin)
                    ViewUtils.showSnackBar(scrollView,""+msg)
                }

            })
        }
    }

    /**
     *  APT to get Forgot Passcode API
     *
     */
    fun forgotPassCode(){
        ViewUtils.hideKeyBoard(this)
        if (!ViewUtils.verifyAvailableNetwork(this)) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(this)
            val hashMap= HashMap<String,String>()
            hashMap[Constants.KEY_USER]= AlchemiApplication.alchemiApplication?.getUUID().toString()
            checkUserPinViewModel.forgotPassCode(hashMap, scrollView,Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(this, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code==Constants.CODE_200){
                    ViewUtils.showSnackBar(scrollView,"Please check your registered Email for PassCode.")
                }else{
                    val msg =getString(R.string.incorrect_pin)
                    ViewUtils.showSnackBar(scrollView,""+msg)
                }

            })
        }
    }

    /***
     * Method to manage password view
     *
     */
    fun ShowHidePass(view:View){

        if(view.id ==R.id.llPassword){

            if(etPin.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
               ivShow.setImageResource(R.drawable.ic_hide)
                tvShow.text = getString(R.string.hide)

                //Show Password
                etPin.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else{
                ivShow.setImageResource(R.drawable.ic_show)
                tvShow.text = getString(R.string.show)

                //Hide Password
                etPin.transformationMethod = PasswordTransformationMethod.getInstance()

            }
        }
    }

}