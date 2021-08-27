package com.app.alchemi.views.fragments.dashboardFeatures

import Constants
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.alchemi.R
import com.app.alchemi.interfaceintercation.OnItemClickListener
import com.app.alchemi.models.TopGainer
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.CoinHistoryViewModel
import com.app.alchemi.viewModel.GetTopCryptoCurrencyViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.DepositCurrencyEarnAdapter
import com.app.alchemi.views.adapters.EarnAdapter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.alchemi_earns_terms_conditions_layout.*
import kotlinx.android.synthetic.main.alchemi_earns_terms_conditions_layout.bottomSheet
import kotlinx.android.synthetic.main.alchemi_earns_terms_conditions_layout.ivCrossIcon
import kotlinx.android.synthetic.main.deposit_currency_bottom_sheet.*
import kotlinx.android.synthetic.main.earn_fragment_layout.*
import kotlinx.android.synthetic.main.earn_fragment_layout.rvList
import kotlinx.android.synthetic.main.earn_fragment_layout.tvBalance
import kotlinx.android.synthetic.main.earn_fragment_layout.tvTotalBalance
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set


class EarnFragment: Fragment(), OnItemClickListener {
    private lateinit var coinHistoryViewModel: CoinHistoryViewModel
    private lateinit var earnAdapter: EarnAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var getCryptoCurrencyViewModel: GetTopCryptoCurrencyViewModel
    private lateinit var depositCurrencyAdapter: DepositCurrencyEarnAdapter
    var coinList: List<TopGainer>?=null
    var dialog: BottomSheetDialog?=null
    companion object {
        fun newInstance() = EarnFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.earn_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinHistoryViewModel = ViewModelProvider(this).get(CoinHistoryViewModel::class.java)
        getCryptoCurrencyViewModel = ViewModelProvider(this).get(GetTopCryptoCurrencyViewModel::class.java)

        ViewUtils.showAnimation(context, tvTotalBalance, R.anim.slide_in_right)
        ViewUtils.showAnimation(context, tvBalance, R.anim.slide_in_right)
        var str="12568.90877"
        DecimalFormat("##,###.00")
        // str=ft.format(str)
        str = NumberFormat.getInstance().format(155568.90)
        ViewUtils.changeTextColor(tvTotalBalance, (str + " " + getString(R.string.aco)), ContextCompat.getColor(requireContext(), R.color.white), 0, (str + " " + getString(R.string.aco)).length - 4, false)
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvList?.layoutManager = llm
        val  hash=HashMap<String,String>()
        hash[Constants.KEY_FIRST_NAME]="USD Coin (ERC20)"
        hash[Constants.KEY_LAST_NAME]="1,307.67 USDC"
        val  hash1=HashMap<String,String>()
        hash1[Constants.KEY_FIRST_NAME]="Bitcoin"
        hash1[Constants.KEY_LAST_NAME]="0.673 BTC"
        val  hash2=HashMap<String,String>()
        hash2[Constants.KEY_FIRST_NAME]="Alchemi.com Coin"
        hash2[Constants.KEY_LAST_NAME]="5,00 ACO"

        val dataList=ArrayList<HashMap<String,String>>()

        dataList.add(hash)
        dataList.add(hash1)
        dataList.add(hash2)
        val dividerItemDecoration = DividerItemDecoration(context, llm.orientation)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
        earnAdapter = EarnAdapter(dataList)
        rvList?.setHasFixedSize(true)
        rvList?.adapter = earnAdapter

        (activity as HomeActivity).tab_layout?.visibility=View.VISIBLE
//        requireActivity().ivBack.setOnClickListener {
//            (activity as HomeActivity).clearStack(1)
//        }
        bottomSheet()
        /***
         *  click buttons
         */
        rlEarnPlus.setOnClickListener {
            slideUpDownBottomSheetTermsConditions()
        }
        rlFlxibleAssets.setOnClickListener {
            val bundle =Bundle()
            (activity as HomeActivity).replaceFragment(FlexibleAssetsFragment(), "" + FlexibleAssetsFragment, bundle)
        }
        rlTermAssets.setOnClickListener {
            val bundle =Bundle()
            (activity as HomeActivity).replaceFragment(TermAssetsFragment(), "" + TermAssetsFragment, bundle)
        }
        rlTermAssets1.setOnClickListener {
            val bundle =Bundle()
            (activity as HomeActivity).replaceFragment(TermAssetsFragment(), "" + TermAssetsFragment, bundle)
        }
       // checkValidationAndGetCoinHistory()
    }

    override fun onResume() {
        super.onResume()
        if (arguments!=null){
            requireActivity().toolbar_title.text = getString(R.string.alchemi_earn)
        }
        requireActivity().ivBack.setImageResource(R.drawable.ic_back)
        requireActivity().ivBack.visibility=View.VISIBLE
        requireActivity().ivScan.visibility=View.GONE
        requireActivity().ivChart.visibility=View.GONE

    }

    override fun onDestroyView() {
        requireActivity().ivBack.setImageResource(R.drawable.ic_back)
        requireActivity().toolbar_title.text = getString(R.string.alchemi_capital_exchange)
        requireActivity().ivBack.visibility=View.VISIBLE
        requireActivity().ivScan.visibility=View.GONE
        requireActivity().ivChart.visibility=View.GONE
        super.onDestroyView()
    }

    fun bottomSheet(){
        /**
         * Bottom Sheet Behaviour
         */
        bottomSheetBehavior = BottomSheetBehavior.from<LinearLayout>(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

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


    /****
     *  Terms and conditions Bottom Sheet View
     */
    private fun slideUpDownBottomSheetTermsConditions() {
        val dialogView = layoutInflater.inflate(R.layout.alchemi_earns_terms_conditions_layout, null)
        dialog = BottomSheetDialog(requireContext(),R.style.SheetDialog)

        dialog?.setContentView(dialogView)
        YoYo.with(Techniques.SlideInUp).duration(500).playOn(dialogView)
        val ivClose = dialog?.ivCrossIcon as ImageView
        val ivBackIcon = dialog?.ivBackIcon as ImageView
        val llCheckbox = dialog?.llCheckbox as LinearLayout
        val llCheckbox_2 = dialog?.llCheckbox_2 as LinearLayout
        val llCheckbox_3 = dialog?.llCheckbox_3 as LinearLayout
        val llCheckbox_4 = dialog?.llCheckbox_4 as LinearLayout
        val tvCheckboxTxt=dialog?.tvCheckboxTxt as TextView
        val tvContinue=dialog?.tvContinue as TextView
        val checkboxTermsConditions=dialog?.checkboxTermsConditions as CheckBox
        val checkboxTermsConditions_2=dialog?.checkboxTermsConditions_2 as CheckBox
        val checkboxTermsConditions_3=dialog?.checkboxTermsConditions_3 as CheckBox
        val checkboxTermsConditions_4=dialog?.checkboxTermsConditions_4 as CheckBox
        changeTextColor(tvCheckboxTxt,getString(R.string.alchemi_earn_term_one),ContextCompat.getColor(requireContext(),R.color.colorGreen),70,getString(R.string.alchemi_earn_term_one).length)

        ivClose.setOnClickListener {
            YoYo.with(Techniques.SlideOutDown).duration(500).delay(500).playOn(dialogView)
            dialog?.dismiss()
        }
        llCheckbox.setOnClickListener {
             checkboxTermsConditions.performClick()
        }
        llCheckbox_3.setOnClickListener {
            checkboxTermsConditions_3.performClick()
        }
        llCheckbox_4.setOnClickListener {
            checkboxTermsConditions_4.performClick()
        }
        llCheckbox_2.setOnClickListener {
            checkboxTermsConditions_2.performClick()
        }
        ivBackIcon.setOnClickListener {
            dialog?.dismiss()
        }
        tvContinue.setOnClickListener {
            if (checkboxTermsConditions.isChecked && checkboxTermsConditions_2.isChecked&& checkboxTermsConditions_3.isChecked&& checkboxTermsConditions_4.isChecked){
                getCryptoCurrency(false)
            }else{
                Toast.makeText(requireContext(),getString(R.string.please_select_terms_and_conditions),Toast.LENGTH_SHORT).show()
            }
        }

        dialog?.show()

    }

    /**
     * Change Text Color
     */
    fun changeTextColor(textView: TextView, text: String, color: Int, startLength: Int, endLength: Int){
        val spannable = SpannableString(text)
        spannable.setSpan(ForegroundColorSpan(color), startLength, endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannable
    }

    /***
     *  get List of coin to buy
     */
    fun getCryptoCurrency(isSwipeRefresh: Boolean){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(requireContext())
            getCryptoCurrencyViewModel.getTopCryptoCurrency(Constants.Token + AlchemiApplication.alchemiApplication?.getToken(), view)!!.observe(viewLifecycleOwner, Observer { homeModel ->
                ViewUtils.dismissProgress()

                if (homeModel.code == Constants.CODE_200) {
                    if(homeModel.data.mktcapfull.isNotEmpty()) {
                        dialog?.dismiss()
                        slideUpDownBottomSheetDepositCurrency(homeModel.data.mktcapfull)
                        }
                } else {
                    ViewUtils.showSnackBar(view, "" + homeModel.message)
                }
            })
        }
    }
    /***
     * Deposit Currency bottom sheet for earn
     */
    private fun slideUpDownBottomSheetDepositCurrency(mktcapfull: List<TopGainer>) {
        val dialogView = layoutInflater.inflate(R.layout.deposit_currency_bottom_sheet, null)
        dialog = BottomSheetDialog(requireContext(),R.style.SheetDialog)

        dialog?.setContentView(dialogView)
        YoYo.with(Techniques.SlideInUp).duration(500).playOn(dialogView)
        val ivClose = dialog?.ivCrossIcon as ImageView
        val etSearch = dialog?.etSearchCoin as EditText
        val  tvCurrencyTitle= dialog?.tvCurrencyTitle as TextView
        val  tvEarnCurrencyTitle= dialog?.tvEarnCurrencyTitle as TextView
        val  tvEarnCurrency= dialog?.tvEarnCurrency as TextView

        tvCurrencyTitle.text=getString(R.string.alchemi_earn)
        etSearch.hint=getString(R.string.currency)
        tvEarnCurrencyTitle.visibility=View.VISIBLE
        tvEarnCurrency.visibility=View.VISIBLE

        val rvList = dialog?.rvList as RecyclerView
//        val bottomSheet= dialog.bottomSheet as LinearLayout
        val metrics= resources.displayMetrics
//        bottomSheetBehavior.peekHeight=metrics.heightPixels/3
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvList?.layoutManager = llm
        coinList=mktcapfull
        depositCurrencyAdapter = DepositCurrencyEarnAdapter(mktcapfull,this)
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

                            depositCurrencyAdapter = DepositCurrencyEarnAdapter(tempList, EarnFragment())
                            rvList.adapter = depositCurrencyAdapter
                            depositCurrencyAdapter.notifyDataSetChanged()

                        } else {
                            depositCurrencyAdapter = DepositCurrencyEarnAdapter(coinList!!, EarnFragment())
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
            dialog?.dismiss()
        }


        dialog?.show()

    }

    override fun getItemClicked(itemId: String?) {
        dialog?.dismiss()
        val bundle = Bundle()
        (activity as HomeActivity).replaceFragment(StakingPeriodFragment(), "" + StakingPeriodFragment, bundle)

    }
}




