package com.app.alchemi.views.fragments.dashboardFeatures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.models.UserDetailModel
import com.app.alchemi.repository.MainActivityRepository
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.LogoutUserViewModel
import com.app.alchemi.viewModel.UserDetailViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.activities.MainActivity
import com.app.alchemi.views.fragments.settings.*
import kotlinx.android.synthetic.main.settings_fragment_layout.*
import kotlinx.android.synthetic.main.settings_fragment_layout.scrollView
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.bouncycastle.asn1.cmc.CMCStatus.pending


class SettingsFragment: Fragment() {
    var isClick=false
    internal lateinit var userDetailViewModel: UserDetailViewModel
    internal  lateinit var logoutUserViewModel: LogoutUserViewModel
    var notificationStatus= true
    var passCodeSettingsStatus=true

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.settings_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDetailViewModel = ViewModelProvider(this).get(UserDetailViewModel::class.java)
        logoutUserViewModel= ViewModelProvider(this).get(LogoutUserViewModel::class.java)
        /***
         *  Manage Click to redirect to user
         */
        rlPassoceSettings.setOnClickListener {
            clickNavigator(rlPassoceSettings)
        }
        rlProfile.setOnClickListener {
            clickNavigator(rlProfile)
        }
        rlChangePasscode.setOnClickListener {
            clickNavigator(rlChangePasscode)
        }
        rlAntiPhishingCode.setOnClickListener {
            clickNavigator(rlAntiPhishingCode)
        }
        rlTwoFactorAuthentication.setOnClickListener {
            clickNavigator(rlTwoFactorAuthentication)
        }

        rlNotificationSettings.setOnClickListener {
            clickNavigator(rlNotificationSettings)
        }
        rlContactSupport.setOnClickListener {
            clickNavigator(rlContactSupport)
        }
        rlAlchemiTermsConditions.setOnClickListener {
            clickNavigator(rlAlchemiTermsConditions)
        }
        rlFeeAndLimits.setOnClickListener {
            clickNavigator(rlFeeAndLimits)
        }
        rlTermsConditions.setOnClickListener {
            clickNavigator(rlTermsConditions)
        }
        rlTermsConditions.setOnClickListener {
            clickNavigator(rlTermsConditions)
        }
        rlPrivacyNotice.setOnClickListener {
            clickNavigator(rlPrivacyNotice)
        }
        rlSystemStatus.setOnClickListener {
            clickNavigator(rlSystemStatus)
        }
        rlVisitWebSite.setOnClickListener {
            clickNavigator(rlVisitWebSite)
        }
        tvLogout.setOnClickListener {
            clickNavigator(tvLogout)
        }




        try {
            if (activity!=null) {
                (activity as HomeActivity).ivBack.setOnClickListener {
                    (activity as HomeActivity).clearStack(1)
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        isClick=false
        (activity as HomeActivity).tab_layout?.visibility=View.GONE
        requireActivity().toolbar_title.text  = getString(R.string.settings)
        requireActivity().ivBack.setImageResource(R.drawable.ic_back)
        requireActivity().ivChat.visibility=View.GONE
        requireActivity().rlAcx.visibility=View.GONE
        requireActivity().ivSettings.visibility=View.GONE
        requireActivity().ivBack.visibility=View.VISIBLE
        getUserDetail()
    }

    override fun onDestroyView() {
        if (!isClick) {
            (activity as HomeActivity).tab_layout?.visibility = View.VISIBLE
            (activity as HomeActivity).ivChat.visibility = View.VISIBLE
            (activity as HomeActivity).rlAcx.visibility = View.VISIBLE
            (activity as HomeActivity).toolbar_title.text = getString(R.string.home)
            (activity as HomeActivity).ivBack.setImageResource(R.drawable.setting)
            requireActivity().ivSettings.visibility=View.VISIBLE
            requireActivity().ivBack.visibility=View.GONE
        }

        super.onDestroyView()
    }
    fun getUserDetail(){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(requireContext())
            userDetailViewModel.getUserDetail(Constants.Token+ AlchemiApplication.alchemiApplication?.getToken(),view)!!.observe(viewLifecycleOwner, Observer { userDetailModel ->
                ViewUtils.dismissProgress()
                if (userDetailModel.code==Constants.CODE_200){
                    if (userDetailModel.data.firstname!=null &&userDetailModel.data.firstname!="null") {
                        tvName.text = userDetailModel.data.firstname + " " + userDetailModel.data.lastname
                    }
                    AlchemiApplication.alchemiApplication?.saveUserDetails(userDetailModel.data.user_id)
                    if (userDetailModel.data.pin!=null&&userDetailModel.data.pin!="null") {
                        AlchemiApplication.alchemiApplication?.savePasscode(userDetailModel.data.pin)
                    }
                    if (userDetailModel.data.anti_phishing_code!=null &&userDetailModel.data.anti_phishing_code!="null") {
                        AlchemiApplication.alchemiApplication?.saveAntiPhishingCode(userDetailModel.data.anti_phishing_code.toString())
                    }
                    notificationStatus=userDetailModel.data.notification_status
                    passCodeSettingsStatus=userDetailModel.data.pin_status
                    AlchemiApplication.alchemiApplication?.savePassCodeSettings(""+userDetailModel.data.pin_status)
                    ViewUtils.loadImage(userDetailModel.data.selfie,ivProfile,requireContext())
                    if (userDetailModel.data.isSsnVerified){
                        ivCardSSN.setImageResource(R.drawable.check)
                        ivBankSSN.setImageResource(R.drawable.check)
                        tvCardSSN.text=getString(R.string.matched)
                        tvBankSSN.text=getString(R.string.matched)
                        tvCardSSN.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorGreen))
                        tvBankSSN.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorGreen))
                    }else{
                        ivCardSSN.setImageResource(R.drawable.ic_error)
                        ivBankSSN.setImageResource(R.drawable.ic_error)
                        tvCardSSN.text=getString(R.string.pending)
                        tvBankSSN.text=getString(R.string.pending)
                        tvCardSSN.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorYellow))
                        tvBankSSN.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorYellow))
                    }
                }else{
                    val msg =userDetailModel.message
                    ViewUtils.showSnackBar(scrollView,""+msg)
                }

            })
        }
    }


    /***
     * Logout User
     */
    fun logoutUser(){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(requireContext())
            logoutUserViewModel.logoutUser(Constants.Token+ AlchemiApplication.alchemiApplication?.getToken(),view)!!.observe(viewLifecycleOwner, Observer { logoutUserModel ->
                ViewUtils.dismissProgress()
                if (logoutUserModel.code==Constants.CODE_200){
                    AlchemiApplication.alchemiApplication?.clearAllData()
                    AlchemiApplication.alchemiApplication?.saveScreenIndex("0")
                    startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    activity?.finish()
                    /// showEmailConfirmationScreen()
                }else{
                    val msg =logoutUserModel.message
                    ViewUtils.showSnackBar(scrollView,""+msg)
                }

            })
        }
    }

    fun clickNavigator(view: View){
        isClick=true
        when (view.id){

            R.id.rlPassoceSettings->{
                val  bundle= Bundle()
                bundle.putSerializable(Constants.KEY_STATUS,passCodeSettingsStatus)
                (activity as HomeActivity).replaceFragment(PasscodeSettingsFragment(),""+PasscodeSettingsFragment,bundle)
            }
            R.id.rlProfile->{
                val  bundle= Bundle()
                (activity as HomeActivity).replaceFragment(EditProfileFragment(),""+EditProfileFragment,bundle)
            }
            R.id.rlChangePasscode->{
                val  bundle= Bundle()
                (activity as HomeActivity).replaceFragment(UpdatePasscodeFragment(),""+UpdatePasscodeFragment,bundle)
            }
            R.id.rlTwoFactorAuthentication->{
//                val  bundle= Bundle()
//                (activity as HomeActivity).replaceFragment(UpdateEmailFragment(),""+UpdateEmailFragment,bundle)
            }
            R.id.rlAntiPhishingCode->{
                val  bundle= Bundle()
                (activity as HomeActivity).replaceFragment(AntiPhishingCodeFragment(),""+AntiPhishingCodeFragment,bundle)
            }
            R.id.rlNotificationSettings->{
                val  bundle= Bundle()
                bundle.putSerializable(Constants.KEY_STATUS,notificationStatus)
                (activity as HomeActivity).replaceFragment(NotificationSettingsFragment(),""+NotificationSettingsFragment,bundle)
            }
            R.id.rlContactSupport->{
                val  bundle= Bundle()
                (activity as HomeActivity).replaceFragment(ContactSupportFragment(),""+ContactSupportFragment,bundle)
            }

            R.id.rlAlchemiTermsConditions->{
                val  bundle= Bundle()
                bundle.putSerializable(Constants.KEY_TITLE,getString(R.string.terms_and_conditions))
                (activity as HomeActivity).replaceFragment(WebViewFragment(),""+WebViewFragment,bundle)
            }
            R.id.rlFeeAndLimits->{
                val  bundle= Bundle()
                bundle.putSerializable(Constants.KEY_TITLE,getString(R.string.fees_and_limits))
                (activity as HomeActivity).replaceFragment(WebViewFragment(),""+WebViewFragment,bundle)
            }
            R.id.rlTermsConditions->{
                val  bundle= Bundle()
                bundle.putSerializable(Constants.KEY_TITLE,getString(R.string.terms_and_conditions))
                (activity as HomeActivity).replaceFragment(WebViewFragment(),""+WebViewFragment,bundle)
            }
            R.id.rlPrivacyNotice->{
                val  bundle= Bundle()
                bundle.putSerializable(Constants.KEY_TITLE,getString(R.string.privacy_notice))
                (activity as HomeActivity).replaceFragment(WebViewFragment(),""+WebViewFragment,bundle)
            }
            R.id.rlSystemStatus->{
                val  bundle= Bundle()
                (activity as HomeActivity).replaceFragment(SystemStatusFragment(),""+SystemStatusFragment,bundle)
            }
            R.id.rlVisitWebSite->{
                Toast.makeText(context,"Coming soon !!",Toast.LENGTH_SHORT).show()

            }
            R.id.tvLogout->{
                showAlertDialog(requireContext())
               // logoutUser()

            }
        }
    }

    fun showAlertDialog(context: Context) {
        if (ViewUtils.alert != null && ViewUtils.alert!!.isShowing) {
            ViewUtils.alert!!.dismiss()
        }
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(getString(R.string.attention))
        dialogBuilder.setMessage(getString(R.string.do_you_want_to_sign_out_from_app))
                .setCancelable(false)
                .setPositiveButton(context.resources.getString(R.string.yes)) { dialog, id ->
                    dialog.cancel()
                    logoutUser()

                }
                .setNegativeButton(context.resources.getString(R.string.no)) { dialog, id ->
                    dialog.cancel()

                }
        ViewUtils.alert = dialogBuilder.create()
        ViewUtils.alert!!.show()
    }


}