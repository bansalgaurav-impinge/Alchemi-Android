package com.app.alchemi.views.fragments.signup

import `in`.aabhasjindal.otptextview.OTPListener
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
import com.app.alchemi.utils.FragmentNavigation
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.PhoneConfirmationRequestViewModel
import com.app.alchemi.viewModel.VerifyOTPRequestViewModel
import com.app.alchemi.views.activities.HomeActivity
import kotlinx.android.synthetic.main.otp_fragment_layout.*
import kotlinx.android.synthetic.main.otp_fragment_layout.llBottoms
import kotlinx.android.synthetic.main.otp_fragment_layout.llTop
import kotlinx.android.synthetic.main.otp_fragment_layout.scrollview
import kotlinx.android.synthetic.main.otp_fragment_layout.tvContinue
import kotlinx.android.synthetic.main.sign_up_fragment_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class VerifyOTPFragment :Fragment() {
    var otpNumber=""
    internal lateinit var verifyOTPRequestViewModel: VerifyOTPRequestViewModel
    internal lateinit var phoneConfirmationRequestViewModel: PhoneConfirmationRequestViewModel
    companion object {
        fun newInstance() = VerifyOTPFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.otp_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val aninSlideRight = AnimationUtils.loadAnimation(context,
            R.anim.slide_in_left)
        llTop.startAnimation(aninSlideRight)
        llBottoms.startAnimation(aninSlideRight)

        verifyOTPRequestViewModel = ViewModelProvider(this).get(VerifyOTPRequestViewModel::class.java)
        phoneConfirmationRequestViewModel=ViewModelProvider(this).get(PhoneConfirmationRequestViewModel::class.java)
        otp_view.otpListener  // retrieves the current OTPListener (null if nothing is set)
        otp_view.requestFocusOTP()	//sets the focus to OTP box (does not open the keyboard)

        otp_view?.otpListener = object : OTPListener {
            override fun onInteractionListener() {
            }

            override fun onOTPComplete(otp: String) {
                otpNumber=otp.trim().trim()
               // Toast.success(activity, "The OTP is $otp", Toast.LENGTH_SHORT).show()
            }
        }
        tvContinue.setOnClickListener {
            checkOTP()
        }
        tvResendCode.setOnClickListener {
            ResendCode()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Constants.isSignIn){
            requireActivity().toolbar_title.text  = getString(R.string.signIn)
        }else if (Constants.isEditProfile){
        requireActivity().toolbar_title.text  = getString(R.string.edit_profile)
       }
        else{
            requireActivity().toolbar_title.text  = getString(R.string.signup)
        }

    }

    override fun onResume() {
        super.onResume()
        if (Constants.isEditProfile){
            (activity as HomeActivity).tab_layout?.visibility=View.GONE
        }
    }

    /**
     *  API to Verify OTP
     */
    fun checkOTP() {
       otpNumber= otp_view.otp.toString().trim()
        ViewUtils.hideKeyBoard(requireActivity())

        if (otpNumber.isEmpty()) {
            ViewUtils.showSnackBar(scrollview, getString(R.string.enter_your_otp))
        } else if (otpNumber.length<6) {
            ViewUtils.showSnackBar(scrollview, getString(R.string.enter_your_valid_otp))
        }
        else if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(scrollview,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(requireContext())
            val hashMap= HashMap<String,String>()
            hashMap[Constants.KEY_USER]=""+ AlchemiApplication.alchemiApplication?.getUUID()?.trim()
            hashMap[Constants.KEY_PHONE]=Constants.phoneNumber
            hashMap[Constants.KEY_OTP]=otpNumber
            var token=""
            if (Constants.isEditProfile) {
                token = Constants.Token+ AlchemiApplication.alchemiApplication?.getToken()
            }
            //Log.e("test>>",">>>P"+hashMap)
            verifyOTPRequestViewModel.verifyOTP(hashMap,view,token)!!.observe(viewLifecycleOwner, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code==Constants.CODE_200){
                    if (Constants.isEditProfile){
                        (activity as HomeActivity).clearStack(2)
                    }else {
                        FragmentNavigation
                                .Builder(activity?.supportFragmentManager, R.id.fragmentContainer, CaptchaVerificationFragment.newInstance()).addToBackstack()
                                .execute()
                    }

                }else{
                    val msg =emailConfirmationRequestModel.message
                    ViewUtils.showSnackBar(cl_parent,""+msg)
                }

            })
        }
    }

    /**
     *  methof to resend code
     */
    fun ResendCode() {
        otpNumber= otp_view.otp.toString().trim()
        otp_view.resetState()
        otp_view.setOTP("")
        ViewUtils.hideKeyBoard(requireActivity())

        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(scrollview,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(requireContext())
            val hashMap= HashMap<String,String>()
            hashMap[Constants.KEY_USER]=""+ AlchemiApplication.alchemiApplication?.getUUID()?.trim()
            hashMap[Constants.KEY_PHONE]=Constants.phoneNumber
            var token=""
            if (Constants.isEditProfile) {
                token = Constants.Token+ AlchemiApplication.alchemiApplication?.getToken()
            }
            phoneConfirmationRequestViewModel.getPhoneConfirmationRquest(hashMap, view, token)!!.observe(viewLifecycleOwner, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code==Constants.CODE_200){
                    ViewUtils.showSnackBar(cl_parent,""+emailConfirmationRequestModel.message)
                }else{
                    val msg =emailConfirmationRequestModel.message
                    ViewUtils.showSnackBar(cl_parent,""+msg)
                }

            })
        }
    }

    override fun onDestroyView() {
        try {
            ViewUtils.hideKeyBoard(requireActivity())
        }catch (e:Exception){
            e.printStackTrace()
        }
        super.onDestroyView()
    }


}