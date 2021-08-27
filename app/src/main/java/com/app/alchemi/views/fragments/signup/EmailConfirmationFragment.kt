package com.app.alchemi.views.fragments.signup

import Constants
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.Target
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.utils.navigateTo
import com.app.alchemi.viewModel.EmailConfirmationRequestViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.email_confirmation_layout.*
import kotlinx.android.synthetic.main.email_confirmation_layout.llBottoms
import kotlinx.android.synthetic.main.toolbar_layout.*


class EmailConfirmationFragment: Fragment() {
    internal lateinit var emailConfirmationRequestViewModel: EmailConfirmationRequestViewModel
    var fcmToken=""

    companion object {
        fun newInstance() = EmailConfirmationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.email_confirmation_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val aninSlideRight = AnimationUtils.loadAnimation(context,
                R.anim.slide_in_left)
        // assigning that animation to
        // the image and start animation
        ll_layout.startAnimation(aninSlideRight)
        llBottoms.startAnimation(aninSlideRight)

        emailConfirmationRequestViewModel = ViewModelProvider(this).get(EmailConfirmationRequestViewModel::class.java)
        /**
         * listener
         */
        tvOpenEmail.setOnClickListener {

            openEmail()
        }
        tvResend.setOnClickListener {
            resendEmail()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            if ( AlchemiApplication.alchemiApplication?.getEmail()?.trim()!="" && AlchemiApplication.alchemiApplication?.getEmail()?.trim()!!.isNotEmpty() && AlchemiApplication.alchemiApplication?.getEmail()?.trim()!=null){
                Constants.EMAIL= AlchemiApplication.alchemiApplication?.getEmail().toString()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        tvEmail.text = AlchemiApplication.alchemiApplication?.getEmail().toString()

        if (Constants.isSignIn){
            requireActivity().toolbar_title.text  = getString(R.string.signIn)

        }else if(Constants.isEditProfile){
            requireActivity().toolbar_title.text  = getString(R.string.edit_profile)
        }
        else{
            requireActivity().toolbar_title.text  = getString(R.string.signup)
        }

    }

    override fun onResume() {
        super.onResume()
        Constants.EMAIL= AlchemiApplication.alchemiApplication?.getEmail().toString()
        firebaseToken()
    }

    /*****
     * Method to open email account from app
     */
    fun openEmail(){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://mail.google.com"));
        requireActivity().startActivity(browserIntent);

    }

    /**
     * resent email
     */
    fun resendEmail(){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(cl_parent,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else if (fcmToken.isEmpty()){
            firebaseToken()
        }
        else {
            ViewUtils.showProgress(requireContext())
            ViewUtils.showProgress(requireContext())
            val hashMap= HashMap<String,String>()
            hashMap[Constants.KEY_EMAIL]=Constants.EMAIL
            hashMap[Constants.KEY_DEVICE_ID]=""+Constants.getDeviceID(requireContext())
            hashMap[Constants.KEY_DEVICE_TOKEN]=""+fcmToken
            hashMap[Constants.KEY_DEVICE_TYPE]=Constants.DEVICE_NAME
            hashMap[Constants.KEY_REDIRECT_URL]=Constants.APP_URL.trim()
            emailConfirmationRequestViewModel.getEmailConfimationRequest(hashMap, view)!!.observe(viewLifecycleOwner, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                val msg =emailConfirmationRequestModel.message
                if (emailConfirmationRequestModel.code==Constants.CODE_200){
                    ViewUtils.showSnackBar(cl_parent,""+msg)

                }else{
                    ViewUtils.showSnackBar(cl_parent,""+msg)
                }

            })
        }
    }

    /***
     *  Firebase token
     */
    fun firebaseToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e( "FCM Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            fcmToken=task.result
            // Log and toast
            Log.d("FCM Token", task.result)

        })
    }

}