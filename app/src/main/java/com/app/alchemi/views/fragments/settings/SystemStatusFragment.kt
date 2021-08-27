package com.app.alchemi.views.fragments.settings

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.UpdateNotificationStatusViewModel
import com.app.alchemi.views.activities.HomeActivity
import kotlinx.android.synthetic.main.settings_fragment_layout.*
import kotlinx.android.synthetic.main.settings_fragment_layout.ivCardSSN
import kotlinx.android.synthetic.main.system_status_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class SystemStatusFragment: Fragment() {
    private lateinit var updateNotificationStatusViewModel: UpdateNotificationStatusViewModel
    companion object {
        fun newInstance() = SystemStatusFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.system_status_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateNotificationStatusViewModel = ViewModelProvider(this).get(UpdateNotificationStatusViewModel::class.java)

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
        requireActivity().toolbar_title.text  = getString(R.string.system_status)
        requireActivity().toolbar_title.gravity= Gravity.START
        requireActivity().ivChat.visibility= View.GONE
        requireActivity().rlAcx.visibility= View.GONE
        checkSystemStatus()

    }



    override fun onDestroyView() {
        (activity as HomeActivity).tab_layout?.visibility= View.VISIBLE
        requireActivity().toolbar_title.gravity= Gravity.NO_GRAVITY
        super.onDestroyView()
    }

    /***
     *  check overall system status status
     */
    fun checkSystemStatus() {
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(
                    view,
                    getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry)
            )
        } else {
            ViewUtils.showProgress(requireContext())
            updateNotificationStatusViewModel.getSystemStatusFun(Constants.Token + AlchemiApplication.alchemiApplication?.getToken(),view)!!.observe(viewLifecycleOwner, Observer { liveDataModel ->
                ViewUtils.dismissProgress()
                if (liveDataModel.code == Constants.CODE_200) {
                    if (liveDataModel.data.system_response){
                        tvSystemStatus.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorGreen))
                        tvSystemStatus.text=getString(R.string.connected)
                    }else{
                        tvSystemStatus.text=getString(R.string.down)
                        tvSystemStatus.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorYellow))
                    }
                } else {
                    ViewUtils.showSnackBar(view, "" + liveDataModel.message)
                }

            })
        }
    }


}