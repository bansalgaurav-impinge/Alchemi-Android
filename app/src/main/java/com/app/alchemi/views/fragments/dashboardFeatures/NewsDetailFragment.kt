package com.app.alchemi.views.fragments.dashboardFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.app.alchemi.R

import com.app.alchemi.views.activities.HomeActivity

import kotlinx.android.synthetic.main.news_detail.*
import kotlinx.android.synthetic.main.toolbar_layout.*


class NewsDetailFragment: Fragment() {
    companion object {
        fun newInstance() = NewsDetailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.news_detail, container, false)

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).tab_layout?.visibility=View.GONE
        requireActivity().toolbar_title.text  = getString(R.string.new_detail)
        requireActivity().ivBack.setImageResource(R.drawable.ic_back)
        requireActivity().ivBack.visibility=View.VISIBLE
        requireActivity().ivChat.visibility=View.GONE
        requireActivity().rlAcx.visibility=View.GONE
        requireActivity().ivSettings.visibility=View.GONE

        try {
            if (!this.requireArguments().isEmpty && arguments!=null){
                tvNewsTitle.text = requireArguments()[Constants.KEY_TITLE].toString()
                tvNewsDescription.text = requireArguments()[Constants.KEY_DESCRIPTION].toString()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    override fun onDestroyView() {
        (activity as HomeActivity).tab_layout?.visibility = View.VISIBLE
        (activity as HomeActivity).ivChat.visibility = View.VISIBLE
        (activity as HomeActivity).rlAcx.visibility = View.VISIBLE
        (activity as HomeActivity).toolbar_title.text = getString(R.string.home)
        (activity as HomeActivity).ivBack.setImageResource(R.drawable.setting)
        requireActivity().ivSettings.visibility=View.VISIBLE
        requireActivity().ivBack.visibility=View.GONE
        super.onDestroyView()
    }

}