package com.app.alchemi.views.fragments.dashboardFeatures

import Constants
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.models.CoinData
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.MyValueFormatter
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.CheckUserPinViewModel
import com.app.alchemi.viewModel.CoinHistoryViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

import kotlinx.android.synthetic.main.coin_detail_fragment_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.text.NumberFormat

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set


class CoinDetailFragment: Fragment(), OnChartValueSelectedListener {
    private lateinit var coinHistoryViewModel: CoinHistoryViewModel
    private lateinit var checkUserPinViewModel: CheckUserPinViewModel
    //1470960000L
    private val OUTPUT_TIME_FORMAT="hh:mm"
    private val OUTPUT_DATE_FORMAT="dd MMM"
    var request=1
    companion object {
        fun newInstance() = CoinDetailFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.coin_detail_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinHistoryViewModel = ViewModelProvider(this).get(CoinHistoryViewModel::class.java)
        checkUserPinViewModel =  ViewModelProvider(this).get(CheckUserPinViewModel::class.java)

        tvAbout.text=getString(R.string.about)+" "+requireArguments()[Constants.KEY_TITLE].toString()
        tvBuy.text=getString(R.string.buy)+" "+requireArguments()[Constants.KEY_DESCRIPTION].toString()
        val totalData= NumberFormat.getInstance().format(ViewUtils.roundOffValue(2, (requireArguments()[Constants.KEY_PRICE].toString()).toDouble()))
        ViewUtils.changeTextColor(tvPriceUSD, (getString(R.string.dollar_sign) + " " + totalData+ " " + getString(R.string.usd)), ContextCompat.getColor(requireContext(), R.color.white), 1, (getString(R.string.dollar_sign) + " " + totalData + " " + getString(R.string.usd)).length - 4, false)

        if (ViewUtils.roundOffValue(2, requireArguments()[Constants.KEY_PRICE_VARIATIONS].toString().toDouble())>0){
            tvPriceChangeUSD.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGreen))
            tvPriceChangeUSD.text="+"+ViewUtils.roundOffValue(2, requireArguments()[Constants.KEY_PRICE_VARIATIONS].toString().toDouble())+"%"
        }else{
            tvPriceChangeUSD.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorRed))
            tvPriceChangeUSD.text=""+ViewUtils.roundOffValue(2, requireArguments()[Constants.KEY_PRICE_VARIATIONS].toString().toDouble())+"%"
        }

        tvRankData.text = "#"+requireArguments()[Constants.KEY_RANK]
        if (""+requireArguments()[Constants.KEY_DESCRIPTION]=="BTC"){
            rlBTCPrice.visibility=View.GONE
        }else{
            rlBTCPrice.visibility=View.VISIBLE
        }

        /**
         *  Manage Click listener
         *
         */
        tvHours.setOnClickListener {
            selectHistoryType(tvHours)
        }
        tvDay.setOnClickListener {
            selectHistoryType(tvDay)
        }
        tvWeek.setOnClickListener {
            selectHistoryType(tvWeek)
        }
        tvMonth.setOnClickListener {
            selectHistoryType(tvMonth)
        }
        tvThreeMonth.setOnClickListener {
            selectHistoryType(tvThreeMonth)
        }
        tvSixMonths.setOnClickListener {
            selectHistoryType(tvSixMonths)
        }

        checkValidationAndGetCoinHistory()
    }

    override fun onResume() {
        super.onResume()
        if (arguments!=null){
            requireActivity().toolbar_title.text = requireArguments()[Constants.KEY_TITLE].toString()
        }
        requireActivity().ivBack.setImageResource(R.drawable.ic_back)
        requireActivity().ivBack.visibility=View.VISIBLE
        requireActivity().ivChat.visibility=View.GONE
        requireActivity().ivSettings.visibility=View.GONE
        requireActivity().rlAcx.visibility=View.GONE
        requireActivity().ivStar.visibility=View.VISIBLE
        requireActivity().ivStar.setOnClickListener {
            addRemoveFavouriteCoinAPI(request)
        }
    }

    override fun onDestroyView() {
        (activity as HomeActivity).ivChat.visibility = View.VISIBLE
        (activity as HomeActivity).rlAcx.visibility = View.VISIBLE
        (activity as HomeActivity).toolbar_title.text = getString(R.string.home)
        (activity as HomeActivity).ivBack.setImageResource(R.drawable.setting)
        (activity as HomeActivity).ivSettings.visibility = View.VISIBLE
        (activity as HomeActivity).ivBack.visibility = View.GONE
        requireActivity().ivStar.visibility=View.GONE
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
                    view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry)
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
                    try {
                        if (liveDataModel.data.historical_data.Data.isNotEmpty()) {
                            if (liveDataModel.data.historical_data.user_favourite){
                                requireActivity().ivStar.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_star))
                                request=2
                            }else {
                                requireActivity().ivStar.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_star_outline))
                                request=1
                            }

                            lineGraph()
                            setData(liveDataModel.data.historical_data.Data)

                        } else {
                            lineGraph()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    marketCapData.text=getString(R.string.dollar_sign)+""+ViewUtils.getFormatedNumber(liveDataModel.data.historical_data.market_cap.toLong())
                    tvCirculatingData.text=ViewUtils.getFormatedNumber(liveDataModel.data.historical_data.supply.toLong())+" "+requireArguments()[Constants.KEY_DESCRIPTION]
                    tvTotalSupplyData.text=ViewUtils.getFormatedNumber(liveDataModel.data.historical_data.supply.toLong())+" "+requireArguments()[Constants.KEY_DESCRIPTION]
                } else {
                    lineGraph()
                    ViewUtils.showSnackBar(view, "" + liveDataModel.message)
                }

            })
        }
    }
    fun lineGraph(){
          // // Chart Style // //

            chart.setBackgroundColor(Color.BLACK)
            // disable description text
            chart.description.isEnabled = true
            // enable touch gestures
            chart.setTouchEnabled(true)
            // set listeners
            chart.setOnChartValueSelectedListener(this)
            chart.setDrawGridBackground(false)
            // enable scaling and dragging
            chart.isDragEnabled = true
            chart.setScaleEnabled(false)
            chart.isScaleXEnabled = false
            chart.isScaleYEnabled = false

            // force pinch zoom along both axis
            chart.setPinchZoom(false)
            chart.isAutoScaleMinMaxEnabled=true

        // // Y-Axis Style // //
        val yAxis: YAxis = chart.axisRight
            chart.axisRight.isEnabled=true
            // disable dual axis (only use LEFT axis)
            chart.axisLeft.isEnabled = false

//            // horizontal grid lines
        chart.xAxis.enableGridDashedLine(9f, 9f, 0f)
            // axis range
            yAxis.axisMinimum = 0f

        // xaxis

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.typeface =  ResourcesCompat.getFont(requireContext(), R.font.roboto)
        xAxis.textSize = 10f
        xAxis.textColor = Color.WHITE
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(true)
        xAxis.setCenterAxisLabels(true)
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        //xAxis.granularity = 1f // one hour

        // draw limit lines behind data instead of on top
        yAxis.setDrawLimitLinesBehindData(false)
        xAxis.setDrawLimitLinesBehindData(false)
// yAxix

        val leftAxis = chart.axisRight
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto)
        leftAxis.textSize = 11f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawAxisLine(false)
        leftAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)


        // add data

        // draw points over time
        chart.animateX(1500)
    }
//count: Int, range: Float
    private fun setData(data: List<CoinData>) {
        val values = ArrayList<Entry>()

    val xValsOriginalMillis = ArrayList<Long>()

            if (data.isNotEmpty()) {
                for (i in data.indices) {
                    xValsOriginalMillis.add(data[i].time)
                    values.add(Entry(i.toFloat(), data[i].high.toFloat()))
                }
            }
        val set1: LineDataSet

            // create a dataset and give it a type
            set1 = LineDataSet(values, "")
            set1.setDrawIcons(false)

            // black lines and points
            set1.color = ContextCompat.getColor(requireContext(), R.color.colorYellow)
            set1.setCircleColor(ContextCompat.getColor(requireContext(), R.color.colorYellow))
            set1.circleHoleColor=ContextCompat.getColor(requireContext(), R.color.black)

            // line thickness and point size
            set1.lineWidth = 1f
            set1.circleRadius = 3f

            // draw points as solid circles
            set1.setDrawCircleHole(true)

            // customize legend entry
            set1.formLineWidth = 2f

            // text size of values
            set1.valueTextSize = 9f
            set1.setDrawValues(false)

            set1.mode=LineDataSet.Mode.CUBIC_BEZIER
            set1.setDrawCircles(true)
            set1.fillFormatter =
                IFillFormatter { _, _ -> chart.axisRight.axisMinimum }

            // set color of filled area

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets
                //    val custom1 = MyValueFormatter()
            // create a data object with the data sets
            val data = LineData(dataSets)
            // set data

            val xValsDateLabel = ArrayList<String>()

            for (i in xValsOriginalMillis) {

                if (Constants.KEY_HISTORY_TYPE=="hour"||Constants.KEY_HISTORY_TYPE=="day") {
                    xValsDateLabel.add("" + getDateFromUTCTimestamp(i, OUTPUT_TIME_FORMAT))
                }else{
                    xValsDateLabel.add("" + getDateFromUTCTimestamp(i, OUTPUT_DATE_FORMAT))
                }
            }

            chart.xAxis.valueFormatter = (MyValueFormatter(xValsDateLabel))
            chart.data = data
         val l1 = chart.legend
            l1.isEnabled = false
            chart.invalidate()

    }


    /**
     *  method manage click listener
     */

 fun selectHistoryType(view: View){
     when(view.id){
         R.id.tvHours -> {
             changeTextViewColor(tvHours)
             viewVisibility(viewHours)
             Constants.KEY_HISTORY_TYPE = "hour"
             getCoinHistory()
         }
         R.id.tvDay -> {
             changeTextViewColor(tvDay)
             viewVisibility(viewDay)
             Constants.KEY_HISTORY_TYPE = "day"
             getCoinHistory()
         }
         R.id.tvWeek -> {
             changeTextViewColor(tvWeek)
             viewVisibility(viewWeek)
             Constants.KEY_HISTORY_TYPE = "week"
             getCoinHistory()
         }
         R.id.tvMonth -> {
             changeTextViewColor(tvMonth)
             viewVisibility(viewMonth)
             Constants.KEY_HISTORY_TYPE = "month"
             getCoinHistory()
         }
         R.id.tvThreeMonth -> {
             changeTextViewColor(tvThreeMonth)
             viewVisibility(viewThreeMonth)
             Constants.KEY_HISTORY_TYPE = "3month"
             getCoinHistory()
         }
         R.id.tvSixMonths -> {
             changeTextViewColor(tvSixMonths)
             viewVisibility(viewSixMonths)
             Constants.KEY_HISTORY_TYPE = "6month"
             getCoinHistory()
         }else->{
         changeTextViewColor(tvHours)
         viewVisibility(viewHours)
         Constants.KEY_HISTORY_TYPE="hour"
         getCoinHistory()
         }

     }
 }
    private fun changeTextViewColor(textView: TextView){
        tvHours.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
        tvDay.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
        tvWeek.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
        tvMonth.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
        tvThreeMonth.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
        tvSixMonths.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        YoYo.with(Techniques.RubberBand)
                .duration(200)
                .playOn(textView)
    }
    private fun viewVisibility(view: View){
        viewHours.visibility=View.GONE
        viewDay.visibility=View.GONE
        viewWeek.visibility=View.GONE
        viewMonth.visibility=View.GONE
        viewThreeMonth.visibility=View.GONE
        viewSixMonths.visibility=View.GONE
        view.visibility=View.VISIBLE
    }
    override fun onValueSelected(e: Entry, h: Highlight?) {
        if (chart.yChartMax.toString().isNotEmpty()) {
            val totalData= NumberFormat.getInstance().format(ViewUtils.roundOffValue(2, (chart.yChartMax.toString()).toDouble()))
            ViewUtils.changeTextColor(tvPriceUSD, (getString(R.string.dollar_sign) + " " + totalData+ " " + getString(R.string.usd)), ContextCompat.getColor(requireContext(), R.color.white), 1, (getString(R.string.dollar_sign) + " " + totalData + " " + getString(R.string.usd)).length - 4, false)

            YoYo.with(Techniques.SlideInDown).duration(50).playOn(tvPriceUSD)
        }

    }

    override fun onNothingSelected() {
        Log.e("Nothing selected", "Nothing selected.")
    }

    /***
     *  Get Date fron utc Time stamp
     */
    fun getDateFromUTCTimestamp(mTimestamp: Long, mDateFormate: String?): String? {
        var date: String? = null
        try {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            cal.timeInMillis = mTimestamp * 1000L
            date = DateFormat.format(mDateFormate, cal.timeInMillis).toString()
            val formatter = SimpleDateFormat(mDateFormate, Locale.ENGLISH)
            val value = formatter.parse(date)
            val dateFormatter = SimpleDateFormat(mDateFormate, Locale.ENGLISH)
            dateFormatter.timeZone = TimeZone.getDefault()
            date = dateFormatter.format(value)

            return date
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return date
    }

    /***
     *  method to hit add/ remove favourite coin
     */
    fun addRemoveFavouriteCoinAPI(requestType:Int) {
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(llLayout, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry)
            )
        } else {
            val hashMap = HashMap<String, String>()
            if(requestType==1) {
                hashMap[Constants.KEY_NAME] = "" + requireArguments()[Constants.KEY_TITLE].toString()
            }
            hashMap[Constants.KEY_CODE]=""+requireArguments()[Constants.KEY_DESCRIPTION].toString()
            ViewUtils.showProgress(requireContext())
            checkUserPinViewModel.addRemoveFavouriteCoin(hashMap,  llLayout,Constants.Token + AlchemiApplication.alchemiApplication?.getToken(),requestType,

            )!!.observe(viewLifecycleOwner) { liveDataModel ->
                ViewUtils.dismissProgress()
                if (liveDataModel.code == Constants.CODE_200) {

                    if (requestType==1){
                        requireActivity().ivStar.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_star))
                        request=2
                    }else {
                        requireActivity().ivStar.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_star_outline))
                        request=1
                    }
                }

            }
        }
    }

}





