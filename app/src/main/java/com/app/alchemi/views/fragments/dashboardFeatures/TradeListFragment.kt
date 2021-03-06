package com.app.alchemi.views.fragments.dashboardFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.CheckUserPinViewModel
import com.app.alchemi.viewModel.GetTopCryptoCurrencyViewModel
import com.app.alchemi.viewModel.HomeViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.TopGainersAdapter
import kotlinx.android.synthetic.main.new_detail.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.greenrobot.eventbus.EventBus


class TradeListFragment: Fragment() {
    private lateinit var getTopCryptoCurrencyViewModel: GetTopCryptoCurrencyViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var checkUserPinViewModel: CheckUserPinViewModel
    private lateinit var tradeCoinsAdapter: TopGainersAdapter
    companion object {
        fun newInstance() = TradeListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.new_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTopCryptoCurrencyViewModel = ViewModelProvider(this).get(GetTopCryptoCurrencyViewModel::class.java)
        checkUserPinViewModel = ViewModelProvider(this).get(CheckUserPinViewModel::class.java)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val llmnew = LinearLayoutManager(context)

        llmnew.orientation = LinearLayoutManager.VERTICAL

        rvTopNews?.layoutManager = llmnew
        swipeRefresh.setOnRefreshListener {
            getTopCryptoCurrency(true)              // refresh your list contents somehow
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).tab_layout?.visibility=View.GONE
        requireActivity().toolbar_title.text  = getString(R.string.top_gainers)
        requireActivity().ivBack.visibility=View.VISIBLE
        requireActivity().ivChat.visibility=View.GONE
        requireActivity().ivSettings.visibility=View.GONE
        requireActivity().rlAcx.visibility=View.GONE
        getTopCryptoCurrency(false)
    }
    override fun onDestroyView() {
        (activity as HomeActivity).tab_layout?.visibility = View.VISIBLE
        (activity as HomeActivity).ivChat.visibility = View.VISIBLE
        (activity as HomeActivity).rlAcx.visibility = View.VISIBLE
        (activity as HomeActivity).toolbar_title.text = getString(R.string.home)
        (activity as HomeActivity).ivSettings.visibility = View.VISIBLE
        (activity as HomeActivity).ivBack.visibility = View.GONE
        super.onDestroyView()
    }

    /***
     *  get Top Crypto Currency
     */
    fun getTopCryptoCurrency(isSwipeRefresh: Boolean){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            if (!isSwipeRefresh) {
                ViewUtils.showProgress(requireContext())
            }
            homeViewModel.homeData(Constants.Token + AlchemiApplication.alchemiApplication?.getToken(), view)!!.observe(viewLifecycleOwner, Observer { homeModel ->
                ViewUtils.dismissProgress()
                swipeRefresh.isRefreshing=false
                if (homeModel.code == Constants.CODE_200) {
                    if(homeModel.data.top_gainers.isNotEmpty()) {
                        tradeCoinsAdapter = TopGainersAdapter(homeModel.data.top_gainers,this)
                        rvTopNews?.adapter = tradeCoinsAdapter
                        tradeCoinsAdapter.notifyDataSetChanged()
                    }
                } else {
                    val msg = homeModel.message
                    ViewUtils.showSnackBar(view, "" + msg)
                }
            })
        }
    }

    /***
     * Update coin Notifications
     */
    fun updateCoinNotifications(status: Boolean, code: String, ivNotifications: ToggleButton){
        val hashMap= HashMap<String,String>()
        hashMap[Constants.KEY_STATUS]=""+status
        hashMap[Constants.KEY_CODE]=code
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            checkUserPinViewModel.updateCoinNotifications(hashMap,view,Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(viewLifecycleOwner, Observer { liveData ->
                ViewUtils.dismissProgress()
                if (liveData.code == Constants.CODE_200) {
                    ivNotifications.isChecked=status
                    EventBus.getDefault().post(Constants.KEY_REFRESH_HOME_SCREEN_DATA)
                } else {
                    ivNotifications.isChecked=!status
                    val msg = liveData.message
                    ViewUtils.showSnackBar(view, "" + msg)
                }
            })
        }
    }
}