package com.app.alchemi.views.fragments.dashboardFeatures

import Constants
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.alchemi.R
import com.app.alchemi.utils.FragmentNavigation
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.CoinHistoryViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.FlexibleAssetsAdapter
import com.app.alchemi.views.adapters.FlexibleAssetsBenefitsAdapter
import com.app.alchemi.views.fragments.signup.PrivacyNoticeFragment
import com.app.alchemi.views.fragments.signup.TermsConditions
import kotlinx.android.synthetic.main.flexible_assests_detail_layout.*
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.recycler_view.rvList
import kotlinx.android.synthetic.main.recyclerview_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlin.collections.set


class FlexibleAssetsFragment: Fragment(){
    private lateinit var coinHistoryViewModel: CoinHistoryViewModel
    private lateinit var flexibleAssetsAdapter: FlexibleAssetsAdapter
    private lateinit var flexibleAssetsBenefitsAdapter: FlexibleAssetsBenefitsAdapter
    companion object {
        fun newInstance() = FlexibleAssetsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.flexible_assests_detail_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinHistoryViewModel = ViewModelProvider(this).get(CoinHistoryViewModel::class.java)
        ViewUtils.showAnimation(context, llInstructions, R.anim.slide_in_right)
        ViewUtils.showAnimation(context, rlStaking, R.anim.slide_in_right)

        ViewUtils.changeTextColorSize(tvEarningsTxt, (getString(R.string.earnings_calculated_text)), ContextCompat.getColor(requireContext(), R.color.white), 1, (getString(R.string.earnings_calculated_text)).length, false)
        singleTextView(tvEarningsTerms,getString(R.string.the_product_governed_by),getString(R.string.alchemi_earn_and_credit))

        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvList?.layoutManager = llm
        val  hash=HashMap<String, String>()
        hash[Constants.KEY_FIRST_NAME]="USD Coin (ERC20)"
        hash[Constants.KEY_LAST_NAME]="8% P.A."
        val  hash1=HashMap<String, String>()
        hash1[Constants.KEY_FIRST_NAME]="Bitcoin"
        hash1[Constants.KEY_LAST_NAME]="6.50% P.A."
        val  hash2=HashMap<String, String>()
        hash2[Constants.KEY_FIRST_NAME]="Alchemi.com Coin"
        hash2[Constants.KEY_LAST_NAME]="8.55% P.A."
        initSpinner()
        val dataList=ArrayList<HashMap<String, String>>()
        dataList.add(hash2)
        dataList.add(hash)
        dataList.add(hash1)
        flexibleAssetsAdapter = FlexibleAssetsAdapter(dataList)
        tvInfoRequired.visibility=View.GONE
        llLayout.setPadding(0,0,0,0)
        rvList?.setHasFixedSize(true)
        rvList?.adapter = flexibleAssetsAdapter

        val  hashb=HashMap<String, String>()
        hashb[Constants.KEY_FIRST_NAME]="Earning will be paid out"
        hashb[Constants.KEY_LAST_NAME]="7 Days*"
        val  hashb1=HashMap<String, String>()
        hashb1[Constants.KEY_FIRST_NAME]="Earning will be credited to"
        hashb1[Constants.KEY_LAST_NAME]="Alchemi Wallet"
        val dataListBenefits=ArrayList<HashMap<String, String>>()
        dataListBenefits.add(hashb)
        dataListBenefits.add(hashb1)
        val llmBenefits = LinearLayoutManager(context)
        llmBenefits.orientation = LinearLayoutManager.VERTICAL
        rvBenefits.layoutManager = llmBenefits
        flexibleAssetsBenefitsAdapter = FlexibleAssetsBenefitsAdapter(dataListBenefits)
        rvBenefits.adapter = flexibleAssetsBenefitsAdapter


        (activity as HomeActivity).tab_layout?.visibility=View.VISIBLE
//        requireActivity().ivBack.setOnClickListener {
//            (activity as HomeActivity).clearStack(1)
//        }

    }

    override fun onResume() {
        super.onResume()
        if (arguments!=null){
            requireActivity().toolbar_title.text = getString(R.string.alchemi_earn)
        }
        requireActivity().ivBack.visibility=View.VISIBLE
        requireActivity().ivScan.visibility=View.GONE
        requireActivity().ivChart.visibility=View.GONE

    }

    override fun onDestroyView() {
        requireActivity().ivBack.visibility=View.GONE
        requireActivity().ivScan.visibility=View.VISIBLE
        requireActivity().ivChart.visibility=View.VISIBLE
        super.onDestroyView()
    }


    /**
     *  Spinner
     */
    fun initSpinner(){
        // access the items of the list
        val languages = resources.getStringArray(R.array.SpinnerOptions)

        // access the spinner
        if (spinner != null) {
            val adapter = ArrayAdapter(requireContext(),
                    android.R.layout.simple_spinner_item, languages)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    (parent.getChildAt(0) as TextView).gravity=Gravity.END
                    (parent.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(requireContext(),R.color.colorYellow))
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    /***
     * Highlighter text og terms and conditions
     */


    private fun singleTextView(textView: TextView, text: String, termsConditions: String) {
        val spanText = SpannableStringBuilder()
        spanText.append(text)
        spanText.append(" $termsConditions")
        spanText.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
            }

            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = textPaint.linkColor // you can use custom color
                textPaint.isUnderlineText = false // this remove the underline
            }
        }, spanText.length - termsConditions.length, spanText.length, 0)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(spanText, TextView.BufferType.SPANNABLE)
    }
}




