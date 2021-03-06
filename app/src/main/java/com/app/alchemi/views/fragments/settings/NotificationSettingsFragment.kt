package com.app.alchemi.views.fragments.settings

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.UpdateNotificationStatusViewModel
import com.app.alchemi.views.activities.HomeActivity
import kotlinx.android.synthetic.main.enable_disable_notifications_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class NotificationSettingsFragment: Fragment() {
    private lateinit var updateNotificationStatusViewModel: UpdateNotificationStatusViewModel
    companion object {
        fun newInstance() = NotificationSettingsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.enable_disable_notifications_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateNotificationStatusViewModel = ViewModelProvider(this).get(UpdateNotificationStatusViewModel::class.java)
        if (arguments!=null){
            switchNotificationSettings.isChecked = requireArguments()[Constants.KEY_STATUS] as Boolean
        }

        switchNotificationSettings.setOnCheckedChangeListener { buttonView, isChecked ->
            updateNotificationSettingsStatus(isChecked)
        }
//        try {
//            if (activity!=null) {
//                (activity as HomeActivity).ivBack.setOnClickListener {
//                    (activity as HomeActivity).onBackPressed()
//                }
//            }
//        }catch (e: Exception){
//            e.printStackTrace()
//        }


    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).tab_layout?.visibility= View.GONE
        requireActivity().toolbar_title.text  = getString(R.string.notification_settings)
        requireActivity().toolbar_title.gravity= Gravity.START
        requireActivity().ivChat.visibility= View.GONE
        requireActivity().rlAcx.visibility= View.GONE


    }



    override fun onDestroyView() {
        (activity as HomeActivity).tab_layout?.visibility= View.VISIBLE
        requireActivity().toolbar_title.gravity= Gravity.NO_GRAVITY
        super.onDestroyView()
    }

    /***
     *  Update notification settings status
     */
    fun updateNotificationSettingsStatus(isChecked:Boolean){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            val hashMap= HashMap<String,Boolean>()
            hashMap[Constants.KEY_STATUS]=isChecked
            ViewUtils.showProgress(requireContext())
            updateNotificationStatusViewModel.updateNotificationStatus(hashMap,view,Constants.Token+ AlchemiApplication.alchemiApplication?.getToken())!!.observe(viewLifecycleOwner, Observer { liveDataModel ->
                ViewUtils.dismissProgress()
                if (liveDataModel.code==Constants.CODE_200){
                    switchNotificationSettings.isChecked=isChecked
                }else{
                    switchNotificationSettings.isChecked = !isChecked
                    ViewUtils.showSnackBar(view,""+liveDataModel.message)
                }

            })
        }
    }




}