package com.app.alchemi.views.fragments.dashboardFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.alchemi.R
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.views.activities.HomeActivity
import kotlinx.android.synthetic.main.amount_fragment_layout.*
import kotlinx.android.synthetic.main.amount_fragment_layout.ivBack
import kotlinx.android.synthetic.main.amount_fragment_layout.tvTitle
import kotlinx.android.synthetic.main.toolbar_layout.*


class WithdrawAmountFragment: Fragment() {

    companion object {
        fun newInstance() = WithdrawAmountFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.withdraw_amount_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /***
         *  send button functionality
         */
        tvSend.setOnClickListener {
            validateView()
        }
        /**
         *  back button functionality
//         */
//        requireActivity().ivBack.setOnClickListener {
//            (activity as HomeActivity).clearStack(1)
//        }
    }


    override fun onResume() {
        super.onResume()
        requireActivity().tvTitle.text = getString(R.string.amount)
        requireActivity().tvTitle.visibility=View.VISIBLE
        requireActivity().toolbar_title.visibility=View.GONE
        (activity as HomeActivity).tab_layout?.visibility=View.GONE
        requireActivity().ivBack.visibility=View.VISIBLE
        requireActivity().ivChat.visibility = View.GONE
        requireActivity().rlAcx.visibility = View.GONE
        requireActivity().ivSettings.visibility=View.GONE
        requireActivity().ivScan.visibility=View.GONE
        requireActivity().ivChart.visibility=View.GONE
    }

    /***
     *
     */
    fun validateView(){
        ViewUtils.hideKeyBoard(requireActivity())
        val walletAddress = etWalletAddress.text.toString().trim()
        val amount = etAmount.text.toString().trim()
        if (walletAddress.isEmpty()) {
            ViewUtils.showSnackBar(requireView(), getString(R.string.enter_wallet_address_validation))
        } else if (amount.isEmpty()) {
            ViewUtils.showSnackBar(requireView(), getString(R.string.enter_amount_value))
        }
        else if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(requireView(), getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            (activity as HomeActivity).clearStack(1)
        }
    }

    override fun onDestroyView() {
        try {
            ViewUtils.hideKeyBoard(requireActivity())
        }catch (e:Exception){
            e.printStackTrace()
        }
        requireActivity().tvTitle.visibility=View.GONE
        requireActivity().toolbar_title.visibility=View.VISIBLE
        (activity as HomeActivity).tab_layout?.visibility=View.VISIBLE
        requireActivity().ivBack.visibility=View.GONE
        requireActivity().ivChat.visibility = View.VISIBLE
        requireActivity().rlAcx.visibility = View.VISIBLE
        requireActivity().toolbar_title.text = getString(R.string.home)
        requireActivity().ivSettings.visibility=View.VISIBLE
        requireActivity().ivScan.visibility=View.GONE
        requireActivity().ivChart.visibility=View.GONE
        super.onDestroyView()
    }
}