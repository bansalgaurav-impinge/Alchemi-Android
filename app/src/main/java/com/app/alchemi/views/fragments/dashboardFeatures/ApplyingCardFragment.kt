package com.app.alchemi.views.fragments.dashboardFeatures

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.CheckUserPinViewModel
import com.app.alchemi.views.activities.HomeActivity
import kotlinx.android.synthetic.main.applying_card_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.greenrobot.eventbus.EventBus


class ApplyingCardFragment: Fragment() {

    private lateinit var checkUserPinViewModel: CheckUserPinViewModel
    var isCardActivated=false
    companion object {
        fun newInstance() = ApplyingCardFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.applying_card_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().toolbar_title.text=""

        isCardActivated = requireArguments()[Constants.KEY_DESCRIPTION].toString().toBoolean()
        checkUserPinViewModel = ViewModelProvider(this).get(CheckUserPinViewModel::class.java)
        tvName.text=requireArguments().getString(Constants.KEY_TITLE)
        ViewUtils.downloadImage(requireArguments().getString(Constants.KEY_IMAGE).toString(),ivCard,requireContext())
        tvActivateCard.isEnabled=true

        if(isCardActivated){
            ivCardStatus.visibility=View.VISIBLE
            tvCardStatus.text=getString(R.string.shipped)
            tvActivateCard.text=getString(R.string.deactivate_my_card)

        }else{
            ivCardStatus.visibility=View.GONE
            tvCardStatus.text="N/A"
            tvActivateCard.text=getString(R.string.activate_my_card)
        }
        /**
         * Click to activate deactivate card
         */
        tvActivateCard.setOnClickListener {
            if (isCardActivated) {
                activateOrDeactivateCard(false)
            } else {
                activateOrDeactivateCard(true)
            }
        }
    }


    override fun onDestroyView() {
        requireActivity().toolbar_title.text=getString(R.string.card_details)

        super.onDestroyView()
    }

    /**
     * activate or deactivate particular card
     */
    fun activateOrDeactivateCard(isCardActivate: Boolean) {
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        } else {
            val hashMap= HashMap<String,String>()
            hashMap[Constants.KEY_CARD_ID]= requireArguments()[Constants.KEY_CARD_ID].toString()
            hashMap[Constants.KEY_IS_USER_ACTIVATE]= ""+isCardActivate
            ViewUtils.showProgress(requireContext())
            checkUserPinViewModel.activateOrDeactivateCard(hashMap,view, Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(viewLifecycleOwner,
                { liveDataModel ->
                    ViewUtils.dismissProgress()
                    if (liveDataModel.code == Constants.CODE_200) {
                        //tvActivateCard.isEnabled=false
                        EventBus.getDefault().post(Constants.KEY_REFRESH_DATA)
                        if (isCardActivate) {
                            isCardActivated=true
                            ivCardStatus.visibility=View.VISIBLE
                            tvCardStatus.text=getString(R.string.shipped)
                            ViewUtils.showSnackBar(view, ""+getString(R.string.your_card_activated_successfully))
                            tvActivateCard.text=getString(R.string.deactivate_my_card)

                        }else{
                            isCardActivated = false
                            ViewUtils.showSnackBar(view, "" + getString(R.string.your_card_dectivated_successfully))
                            tvActivateCard.text=getString(R.string.activate_my_card)
                        }
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            (activity as HomeActivity).clearStack(1)
//                        }, 300)
                    } else {
                        ViewUtils.showSnackBar(view, "" + liveDataModel.message)
                    }

                })
        }
    }
}




