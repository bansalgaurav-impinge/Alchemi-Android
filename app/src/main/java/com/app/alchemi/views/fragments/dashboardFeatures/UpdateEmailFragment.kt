package com.app.alchemi.views.fragments.dashboardFeatures

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
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.fragments.signup.EmailConfirmationFragment
import kotlinx.android.synthetic.main.sign_up_fragment_layout.*
import kotlinx.android.synthetic.main.sign_up_fragment_layout.cl_parent
import kotlinx.android.synthetic.main.sign_up_fragment_layout.llBottoms
import kotlinx.android.synthetic.main.sign_up_fragment_layout.tvContinue
import kotlinx.android.synthetic.main.toolbar_layout.*


class UpdateEmailFragment: Fragment() {
    internal lateinit var emailConfirmationRequestViewModel: EmailConfirmationRequestViewModel
    companion object {
        fun newInstance() = UpdateEmailFragment()
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
        requireActivity().toolbar_title.text  = getString(R.string.edit_profile)
        emailConfirmationRequestViewModel = ViewModelProvider(this).get(EmailConfirmationRequestViewModel::class.java)

            if (Constants.isEditProfile){
                try {
                    if (!this.requireArguments().isEmpty && arguments!=null){
                        etEmail.setText(requireArguments()[Constants.KEY_EMAIL].toString())
                        etEmail.setSelection(requireArguments()[Constants.KEY_EMAIL].toString().length)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            Constants.EMAIL= this.requireArguments()[Constants.KEY_EMAIL].toString()
        }

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

    override fun onResume() {
        super.onResume()
        if (Constants.isEditProfile){
            (activity as HomeActivity).tab_layout?.visibility=View.GONE
        }
        ll_checkbox.visibility=View.GONE
        Log.e("test>>",">>>"+AlchemiApplication.alchemiApplication?.getEmail())
    }
    fun showEmailConfirmationScreen(){
        val bundle= Bundle()
        FragmentNavigation
                .Builder(activity?.supportFragmentManager, R.id.fragmentContainer, EmailConfirmationFragment.newInstance())
                //.addToBackstack()
                .execute()
    }


    /***
     *  method to check validation and call api
     */
    fun checkEmail(){
        ViewUtils.hideKeyBoard(requireActivity())
        val email = etEmail.text.toString().trim()
        if (email.isEmpty()) {
            ViewUtils.showSnackBar(view,getString(R.string.enter_you_email))
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ViewUtils.showSnackBar(view,getString(R.string.enter_valid_email))
        }
        else if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {

            ViewUtils.showProgress(requireContext())
            val hashMap= HashMap<String,String>()
            hashMap[Constants.KEY_EMAIL]=email.trim()
            hashMap[Constants.KEY_REDIRECT_URL]=""+Constants.APP_URL.trim()
            emailConfirmationRequestViewModel.getUpdateEmailConfimationRequest(hashMap,view, Constants.Token+AlchemiApplication.alchemiApplication?.getToken().toString())!!.observe(viewLifecycleOwner, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code==Constants.CODE_200){
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