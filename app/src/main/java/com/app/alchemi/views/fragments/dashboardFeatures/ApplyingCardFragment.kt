package com.app.alchemi.views.fragments.dashboardFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.alchemi.R
import kotlinx.android.synthetic.main.applying_card_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*


class ApplyingCardFragment: Fragment() {

    companion object {
        fun newInstance() = ApplyingCardFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.applying_card_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().toolbar_title.text=""
        requireActivity().ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
       tvName.text=requireArguments().getString(Constants.KEY_TITLE)
    }

    override fun onDestroyView() {
        requireActivity().toolbar_title.text=getString(R.string.card_details)
        super.onDestroyView()
    }
}




