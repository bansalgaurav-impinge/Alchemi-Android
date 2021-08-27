package com.app.alchemi.views.fragments.dashboardFeatures

import Constants
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.alchemi.R
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.CoinHistoryViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.TermAssetsAdapter
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.term_assets_detail_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*


class TermAssetsFragment: Fragment(){
    private lateinit var coinHistoryViewModel: CoinHistoryViewModel
    private lateinit var termAssetsAdapter: TermAssetsAdapter
    companion object {
        fun newInstance() = TermAssetsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.term_assets_detail_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinHistoryViewModel = ViewModelProvider(this).get(CoinHistoryViewModel::class.java)
        ViewUtils.showAnimation(context, ivTermIcon, R.anim.slide_in_right)
        ViewUtils.showAnimation(context, tvTermName, R.anim.slide_in_right)



        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvList?.layoutManager = llm
        val  hash=HashMap<String, String>()
        hash[Constants.KEY_FIRST_NAME]="Alchemi Earn Deposit"
        hash[Constants.KEY_LAST_NAME]="SUN, FEB 28"
        val  hash1=HashMap<String, String>()
        hash1[Constants.KEY_FIRST_NAME]="Alchemi Earn"
        hash1[Constants.KEY_LAST_NAME]="SUN, FEB 28"
        val  hash2=HashMap<String, String>()
        hash2[Constants.KEY_FIRST_NAME]="Alchemi Earn"
        hash2[Constants.KEY_LAST_NAME]="SUN, FEB 28"
        val  hash3=HashMap<String, String>()
        hash3[Constants.KEY_FIRST_NAME]="Alchemi Earn"
        hash3[Constants.KEY_LAST_NAME]="WED, MAR 03"

        val dataList=ArrayList<HashMap<String, String>>()

        dataList.add(hash)
        dataList.add(hash1)
        dataList.add(hash2)
        dataList.add(hash3)
        termAssetsAdapter = TermAssetsAdapter(dataList)
        rvList?.setHasFixedSize(true)
        rvList?.adapter = termAssetsAdapter

        (activity as HomeActivity).tab_layout?.visibility=View.VISIBLE
//        requireActivity().ivBack.setOnClickListener {
//            (activity as HomeActivity).clearStack(1)
//        }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().toolbar_title.text = getString(R.string.three_months_terms)
        requireActivity().ivBack.visibility=View.VISIBLE
        requireActivity().ivScan.visibility=View.GONE
        requireActivity().ivChart.visibility=View.GONE

    }

    override fun onDestroyView() {
        requireActivity().toolbar_title.text = getString(R.string.alchemi_earn)
        requireActivity().ivBack.visibility=View.GONE
        requireActivity().ivScan.visibility=View.VISIBLE
        requireActivity().ivChart.visibility=View.VISIBLE
        super.onDestroyView()
    }






}




