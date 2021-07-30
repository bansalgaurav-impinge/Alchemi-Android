package com.app.alchemi.views.fragments.signup

import Constants
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.telephony.PhoneNumberUtils
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
import com.app.alchemi.viewModel.CountryListViewModel
import com.app.alchemi.viewModel.PhoneConfirmationRequestViewModel
import com.app.alchemi.views.activities.HomeActivity
import kotlinx.android.synthetic.main.phone_number_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*


class PhoneNumberFragment: Fragment() {
    private lateinit var phoneConfirmationRequestViewModel: PhoneConfirmationRequestViewModel
    private lateinit var countryListViewModel: CountryListViewModel

    companion object {
        fun newInstance() = PhoneNumberFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.phone_number_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val aninSlideRight = AnimationUtils.loadAnimation(context,
                R.anim.slide_in_left)
        llBottoms.startAnimation(aninSlideRight)
        ccPicker.registerCarrierNumberEditText(etmMobile)

        ccPicker.setInternationalFormattingOnly(true)
        ccPicker.setNumberAutoFormattingEnabled(true)
        registerCarrierEditText()
        ccPicker.setCustomMasterCountries("IN,US,MU")
        ccPicker.setDetectCountryWithAreaCode(true)
        try {
            if (!this.requireArguments().isEmpty && arguments!=null){
                this.requireArguments()["phone"]
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        phoneConfirmationRequestViewModel = ViewModelProvider(this).get(PhoneConfirmationRequestViewModel::class.java)
        countryListViewModel = ViewModelProvider(this).get(CountryListViewModel::class.java)
        getCountryList()

        tvSendCode.setOnClickListener {
            checkPhone()

        }

    }

    override fun onResume() {
        super.onResume()
        if (Constants.isEditProfile){
            (activity as HomeActivity).tab_layout?.visibility=View.GONE
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

    /***
     *  check validation and register phone number
     */
    fun checkPhone() {
        ViewUtils.hideKeyBoard(requireActivity())
        val phone = etmMobile.text.toString().trim()
        if (phone.isEmpty()) {
            ViewUtils.showSnackBar(scrollview, getString(R.string.enter_your_phone_number))
        } else if (!registerCarrierEditText()) {
            ViewUtils.showSnackBar(scrollview, getString(R.string.enter_your_valid_phone_number))
        }
        else if (phone.length < 6) {
            ViewUtils.showSnackBar(scrollview, getString(R.string.enter_your_valid_phone_number))
        } else if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(scrollview, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        } else {
            val hashMap = HashMap<String, String>()
            hashMap[Constants.KEY_PHONE] = ccPicker.selectedCountryCodeWithPlus +phone.trim()
            hashMap[Constants.KEY_USER] = ""+AlchemiApplication.alchemiApplication?.getUUID()?.trim()
            Constants.phoneNumber=""+ccPicker.selectedCountryCodeWithPlus +phone.trim()
            var token=""
            if (Constants.isEditProfile){
                token=Constants.Token+ AlchemiApplication.alchemiApplication?.getToken()
            }
            phoneConfirmationRequestViewModel.getPhoneConfirmationRquest(hashMap, view, token)!!.observe(viewLifecycleOwner, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code == Constants.CODE_200) {
                    Constants.countryName = ccPicker.selectedCountryName
                    Constants.countryCode = ccPicker.selectedCountryNameCode

                    AlchemiApplication.alchemiApplication?.saveCoutryNameAndCode(Constants.countryName, Constants.countryCode)
                    if (Constants.isEditProfile) {
                        val bundle = Bundle()
                        (activity as HomeActivity).replaceFragment(VerifyOTPFragment(), "" + VerifyOTPFragment, bundle)
                    } else {
                        FragmentNavigation
                                .Builder(activity?.supportFragmentManager, R.id.fragmentContainer, VerifyOTPFragment.newInstance())
                                .addToBackstack()
                                .execute()
                    }
                } else {
                    val msg = emailConfirmationRequestModel.message
                    ViewUtils.showSnackBar(cl_parent, "" + msg)
                }

            })

        }
    }

    /***
     * Register carriage for formatting
     */
    fun registerCarrierEditText(): Boolean {
            var isValid = false
            ccPicker.registerCarrierNumberEditText(etmMobile)
            ccPicker.setPhoneNumberValidityChangeListener { isValidNumber ->
                if (isValidNumber) {
                    isValid = true
                    if (ccPicker.selectedCountryCodeWithPlus.equals("+1")) {
                        etmMobile.setText(PhoneNumberUtils.formatNumber(etmMobile.text.toString().trim(), ccPicker.selectedCountryNameCode))
                        etmMobile.addTextChangedListener(PhoneNumberFormattingTextWatcher())
                    }

                } else {
                    isValid = false
                    if (ccPicker.selectedCountryCodeWithPlus.equals("+1")) {
                        etmMobile.setText(PhoneNumberUtils.formatNumber(etmMobile.text.toString().trim(), ccPicker.selectedCountryNameCode))
                        etmMobile.addTextChangedListener(PhoneNumberFormattingTextWatcher())
                    }


                }

            }

        return isValid
    }



    /****
     *  Get List of country
     */
    fun getCountryList(){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(scrollview, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(requireContext())
            countryListViewModel.getCountryList()!!.observe(viewLifecycleOwner, Observer { countryListModel ->
                ViewUtils.dismissProgress()
                if (countryListModel.code == 200) {
                    countryListModel.data.country_list
                    if (countryListModel.data.country_list.isNotEmpty()) {
                        var customCountryList = ""
                        for (element in countryListModel.data.country_list) {
                            if (element.country_code.equals("USA")) {
                                customCountryList = customCountryList + "US" + ","
                            } else {
                                customCountryList = customCountryList + element.country_code + ","
                            }
                        }
                        ccPicker.setCustomMasterCountries(customCountryList)
                    }


                } else {
                    ViewUtils.showSnackBar(scrollview, "" + countryListModel.message)
                }

            })
        }
    }

    override fun onDestroyView() {
        try {
            ViewUtils.hideKeyBoard(requireActivity())
        }catch (e: Exception){
            e.printStackTrace()
        }
        super.onDestroyView()
    }
}