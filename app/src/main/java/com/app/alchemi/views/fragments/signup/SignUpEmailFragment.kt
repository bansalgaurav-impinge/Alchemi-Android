package com.app.alchemi.views.fragments.signup

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.TextView.BufferType
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.FragmentNavigation
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.EmailConfirmationRequestViewModel
import kotlinx.android.synthetic.main.sign_up_fragment_layout.*
import kotlinx.android.synthetic.main.sign_up_fragment_layout.llBottoms
import kotlinx.android.synthetic.main.toolbar_layout.*


class SignUpEmailFragment: Fragment() {
    internal lateinit var emailConfirmationRequestViewModel: EmailConfirmationRequestViewModel
    companion object {
        fun newInstance() = SignUpEmailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.sign_up_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val aninSlideRight = AnimationUtils.loadAnimation(context,
            R.anim.slide_in_left)
        llTopView.startAnimation(aninSlideRight)
        llBottoms.startAnimation(aninSlideRight)
        emailConfirmationRequestViewModel = ViewModelProvider(this).get(EmailConfirmationRequestViewModel::class.java)

        singleTextView(tvCheckboxTxt,getString(R.string.i_accept_the_terms_and_conditions),getString(R.string.terms_and_conditions),getString(R.string.and),getString(R.string.privacy_notice))

        /***
         *  Click to open Email confirmation
         */
        tvContinue.setOnClickListener {
            if (!etEmail.text.trim().isEmpty()){
                Constants.EMAIL=etEmail.text.toString().trim()
            }
            checkEmail()
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Constants.isSignIn){
            ll_checkbox.visibility=View.VISIBLE
            requireActivity().toolbar_title.text  = getString(R.string.signIn)
        }else{
            ll_checkbox.visibility=View.VISIBLE
            requireActivity().toolbar_title.text  = getString(R.string.signup)
        }
    }

    /**
     *  show next screen
     */
    fun showEmailConfirmationScreen(){
        FragmentNavigation
                .Builder(activity?.supportFragmentManager, R.id.fragmentContainer, EmailConfirmationFragment.newInstance())
                .addToBackstack()
                .execute()
    }

    /**
     *  method to change and text
     */
    private fun singleTextView(textView: TextView, text: String, termsConditions: String, and: String, privacyNotice: String) {
        val spanText = SpannableStringBuilder()
        spanText.append(text)
        spanText.append(" $termsConditions")
        spanText.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {

                // On Click Action
                FragmentNavigation
                    .Builder(activity?.supportFragmentManager, R.id.fragmentContainer, TermsConditions.newInstance())
                    .addToBackstack()
                    .execute()
            }

            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = textPaint.linkColor // you can use custom color
                textPaint.isUnderlineText = false // this remove the underline
            }
        }, spanText.length - termsConditions.length, spanText.length, 0)
        spanText.append(" $and ")
        spanText.append(privacyNotice)
        spanText.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {

                // On Click Action
                FragmentNavigation
                        .Builder(activity?.supportFragmentManager, R.id.fragmentContainer, PrivacyNoticeFragment.newInstance())
                        .addToBackstack()
                        .execute()
            }

            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = textPaint.linkColor // you can use custom color
                textPaint.isUnderlineText = false // this remove the underline
            }
        }, spanText.length - privacyNotice.length, spanText.length, 0)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(spanText, BufferType.SPANNABLE)
    }

    /***
     *  method to check validation and call api
     */
    fun checkEmail(){
        ViewUtils.hideKeyBoard(requireActivity())
        val email = etEmail.text.toString().trim()
        if (email.isEmpty()) {
            ViewUtils.showSnackBar(cl_parent,getString(R.string.enter_you_email))
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ViewUtils.showSnackBar(cl_parent,getString(R.string.enter_valid_email))
        }
        else if (!checkboxTermsConditions.isChecked) {
            ViewUtils.showSnackBar(cl_parent,getString(R.string.accept_terms_conditions))
        }
        else if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(cl_parent,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(requireContext())
            val hashMap= HashMap<String,String>()
            hashMap[Constants.KEY_EMAIL]=email.trim()
            hashMap[Constants.KEY_REDIRECT_URL]=""+Constants.APP_URL.trim()
            if (!Constants.isSignIn) {
                hashMap[Constants.KEY_REFERRAL_CODE] = "" + requireArguments()[Constants.KEY_REFERRAL_CODE]
            }
            emailConfirmationRequestViewModel.getEmailConfimationRequest(hashMap,view)!!.observe(viewLifecycleOwner, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code==Constants.CODE_200){
                    AlchemiApplication.alchemiApplication?.clearAllData()
                    AlchemiApplication.alchemiApplication?.saveScreenIndex("1")
                    AlchemiApplication.alchemiApplication?.saveUserEmail(email)
                    showEmailConfirmationScreen()
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