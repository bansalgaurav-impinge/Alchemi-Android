package com.app.alchemi.views.fragments.signup


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.FragmentNavigation
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.UploadDocumentsViewModel
import com.app.alchemi.viewModel.VeriffSessionUrlViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.veriff.VeriffBranding
import com.veriff.VeriffConfiguration
import com.veriff.VeriffResult
import com.veriff.VeriffSdk
import kotlinx.android.synthetic.main.full_legal_name_fragment_layout.*
import kotlinx.android.synthetic.main.full_legal_name_fragment_layout.cl_parent
import kotlinx.android.synthetic.main.full_legal_name_fragment_layout.llBottoms
import kotlinx.android.synthetic.main.full_legal_name_fragment_layout.tvContinue
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class FullLegalNameFragment: Fragment() {
    private lateinit var veriffSessionUrlViewModel: VeriffSessionUrlViewModel
    internal lateinit var uploadDocumentsViewModel: UploadDocumentsViewModel
    val REQUEST_CODE=100
    companion object {
        fun newInstance() = FullLegalNameFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.full_legal_name_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val aninSlideRight = AnimationUtils.loadAnimation(context,
            R.anim.slide_in_left)
        llTop.startAnimation(aninSlideRight)
        llBottoms.startAnimation(aninSlideRight)
        veriffSessionUrlViewModel= ViewModelProvider(this).get(VeriffSessionUrlViewModel::class.java)
        uploadDocumentsViewModel= ViewModelProvider(this).get(UploadDocumentsViewModel::class.java)
        if (Constants.isEditProfile){
            (activity as HomeActivity).tab_layout?.visibility=View.GONE
            try {
                if (!this.requireArguments().isEmpty && arguments!=null){
                    etFirstName.setText(requireArguments()[Constants.KEY_FIRST_NAME].toString())
                    etLastName.setText(requireArguments()[Constants.KEY_LAST_NAME].toString())
                    etFirstName.setSelection(requireArguments()[Constants.KEY_FIRST_NAME].toString().length)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }

        }

        /***
         *  Click to open Email confirmation
         */
        tvContinue.setOnClickListener {
            validateView()
        }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().toolbar_title.text  = getString(R.string.full_legal_name)
        if (Constants.isEditProfile){
            (activity as HomeActivity).tab_layout!!.visibility=View.GONE
        }
        try {
           Constants.countryName= AlchemiApplication.alchemiApplication?.getCountryName().toString()
            Constants.countryCode= AlchemiApplication.alchemiApplication?.getCountryCode().toString()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    /***
     * show doocuments
     */
    fun showIdDocumentsScreen() {
        getVeriffSdkSessionUrl()
    }

    /**
     * validate View
     */
    fun validateView(){
        ViewUtils.hideKeyBoard(requireActivity())
        val firstname = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        Constants.firstName=etFirstName.text.toString().trim()
        Constants.lasttName=etLastName.text.toString().trim()

        if (firstname.isEmpty()) {
            ViewUtils.showSnackBar(cl_parent,getString(R.string.enter_your_first_name))
        } else if (lastName.isEmpty()) {
            ViewUtils.showSnackBar(cl_parent,getString(R.string.enter_your_last_name))
        }
        else if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(cl_parent,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            showIdDocumentsScreen()
        }
    }

    /***
     * Get Veriff SDK Session URL
     */
    fun getVeriffSdkSessionUrl(){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            val date = Calendar.getInstance().time
            val dateformat = SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            ViewUtils.showProgress(requireContext())
            val hashMap= HashMap<String,String>()
            hashMap[Constants.KEY_FIRSTNAME]=Constants.firstName.trim()
            hashMap[Constants.KEY_LASTNAME]=Constants.lasttName.trim()
            hashMap[Constants.KEY_USER]=""+ AlchemiApplication.alchemiApplication?.getUUID()
            hashMap[Constants.KEY_TIMESTAMP]=""+dateformat.format(date)
            veriffSessionUrlViewModel.getVeriffSessionUrl(hashMap,view)!!.observe(viewLifecycleOwner, Observer { veriffSessionUrlModel ->
                ViewUtils.dismissProgress()
                if (veriffSessionUrlModel.code==200){
                    if (veriffSessionUrlModel.data.sessionToken.isNotEmpty()){
                        initateVeriffSdk(veriffSessionUrlModel.data.sessionToken)
                    }


                }else{
                    ViewUtils.showSnackBar(view,""+veriffSessionUrlModel.message)
                }

            })
        }
    }

    /***
     *  Init Veriff SDK
     */
    fun  initateVeriffSdk(sessionToken: String){
        val branding = VeriffBranding.Builder()
            .themeColor(ContextCompat.getColor(requireContext(),R.color.colorYellow))
            .backgroundColor(ContextCompat.getColor(requireContext(),R.color.colorVeriffBg))
            .statusBarColor(ContextCompat.getColor(requireContext(),R.color.black))
            .primaryTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            .secondaryTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            .buttonCornerRadius(48f)
            .build()

        val configuration = VeriffConfiguration.Builder()
            .branding(branding)
            .customIntroScreen(true)
            .build()

        val intent = VeriffSdk.createLaunchIntent(activity,""+sessionToken, configuration)
        startActivityForResult(intent, REQUEST_CODE)
    }

    fun handleResult(result: VeriffResult) {
        when (result.status) {
            VeriffResult.Status.DONE -> {

                if (Constants.isEditProfile){
                    updateUserDetail()
                }else{
                    uploadDocument()
                }
            }
            VeriffResult.Status.CANCELED -> {
                Log.e("test>>", "Verification cancled occurred: " + result)
            }
            VeriffResult.Status.ERROR ->         // An error occurred during the flow, Veriff has already shown UI, no need to display
                // a separate error message here
                Log.e("test>>", "Verification error occurred: " + result.error)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            val result = VeriffResult.fromResultIntent(data)
            result?.let { handleResult(it)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Upload documments
     */
    fun uploadDocument() {
         if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(cl_parent, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        } else {
            ViewUtils.showProgress(requireContext())
            uploadDocumentsViewModel.uploadDocuments(false,""+ AlchemiApplication.alchemiApplication?.getUUID()?.trim(),Constants.firstName,Constants.lasttName,Constants.countryName,Constants.countryCode,"",true,"",view)!!.observe(viewLifecycleOwner, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code == Constants.CODE_200) {
                    AlchemiApplication.alchemiApplication?.saveScreenIndex("5")
                    FragmentNavigation
                            .Builder(activity?.supportFragmentManager, R.id.fragmentContainer,UploadDocumentFragment.newInstance())
                            .addToBackstack()
                            .execute()
                } else {
                    val msg = emailConfirmationRequestModel.message
                    ViewUtils.showSnackBar(cl_parent, "" + msg)
                }

            })
        }
    }

    /**
     * Update user details
     */
    fun updateUserDetail() {
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        } else {
            ViewUtils.showProgress(requireContext())
            uploadDocumentsViewModel.uploadDocuments(true,""+ AlchemiApplication.alchemiApplication?.getUUID()?.trim(),Constants.firstName,Constants.lasttName,Constants.countryName,Constants.countryCode,"",true,Constants.Token+ AlchemiApplication.alchemiApplication?.getToken(), view)!!.observe(viewLifecycleOwner, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code == Constants.CODE_200) {
                    ViewUtils.showSnackBar(view, "" + emailConfirmationRequestModel.message)
                    if (activity!=null){
                        (activity as HomeActivity).clearStack(2)
                    }
                } else {
                    val msg = emailConfirmationRequestModel.message
                    ViewUtils.showSnackBar(view, "" + msg)
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