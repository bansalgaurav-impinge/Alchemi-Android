package com.app.alchemi.views.fragments.dashboardFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.GetTopNewsViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.NewsAdapterDetail
import kotlinx.android.synthetic.main.new_detail.*
import kotlinx.android.synthetic.main.toolbar_layout.*


class AllNewsFragment: Fragment() {
    private lateinit var getTopNewsViewModel: GetTopNewsViewModel
    private lateinit var newsAdapter: NewsAdapterDetail
    companion object {
        fun newInstance() = AllNewsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.new_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTopNewsViewModel = ViewModelProvider(this).get(GetTopNewsViewModel::class.java)
        val llmnew = LinearLayoutManager(context)
        llmnew.orientation = LinearLayoutManager.VERTICAL
        rvTopNews?.layoutManager = llmnew
        swipeRefresh.setOnRefreshListener {
            getTopNews(true)              // refresh your list contents somehow
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).tab_layout?.visibility=View.GONE
        requireActivity().toolbar_title.text  = getString(R.string.all_news)
        requireActivity().ivBack.setImageResource(R.drawable.ic_back)
        requireActivity().ivBack.visibility=View.VISIBLE
        requireActivity().ivChat.visibility=View.GONE
        requireActivity().ivSettings.visibility=View.GONE
        requireActivity().rlAcx.visibility=View.GONE
        getTopNews(false)
    }
    override fun onDestroyView() {
        (activity as HomeActivity).tab_layout?.visibility = View.VISIBLE
        (activity as HomeActivity).ivChat.visibility = View.VISIBLE
        (activity as HomeActivity).rlAcx.visibility = View.VISIBLE
        (activity as HomeActivity).toolbar_title.text = getString(R.string.home)
        (activity as HomeActivity).ivBack.setImageResource(R.drawable.setting)
        (activity as HomeActivity).ivSettings.visibility = View.VISIBLE
        (activity as HomeActivity).ivBack.visibility = View.GONE
        super.onDestroyView()
    }
    fun getTopNews(isSWipeRefresh:Boolean){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            if (!isSWipeRefresh) {
                ViewUtils.showProgress(requireContext())
            }
            getTopNewsViewModel.getTopNews(Constants.Token + AlchemiApplication.alchemiApplication?.getToken(), view)!!.observe(viewLifecycleOwner, Observer { homeModel ->
                ViewUtils.dismissProgress()
                swipeRefresh.isRefreshing=false
                if (homeModel.code == Constants.CODE_200) {
                    if(homeModel.data.top_news.isNotEmpty()) {
                        //llTopNews.visibility=View.VISIBLE
                        newsAdapter = NewsAdapterDetail(homeModel.data.top_news)
                        rvTopNews?.setHasFixedSize(true)
                        rvTopNews?.adapter = newsAdapter
                    }else{
                       // llTopNews.visibility=View.GONE
                    }
                } else {
                    val msg = homeModel.message
                    ViewUtils.showSnackBar(view, "" + msg)
                }
            })
        }
    }
}