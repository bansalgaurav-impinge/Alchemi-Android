package com.app.alchemi.views.fragments.dashboardFeatures

import Constants
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.interfaceintercation.OnItemClickListener
import com.app.alchemi.models.TopGainer
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.GetTopCryptoCurrencyViewModel
import com.app.alchemi.viewModel.HomeViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.BuyCoinAdapter
import com.app.alchemi.views.adapters.DepositCurrencyAdapter
import com.app.alchemi.views.adapters.NewsAdapter
import com.app.alchemi.views.adapters.TopGainersHomeAdapter
import com.app.alchemi.views.adapters.TradeCoinsAdapter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_layout.bottomSheet
import kotlinx.android.synthetic.main.bottom_sheet_layout.ivCrossIcon
import kotlinx.android.synthetic.main.buy_crypto_bootom_sheet.*
import kotlinx.android.synthetic.main.deposit_coin_wallet_address_layout.*
import kotlinx.android.synthetic.main.deposit_currency_bottom_sheet.*
import kotlinx.android.synthetic.main.home_fragment_layout.*
import kotlinx.android.synthetic.main.home_fragment_layout.rvTopNews
import kotlinx.android.synthetic.main.home_fragment_layout.swipeRefresh
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.trade_bottom_sheet.*
import kotlinx.android.synthetic.main.transafer_bottom_sheet.*
import kotlinx.android.synthetic.main.withdraw_currency_bottom_sheet.*
import java.text.NumberFormat


class HomeFragment: Fragment(), OnItemClickListener {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var topGainersAdapter: TopGainersHomeAdapter
    private lateinit var tradeCoinsAdapter: TradeCoinsAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var getCryptoCurrencyViewModel: GetTopCryptoCurrencyViewModel
    private lateinit var buyCoinAdapter: BuyCoinAdapter
    private lateinit var depositCurrencyAdapter: DepositCurrencyAdapter
    var coinList: List<TopGainer>?=null
    var dialog: BottomSheetDialog?=null
    var isFromDeposit=false
    var isFromWithDraw=false
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.home_fragment_layout, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        getCryptoCurrencyViewModel = ViewModelProvider(this).get(GetTopCryptoCurrencyViewModel::class.java)
        ViewUtils.showAnimation(context,tvTotalBalance,R.anim.slide_in_right)
        ViewUtils.showAnimation(context,llTradeBalance,R.anim.slide_in_right)
        ViewUtils.showAnimation(context,llTopButtons,R.anim.slide_in_right)
        val llmnew = LinearLayoutManager(context)
        llmnew.orientation = LinearLayoutManager.HORIZONTAL
        rvTopNews?.layoutManager = llmnew
        val llmTopGainers=LinearLayoutManager(context)
        llmTopGainers.orientation = LinearLayoutManager.HORIZONTAL
        rvTopGainers?.layoutManager=llmTopGainers
        val llmTradeCoins = LinearLayoutManager(context)
        llmTradeCoins.orientation = LinearLayoutManager.VERTICAL
        rvListHome?.layoutManager = llmTradeCoins


        homeData(false)
        /***
         * Click Listener
         */
        val bundle= Bundle()
        tvSeeAllNews.setOnClickListener {
            (activity as HomeActivity).replaceFragment(AllNewsFragment(), "" + AllNewsFragment, bundle)
        }
        tvSeeAll.setOnClickListener {
            (activity as HomeActivity).replaceFragment(TradeListFragment(), "" + TradeListFragment, bundle)
        }
        try {
            if (activity!=null) {
                (activity as HomeActivity).ivBack.setOnClickListener {
                    //  SettingsFragment
                    (activity as HomeActivity).onBackPressed()
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        /***
         *  bottom sheet init
         */
        bottomSheet()
        tvTransfer.setOnClickListener {
            slideUpDownBottomSheet()
        }
        tvTrade.setOnClickListener {
            slideUpDownBottomSheetTrade()
        }
        swipeRefresh.setOnRefreshListener {
            homeData(true)                  // refresh your list contents somehow
           /// swipeRefresh.isRefreshing = false   // reset the SwipeRefreshLayout (stop the loading spinner)
        }

    }

    /**
     * Home Data API
     */
    fun homeData(isSwipeRefresh: Boolean){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            if (!isSwipeRefresh) {
                ViewUtils.showProgress(requireContext())
            }
            homeViewModel.homeData(Constants.Token + AlchemiApplication.alchemiApplication?.getToken(), view)!!.observe(viewLifecycleOwner, Observer { homeModel ->
                ViewUtils.dismissProgress()
                swipeRefresh.isRefreshing = false
                if (homeModel.code == Constants.CODE_200) {
                    val totalData= NumberFormat.getInstance().format(homeModel.data.total_balance)
                    ViewUtils.changeTextColor(tvTotalBalance, (getString(R.string.dollar_sign) + " " + totalData+ " " + getString(R.string.usd)), ContextCompat.getColor(requireContext(), R.color.white), 1, (getString(R.string.dollar_sign) + " " + totalData + " " + getString(R.string.usd)).length - 4, false)
                    ViewUtils.showAnimation(context,tvTotalBalance,R.anim.slide_in_right)
                    if (homeModel.data.top_news.isNotEmpty()) {
                        rvTopNews.visibility = View.VISIBLE
                        tvSeeAllNews.isEnabled=true
                        tvNoNews.visibility=View.GONE
                        newsAdapter = NewsAdapter(homeModel.data.top_news)
                        rvTopNews?.adapter = newsAdapter
                    } else {
                        rvTopNews.visibility = View.GONE
                        tvNoNews.visibility=View.VISIBLE
                        tvSeeAllNews.isEnabled=false
                    }
                    tradeCoinsAdapter = TradeCoinsAdapter(homeModel.data.crypto_live_data)
                    rvListHome?.adapter = tradeCoinsAdapter
                    topGainersAdapter = TopGainersHomeAdapter(homeModel.data.top_gainers)
                    rvTopGainers?.adapter = topGainersAdapter
                } else {
                    val msg = homeModel.message
                    ViewUtils.showSnackBar(view, "" + msg)
                }
            })
        }
    }



    override fun onResume() {
        super.onResume()
        YoYo.with(Techniques.Flash)
            .duration(1500)
            .repeat(100)
            .repeatMode(Animation.INFINITE)
            .playOn(ivTopGainer)
    }

    /***
     * Transfer Bottom Sheet
     */
    private fun slideUpDownBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.transafer_bottom_sheet, null)
         dialog = BottomSheetDialog(requireContext(),R.style.SheetDialog)

        dialog?.setContentView(dialogView)
        YoYo.with(Techniques.SlideInUp).duration(500).playOn(dialogView)
        val ivClose = dialog?.ivCrossIcon as ImageView
        val llDeposit = dialog?.llDeposit as LinearLayout
        val llWithdraw = dialog?.llWithdraw as LinearLayout
        ivClose.setOnClickListener {
            YoYo.with(Techniques.SlideOutDown).duration(500).playOn(dialogView)
            dialog?.dismiss()
        }
        llDeposit.setOnClickListener {
            isFromDeposit=true
            isFromWithDraw=false
            getCryptoCurrency(false)
           // dialog?.dismiss()


        }
        llWithdraw.setOnClickListener {
            dialog?.dismiss()
            bottomSheetWithdraw()
        }

        dialog?.show()

    }

    fun bottomSheet(){
        /**
         * Bottom Sheet Behaviour
         */
        bottomSheetBehavior = BottomSheetBehavior.from<LinearLayout>(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // animating the view on top of Bottom Sheet
         //         bottomSheet.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation)
                bottomSheet.animate().y((if (slideOffset <= 0) view!!.y + bottomSheetBehavior.peekHeight - bottomSheet.height else view!!.height - bottomSheet.height) as Float).setDuration(0).start()
                YoYo.with(Techniques.SlideInUp)
                        .duration(500)
                        .playOn(bottomSheet)

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                }
            }
        })
    }

    /***
     *  get List of coin to buy
     */
    fun getCryptoCurrency(isSwipeRefresh: Boolean){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            if (!isSwipeRefresh) {
                ViewUtils.showProgress(requireContext())
            }
            getCryptoCurrencyViewModel.getTopCryptoCurrency(Constants.Token + AlchemiApplication.alchemiApplication?.getToken(), view)!!.observe(viewLifecycleOwner, Observer { homeModel ->
                ViewUtils.dismissProgress()
                swipeRefresh.isRefreshing=false
                if (homeModel.code == Constants.CODE_200) {
                    if(homeModel.data.mktcapfull.isNotEmpty()) {
                        dialog!!.dismissWithAnimation=true
                        dialog?.dismiss()
                        if (isFromDeposit||isFromWithDraw){
                            slideUpDownBottomSheetDepositCurrency(homeModel.data.mktcapfull)
                        }else {
                            slideUpDownBottomSheetBuyCrypto(homeModel.data.mktcapfull)
                        }

                    }
                } else {
                    ViewUtils.showSnackBar(view, "" + homeModel.message)
                }
            })
        }
    }

    /****
     *  Trade Bottom Sheet View
     */
    private fun slideUpDownBottomSheetTrade() {
        val dialogView = layoutInflater.inflate(R.layout.trade_bottom_sheet, null)
         dialog = BottomSheetDialog(requireContext(),R.style.SheetDialog)

        dialog?.setContentView(dialogView)
        YoYo.with(Techniques.SlideInUp).duration(500).playOn(dialogView)
        val ivClose = dialog?.ivCrossIcon as ImageView
        val llBuy = dialog?.llBuy as LinearLayout
        val llSell = dialog?.llSell as LinearLayout
        ivClose.setOnClickListener {
            YoYo.with(Techniques.SlideOutDown).duration(500).delay(500).playOn(dialogView)
            dialog?.dismiss()
        }
        llBuy.setOnClickListener {
           isFromWithDraw=false
            getCryptoCurrency(false)
           // dialog?.dismiss()
        }
        llSell.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.show()

    }
    /***
     * Buy Crypto bottom Sheet View
     */
    private fun slideUpDownBottomSheetBuyCrypto(mktcapfull: List<TopGainer>) {
        val dialogView = layoutInflater.inflate(R.layout.buy_crypto_bootom_sheet, null)
         dialog = BottomSheetDialog(requireContext(),R.style.SheetDialog)

        dialog?.setContentView(dialogView)
      YoYo.with(Techniques.SlideInUp).duration(500).playOn(dialogView)
        val ivClose = dialog?.ivCrossIcon as ImageView
        val etSearch = dialog?.etSearch as EditText
        val rvList = dialog?.rvList as RecyclerView
//        val bottomSheet= dialog.bottomSheet as LinearLayout
        val metrics= resources.displayMetrics
//        bottomSheetBehavior.peekHeight=metrics.heightPixels/3
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvList?.layoutManager = llm
        coinList=mktcapfull
        buyCoinAdapter = BuyCoinAdapter(mktcapfull,this)
        rvList.adapter = buyCoinAdapter
        buyCoinAdapter.notifyDataSetChanged()
        /****
         * To Search Coin
         */

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                try {
                    if (coinList?.isNotEmpty() == true || coinList != null) {
                        if (s.trim().isNotEmpty()) {
                            var tempList = coinList!!.filter {
                                it.FULLNAME
                                        .startsWith(s.trim(), true)

                            }

                            buyCoinAdapter = BuyCoinAdapter(tempList, HomeFragment())
                            rvList.adapter = buyCoinAdapter
                            buyCoinAdapter.notifyDataSetChanged()

                        } else {
                            buyCoinAdapter = BuyCoinAdapter(coinList!!, HomeFragment())
                            rvList.adapter = buyCoinAdapter
                            buyCoinAdapter.notifyDataSetChanged()

                        }
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }
        })

        ivClose.setOnClickListener {
            YoYo.with(Techniques.SlideOutDown).duration(500).delay(500).playOn(dialogView)
            etSearch.text.clear()
            ViewUtils.hideKeyBoard(requireActivity())
            dialog?.dismiss()
        }


        dialog?.show()

    }

    /***
     * Deposit Currency bottom sheet
     */
    private fun slideUpDownBottomSheetDepositCurrency(mktcapfull: List<TopGainer>) {
        val dialogView = layoutInflater.inflate(R.layout.deposit_currency_bottom_sheet, null)
        dialog = BottomSheetDialog(requireContext(),R.style.SheetDialog)

        dialog?.setContentView(dialogView)
        YoYo.with(Techniques.SlideInUp).duration(500).playOn(dialogView)
        val ivClose = dialog?.ivCrossIcon as ImageView
        val etSearch = dialog?.etSearchCoin as EditText
        val  tvCurrencyTitle= dialog?.tvCurrencyTitle as TextView
        if (isFromWithDraw){
            tvCurrencyTitle.text=getString(R.string.withdraw_currency)
            etSearch.hint=getString(R.string.withdraw_currency)
        }else{
            tvCurrencyTitle.text=getString(R.string.deposit_currency)
            etSearch.hint=getString(R.string.deposit_currency)
        }
        val rvList = dialog?.rvList as RecyclerView
//        val bottomSheet= dialog.bottomSheet as LinearLayout
        val metrics= resources.displayMetrics
//        bottomSheetBehavior.peekHeight=metrics.heightPixels/3
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvList?.layoutManager = llm
        coinList=mktcapfull
        depositCurrencyAdapter = DepositCurrencyAdapter(mktcapfull,this)
        rvList.adapter = depositCurrencyAdapter
        depositCurrencyAdapter.notifyDataSetChanged()
        /****
         * To Search Coin
         */

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                try {
                    if (coinList?.isNotEmpty() == true || coinList != null) {
                        if (s.trim().isNotEmpty()) {
                            var tempList = coinList!!.filter {
                                it.FULLNAME
                                        .startsWith(s.trim(), true)

                            }

                            depositCurrencyAdapter = DepositCurrencyAdapter(tempList, HomeFragment())
                            rvList.adapter = depositCurrencyAdapter
                            depositCurrencyAdapter.notifyDataSetChanged()

                        } else {
                            depositCurrencyAdapter = DepositCurrencyAdapter(coinList!!, HomeFragment())
                            rvList.adapter = depositCurrencyAdapter
                            depositCurrencyAdapter.notifyDataSetChanged()

                        }
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }
        })

        ivClose.setOnClickListener {
            YoYo.with(Techniques.SlideOutDown).duration(500).delay(500).playOn(dialogView)
            etSearch.text.clear()
            ViewUtils.hideKeyBoard(requireActivity())
            isFromDeposit=false
            dialog?.dismiss()
        }


        dialog?.show()

    }

    /****
     * bottom sheet for deposit coin wallet address.
     */


    private fun bottomSheetDepositCoinWalletAddress(fromsymbol: String, fullname: String) {
        val dialogView = layoutInflater.inflate(R.layout.deposit_coin_wallet_address_layout, null)
        dialog = BottomSheetDialog(requireContext(),R.style.SheetDialog)
        dialog?.setContentView(dialogView)
        YoYo.with(Techniques.SlideInUp).duration(500).playOn(dialogView)
        val ivClose = dialog?.ivCrossIcon as ImageView
        val ivQRCode = dialog?.ivQRCode as ImageView
        val ivBackIcon = dialog?.ivBackIcon as ImageView
        val ivCopy=dialog?.ivCopy as ImageView
        val tvBlockChainDomains = dialog?.tvBlockChainDomains as TextView
        val tvWallet = dialog?.tvWallet as TextView
        val tvBuy=dialog?.tvBuy as TextView
        val tvCoinName= dialog?.tvCoinName as TextView
        val tvTitle= dialog?.tvTitleTxt as TextView
        val tvWalletAddress=dialog?.tvWalletAddress as TextView
        val view1= dialog?.view1 as View
        val view2= dialog?.view2 as View
        tvTitle.text=getString(R.string.deposit)+" "+fromsymbol
        tvWallet.text=fromsymbol+" Wallet Address"
        tvCoinName.text=fullname
        ivClose.setOnClickListener {
            YoYo.with(Techniques.SlideOutDown).duration(500).delay(500).playOn(dialogView)
            dialog?.dismiss()
        }
        tvCoinName.setOnClickListener {
           view1.visibility=View.VISIBLE
           view2.visibility=View.INVISIBLE
            tvWallet.text=fromsymbol+" Wallet Address"
           // getCryptoCurrency(false)
            // dialog?.dismiss()
        }
        tvBlockChainDomains.setOnClickListener {
           // dialog?.dismiss()
            view1.visibility=View.INVISIBLE
            view2.visibility=View.VISIBLE
            tvWallet.text=getString(R.string.blockchain_domains)
        }
        tvBuy.setOnClickListener {
            dialog?.dismiss()
        }
        ivCopy.setOnClickListener {
            tvWalletAddress.text
            Toast.makeText(requireContext(), getString(R.string.copied),Toast.LENGTH_SHORT).show()
        }
        ivBackIcon.setOnClickListener {
            YoYo.with(Techniques.SlideOutRight).duration(500).delay(500).playOn(dialogView)
            dialog?.dismiss()
            coinList?.let { it1 -> slideUpDownBottomSheetDepositCurrency(it1) }
        }
        dialog?.show()

    }

    /***
     *  Withdraw currency feature bottom sheet
     */

    /****
     *  Trade Bottom Sheet View
     */
    private fun bottomSheetWithdraw() {
        val dialogView = layoutInflater.inflate(R.layout.withdraw_currency_bottom_sheet, null)
        dialog = BottomSheetDialog(requireContext(),R.style.SheetDialog)

        dialog?.setContentView(dialogView)
        YoYo.with(Techniques.SlideInUp).duration(500).playOn(dialogView)
        val ivClose = dialog?.ivCrossIcon as ImageView
        val tvExternalWallet = dialog?.tvExternalWallet as TextView
        val tvAlchemiExchange = dialog?.tvAlchemiExchange as TextView
        val tvAlchemiUsers =dialog?.tvAlchemiUsers as TextView
        ivClose.setOnClickListener {
            YoYo.with(Techniques.SlideOutDown).duration(500).delay(500).playOn(dialogView)
            dialog?.dismiss()
            isFromDeposit=false
            isFromWithDraw=false
        }
        tvAlchemiExchange.setOnClickListener {
           // dialog?.dismiss()
            isFromWithDraw=true
            isFromDeposit=false
            getCryptoCurrency(false)

        }
        tvAlchemiUsers.setOnClickListener {
           // dialog?.dismiss()
            isFromWithDraw=true
            isFromDeposit=false
            getCryptoCurrency(false)

        }
        tvExternalWallet.setOnClickListener {
           // dialog?.dismiss()
            isFromWithDraw=true
            isFromDeposit=false
            getCryptoCurrency(false)
        }

        dialog?.show()

    }

    /**
     *  Click listner
     */
    override fun getItemClicked(itemId: String?) {
        dialog?.dismiss()
        if (isFromDeposit) {
            bottomSheetDepositCoinWalletAddress(coinList!!.get(itemId!!.toInt()).FROMSYMBOL, coinList!![itemId.toInt()].FULLNAME)
        }else if (isFromWithDraw){
            val bundle = Bundle()
            (activity as HomeActivity).replaceFragment(WithdrawAmountFragment(), "" + WithdrawAmountFragment, bundle)
            isFromWithDraw=false
        }
        else{

            val bundle = Bundle()
            (activity as HomeActivity).replaceFragment(StakingPeriodFragment(), "" + StakingPeriodFragment, bundle)
        }
    }

    override fun onDestroyView() {
        isFromDeposit=false
        isFromWithDraw=false
        super.onDestroyView()
    }

}