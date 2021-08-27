package com.app.alchemi.views.fragments.dashboardFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.alchemi.R
import com.app.alchemi.interfaceintercation.OnItemClickListener
import com.app.alchemi.models.Option
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.StakingOptionsViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.StakingPeriodSpinnerAdapter
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.staking_period_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.util.*
import kotlin.collections.HashMap


class StakingPeriodFragment: Fragment(), OnItemClickListener {
    private lateinit var stakingPeriodSpinnerAdapter: StakingPeriodSpinnerAdapter
    private lateinit var stakingOptionsViewModel: StakingOptionsViewModel
     var stakingOptions: List<Option>?=null


    companion object {
        fun newInstance() = StakingPeriodFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.staking_period_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stakingOptionsViewModel = ViewModelProvider(this).get(StakingOptionsViewModel::class.java)

        rlSpinnerOptions.setOnClickListener {
            rvList.visibility=View.VISIBLE
        }
        checkStakingOptions()
        val llm = LinearLayoutManager(context)
        Constants.KEY_SELECTED_SPINNER_VALUE= getString(R.string.flexible)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvList.layoutManager = llm

        rvList.visibility=View.GONE

        tvContinue.setOnClickListener {
            (activity as HomeActivity).clearStack(1)
        }
    }


    override fun onResume() {
        super.onResume()
        requireActivity().tvTitle.text = getString(R.string.choose_staking_period)
        requireActivity().tvTitle.visibility=View.VISIBLE
        requireActivity().toolbar_title.visibility=View.GONE
        (activity as HomeActivity).tab_layout?.visibility=View.GONE
        requireActivity().ivBack.visibility=View.VISIBLE
        requireActivity().ivBack.visibility=View.VISIBLE
        requireActivity().ivSettings.visibility=View.GONE
        requireActivity().ivChat.visibility=View.GONE
        requireActivity().rlAcx.visibility=View.GONE
        rlSpinnerOptions.performClick()


    }

    override fun onDestroyView() {

        requireActivity().tvTitle.visibility=View.GONE
        requireActivity().toolbar_title.visibility=View.VISIBLE
        (activity as HomeActivity).tab_layout?.visibility=View.VISIBLE

        super.onDestroyView()
    }

    /***
     * listner
     */
    override fun getItemClicked(itemId: String?) {
        when (itemId){
            "0" -> {
                tvFilterName.text = stakingOptions?.get(itemId.toInt())?.name
                tvInterest.text= stakingOptions?.get(itemId.toInt())?.interest
                rvList.visibility=View.GONE
            }
            "1" -> {
                tvFilterName.text =  stakingOptions?.get(itemId.toInt())?.name
                tvInterest.text= stakingOptions?.get(itemId.toInt())?.interest
                rvList.visibility=View.GONE
            }
            "2" -> {
                tvFilterName.text =  stakingOptions?.get(itemId.toInt())?.name
                tvInterest.text= stakingOptions?.get(itemId.toInt())?.interest
                rvList.visibility=View.GONE

            }
            "3" -> {
                tvFilterName.text =  stakingOptions?.get(itemId.toInt())?.name
                tvInterest.text= stakingOptions?.get(itemId.toInt())?.interest
                rvList.visibility=View.GONE
            }
            "4" -> {
                tvFilterName.text =  stakingOptions?.get(itemId.toInt())?.name
                tvInterest.text= stakingOptions?.get(itemId.toInt())?.interest
                rvList.visibility=View.GONE
            }else->{
            tvFilterName.text =  stakingOptions?.get(itemId!!.toInt())?.name
            tvInterest.text= stakingOptions?.get(itemId!!.toInt())?.interest
            rvList.visibility=View.GONE
            }

        }
    }

    /***
     *  Get Staking Option
     */

    fun checkStakingOptions() {
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        } else {
            ViewUtils.showProgress(requireContext())
            stakingOptionsViewModel.getStakingOptions(view, Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(viewLifecycleOwner,
                { liveDataModel ->
                    ViewUtils.dismissProgress()
                    if (liveDataModel.code == Constants.CODE_200) {
                        stakingOptions=liveDataModel.data.options
                        Collections.reverse(stakingOptions)
                        Constants.KEY_SELECTED_SPINNER_VALUE= stakingOptions!![0].name
                        tvInterest.text= stakingOptions!![0].interest
                        stakingPeriodSpinnerAdapter = StakingPeriodSpinnerAdapter(stakingOptions!!, this)
                        rvList.setHasFixedSize(true)
                        rvList.adapter = stakingPeriodSpinnerAdapter
                    } else {
                        ViewUtils.showSnackBar(view, "" + liveDataModel.message)
                    }

                })
        }
    }
}