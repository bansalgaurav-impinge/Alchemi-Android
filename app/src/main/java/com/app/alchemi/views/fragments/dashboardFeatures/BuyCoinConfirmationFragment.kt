package com.app.alchemi.views.fragments.dashboardFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.alchemi.R
import com.app.alchemi.models.Option
import com.app.alchemi.views.activities.HomeActivity
import kotlinx.android.synthetic.main.buy_coin_confirmation_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*


class BuyCoinConfirmationFragment: Fragment() {
     var stakingOptions: List<Option>?=null


    companion object {
        fun newInstance() = BuyCoinConfirmationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.buy_coin_confirmation_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvContinue.setOnClickListener {
            (activity as HomeActivity).clearStack(1)
        }
    }


    override fun onResume() {
        super.onResume()
        requireActivity().toolbar_title.text = getString(R.string.confirm_buy,"BTC")
        (activity as HomeActivity).tab_layout?.visibility=View.GONE
        requireActivity().ivBack.visibility=View.INVISIBLE
        requireActivity().tvCancel.visibility=View.VISIBLE
        requireActivity().ivSettings.visibility=View.GONE
        requireActivity().ivChat.visibility=View.GONE
        requireActivity().rlAcx.visibility=View.GONE



    }

    override fun onDestroyView() {
        (activity as HomeActivity).tab_layout?.visibility=View.VISIBLE
        requireActivity().toolbar_title.text = getString(R.string.home)
        requireActivity().ivBack.visibility=View.GONE
        requireActivity().ivSettings.visibility=View.VISIBLE
        requireActivity().ivChat.visibility=View.VISIBLE
        requireActivity().rlAcx.visibility=View.VISIBLE
        requireActivity().tvCancel.visibility=View.GONE
        super.onDestroyView()
    }


}