package com.app.alchemi.views.fragments.dashboardFeatures

import Constants
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.models.Card
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.CardListViewModel
import com.app.alchemi.viewModel.CheckUserPinViewModel
import com.app.alchemi.views.adapters.CardPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.card_fragment_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class CardsFragment: Fragment() {
    private lateinit var cardListViewModel: CardListViewModel
    private lateinit var checkUserPinViewModel:CheckUserPinViewModel
    var cardList: List<Card>?=null
    var selectedPosition=0
    companion object {
        fun newInstance() = CardsFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.card_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardListViewModel = ViewModelProvider(this).get(CardListViewModel::class.java)
        checkUserPinViewModel = ViewModelProvider(this).get(CheckUserPinViewModel::class.java)
        getCardList()


        //pager.adapter = CardPagerAdapter(requireContext(), cardList)
//        TabLayoutMediator(tabLayout, pager) {tab, position ->
//           tab.customView=getTabIndicator(
//                            tabLayout.context,
//                            R.drawable.order_card,words[position]
//                    )
//
//        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedPosition= tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    override fun onResume() {
        super.onResume()

    }

    fun getTabIndicator(context: Context, url: String, title: String): View {
        val view = LayoutInflater.from(context).inflate(R.layout.card_view, null)
        val ivIcon = view.findViewById(R.id.ivIcon) as ImageView
        val tvTitle = view.findViewById(R.id.tvTabText) as TextView
        ViewUtils.downloadImage(url,ivIcon,requireContext())
     //   iv.setImageResource(icon)
        tvTitle.text =  title.split(" ")[0]+"\n"+title.split(" ")[1]
        return view

    }

    /***
     *  Get Card List
     */
    fun getCardList() {
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        } else {
            ViewUtils.showProgress(requireContext())
            cardListViewModel.getCardList(view, Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(viewLifecycleOwner,
                { liveDataModel ->
                    ViewUtils.dismissProgress()
                    if (liveDataModel.code == Constants.CODE_200) {
                        cardList=liveDataModel.data.cards
                        pager.adapter = CardPagerAdapter(requireContext(), cardList,this)
                        pager.currentItem=selectedPosition
                        pager.adapter?.notifyDataSetChanged()

                        TabLayoutMediator(tabLayout, pager) {tab, position ->
                            tab.customView=getTabIndicator(
                                tabLayout.context,
                                liveDataModel.data.cards[position].card_image , liveDataModel.data.cards[position].name
                            )

                        }.attach()
                    } else {
                        ViewUtils.showSnackBar(view, "" + liveDataModel.message)
                    }

                })
        }
    }

    /***
     *  Enable/ disable stake feature
     */

    fun updateStakeStatus(isChecked: Boolean, id: String,selectedPos:Int) {
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(
                view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry)
            )
        } else {
            val hashMap = HashMap<String, String>()
            hashMap[Constants.KEY_CARD_ID] = id
            hashMap[Constants.KEY_STAKING_STATUS] = ""+isChecked
            ViewUtils.showProgress(requireContext())
            checkUserPinViewModel.activateOrDeactivateStakeStatus(hashMap, view, Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(viewLifecycleOwner, Observer { liveDataModel ->
                ViewUtils.dismissProgress()
                 var isSuccess:Boolean?=null
                selectedPosition=selectedPos
                if (liveDataModel.code == Constants.CODE_200) {
                    getCardList()
                    //retun isSuccess=true
//                    switchPassCodeOptions.isChecked = isChecked
//                    AlchemiApplication.alchemiApplication?.savePassCodeSettings(""+isChecked)
                } else {
                  //retun isSuccess=false
                   // switchPassCodeOptions.isChecked = !isChecked
                    ViewUtils.showSnackBar(view, "" + liveDataModel.message)
                }

            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().unregister(this)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun onStart() {
        super.onStart()
        try {
            if (!EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().register(this)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    internal fun onMessageEvent(event:String) {
        if (event == Constants.KEY_REFRESH_DATA){
            getCardList()
        }
    }

}






