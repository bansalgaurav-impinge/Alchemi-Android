package com.app.alchemi.views.fragments.dashboardFeatures

import Constants
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.CoinHistoryViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.AccountAllocationAdapter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.account_allocation_fragment_layout.*
import kotlinx.android.synthetic.main.account_allocation_fragment_layout.chart
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set


class AccountAllocationFragment: Fragment(){
    private lateinit var coinHistoryViewModel: CoinHistoryViewModel
    private lateinit var accountAllocationAdapter: AccountAllocationAdapter
    //1470960000L
    private val OUTPUT_TIME_FORMAT="hh:mm"
    private val OUTPUT_DATE_FORMAT="dd MMM"
    companion object {
        fun newInstance() = AccountAllocationFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.account_allocation_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinHistoryViewModel = ViewModelProvider(this).get(CoinHistoryViewModel::class.java)
        ViewUtils.showAnimation(context, tvTotalBalance, R.anim.slide_in_right)
        ViewUtils.showAnimation(context, tvBalance, R.anim.slide_in_right)
        var str="12568.90877"
        val ft= DecimalFormat("##,###.00")
        // str=ft.format(str)
        str = NumberFormat.getInstance().format(155568.90)
        ViewUtils.changeTextColor(tvTotalBalance, (getString(R.string.dollar_sign) + " " + str + " " + getString(R.string.usd)), ContextCompat.getColor(requireContext(), R.color.white), 1, (getString(R.string.dollar_sign) + " " + str + " " + getString(R.string.usd)).length - 4, false)
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvList?.layoutManager = llm
        val  hash=HashMap<String,String>()
        hash[Constants.KEY_FIRST_NAME]="Alchemi.com Coin"
        val  hash1=HashMap<String,String>()
        hash1[Constants.KEY_FIRST_NAME]="Bitcoin"

        val  hash2=HashMap<String,String>()
        hash2[Constants.KEY_FIRST_NAME]="Yearn Finance"

        val  hash3=HashMap<String,String>()
        hash3[Constants.KEY_FIRST_NAME]="Harmony"

        val dataList=ArrayList<HashMap<String,String>>()

        dataList.add(hash)
        dataList.add(hash1)
        dataList.add(hash2)
        dataList.add(hash3)
        accountAllocationAdapter = AccountAllocationAdapter(dataList)
        rvList?.setHasFixedSize(true)
        rvList?.adapter = accountAllocationAdapter
        (activity as HomeActivity).tab_layout?.visibility=View.VISIBLE
        requireActivity().ivBack.setOnClickListener {
            (activity as HomeActivity).clearStack(1)
        }
       // checkValidationAndGetCoinHistory()
    }

    override fun onResume() {
        super.onResume()
        if (arguments!=null){
            requireActivity().toolbar_title.text = getString(R.string.allocation)
        }
        requireActivity().ivBack.setImageResource(R.drawable.ic_back)
        requireActivity().ivBack.visibility=View.VISIBLE
        requireActivity().ivScan.visibility=View.GONE
        requireActivity().ivChart.visibility=View.GONE
        pieChart()
    }

    override fun onDestroyView() {
        requireActivity().ivBack.setImageResource(R.drawable.ic_back)
        requireActivity().ivBack.visibility=View.GONE
        requireActivity().ivScan.visibility=View.VISIBLE
        requireActivity().ivChart.visibility=View.VISIBLE
        requireActivity().toolbar_title.text = getString(R.string.account)
        super.onDestroyView()
    }


    fun checkValidationAndGetCoinHistory(){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(
                    view,
                    getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry)
            )
        }
        else {
            getCoinHistory()
        }
    }

    /***
     * Get History Of Particular COIN
     */
    fun getCoinHistory() {
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(
                    view,
                    getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry)
            )
        } else {
            val hashMap = HashMap<String, String>()
            hashMap[Constants.KEY_CRYPTO] = ""+requireArguments()[Constants.KEY_DESCRIPTION].toString()
            hashMap[Constants.KEY_LIMIT]=Constants.KEY_HISTORY_TYPE
            ViewUtils.showProgress(requireContext())
            coinHistoryViewModel.getCoinHistory(
                    hashMap,
                    Constants.Token + AlchemiApplication.alchemiApplication?.getToken(),
                    view
            )!!.observe(viewLifecycleOwner, { liveDataModel ->
                ViewUtils.dismissProgress()
                if (liveDataModel.code == Constants.CODE_200) {

                } else {
                    ViewUtils.showSnackBar(view, "" + liveDataModel.message)
                }

            })
        }
    }
    fun pieChart(){

        chart.setUsePercentValues(false)
        chart.description.isEnabled = false

        chart.dragDecelerationFrictionCoef = 0.95f

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.BLACK)

        chart.setTransparentCircleColor(Color.BLACK)
        chart.setTransparentCircleAlpha(110)

        chart.holeRadius= 60f
        chart.transparentCircleRadius = 50f

        chart.setDrawCenterText(false)

        chart.rotationAngle = 0f

        chart.isRotationEnabled = false
        chart.isHighlightPerTapEnabled = false
        chart.description.isEnabled = false


        chart.animateY(1400, Easing.EaseInOutQuad)

        val l: Legend = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL

        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f
        chart.legend.isEnabled=false

        chart.setEntryLabelColor(Color.WHITE)
        chart.setEntryLabelTypeface(ResourcesCompat.getFont(requireContext(), R.font.roboto))
        chart.setEntryLabelTextSize(12f)
        chart.setDrawEntryLabels(false)
        setData(4,10F)
    }
    private fun setData(count: Int, range: Float) {
        val entries = ArrayList<PieEntry>()


            entries.add(PieEntry(59.19f,1))
            entries.add(PieEntry(22.35f,2))
            entries.add(PieEntry(8.79f,3))
            entries.add(PieEntry(9.68f,4))

        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f
        dataSet.setDrawValues(false)

        val colors = ArrayList<Int>()
        colors.add(ContextCompat.getColor(requireContext(),R.color.colorYellow))
        colors.add(ContextCompat.getColor(requireContext(),R.color.colorPieRed))
        colors.add(ContextCompat.getColor(requireContext(),R.color.colorPieGreen))
        colors.add(ContextCompat.getColor(requireContext(),R.color.colorPieBlue))

        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        data.setValueTypeface(ResourcesCompat.getFont(requireContext(), R.font.roboto))
        chart.data = data


        // undo all highlights
        chart.highlightValues(null)
        chart.invalidate()
    }

}




