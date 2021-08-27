package com.app.alchemi.views.fragments.dashboardFeatures

import Constants
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
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
import com.app.alchemi.viewModel.HomeViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.TrackAllCoinsAdapter
import com.app.alchemi.views.adapters.TrackCoinSpinnerAdapter
import com.app.alchemi.views.adapters.TrackStarredCoinsAdapter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.spinner_bottom_sheet.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.track_fragent_layout.*
import kotlinx.android.synthetic.main.track_fragent_layout.view.*


class TrackFragment: Fragment(),OnItemClickListener {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    var coinList: List<TopGainer>?=null
    var starredCoinList: List<TopGainer>?=null
    var dialog:BottomSheetDialog?=null
    private lateinit var trackCoinSpinnerAdapter: TrackCoinSpinnerAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var trackAllCoinsAdapter: TrackAllCoinsAdapter
    private lateinit var trackStarredCoinsAdapter: TrackStarredCoinsAdapter
    companion object {
        fun newInstance() = TrackFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.track_fragent_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        ViewUtils.showAnimation(context,llTop,R.anim.slide_in_right)
        ViewUtils.showAnimation(context,rlStarredList,R.anim.slide_in_right)
        ViewUtils.showAnimation(context,tvAllCoins,R.anim.slide_in_right)
        etSearch.text.clear()
        swipeRefresh.setOnRefreshListener{
            trackCoins(true)
            etSearch.text.clear()
            try {
                ViewUtils.hideKeyBoard(requireActivity())
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        /**
         * Bottom Sheet Behaviour
         */
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                bottomSheet.animate().y((if (slideOffset <= 0) view.y + bottomSheetBehavior.peekHeight - bottomSheet.height else view.height - bottomSheet.height).toFloat()).setDuration(0).start()
                YoYo.with(Techniques.SlideInUp)
                        .duration(1000)
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
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                    }
                }
            }
        })
        rlSpinnerOptions.setOnClickListener {
            slideUpDownBottomSheet()
        }


        val llmTrackCoins= LinearLayoutManager(context)
        llmTrackCoins.orientation = LinearLayoutManager.VERTICAL
        rvTrackCoins?.layoutManager=llmTrackCoins
        val llmTrackStarredCoins = LinearLayoutManager(context)
        llmTrackStarredCoins.orientation = LinearLayoutManager.VERTICAL
        rvStarredList?.layoutManager = llmTrackStarredCoins
        trackCoins(false)


        val dividerItemDecoration = DividerItemDecoration(rvTrackCoins.context,
                llmTrackCoins.orientation)
        dividerItemDecoration.setDrawable(getDrawable(requireContext(),R.drawable.divider)!!)

        rvTrackCoins.addItemDecoration(dividerItemDecoration)

        val dividerItemDecoration1 = DividerItemDecoration(rvTrackCoins.context,
                llmTrackStarredCoins.orientation)
        dividerItemDecoration1.setDrawable(getDrawable(requireContext(),R.drawable.divider)!!)
        rvStarredList.addItemDecoration(dividerItemDecoration1)
//        try {
//            if (activity!=null) {
//                (activity as HomeActivity).ivBack.setOnClickListener {
//                    //  SettingsFragment
//                    (activity as HomeActivity).selectPage(0)
//                }
//            }
//        }catch (e: Exception){
//            e.printStackTrace()
//        }
        searchCoinByName(view)
       activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {

            val rect = Rect().apply { view.getWindowVisibleDisplayFrame(this) }

            val screenHeight = view.height

            // rect.bottom is the position above soft keypad or device button.
            // if keypad is shown, the rect.bottom is smaller than that before.
            val keypadHeight = screenHeight - rect.bottom

            // 0.15 ratio is perhaps enough to determine keypad height.
            if (keypadHeight > screenHeight * 0.15) {
            //   Log.e("test>>track","open")
            } else {
              //  Log.e("test>>track","close")
            }
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    /**
     *  Get Track list API
     *
     */
    fun trackCoins(isSwipeReFresh:Boolean){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            if (!isSwipeReFresh) {
                ViewUtils.showProgress(requireContext())
            }
            homeViewModel.getTrackList(Constants.Token + AlchemiApplication.alchemiApplication?.getToken(), view)!!.observe(viewLifecycleOwner, Observer { homeModel ->
                ViewUtils.dismissProgress()
                swipeRefresh.isRefreshing=false
                if (homeModel.code == Constants.CODE_200) {
                    if (homeModel.data.coins.isNotEmpty()) {
                        coinList = homeModel.data.coins.filter {
                            !it.user_favourite
                            // .startsWith(s.trim(), true)
                        }
                        starredCoinList= homeModel.data.coins.filter {
                            it.user_favourite
                        }
                        if(starredCoinList?.isEmpty() == true){
                            rvStarredList.visibility=View.GONE

                        }else{
                            rvStarredList.visibility=View.VISIBLE
                        }
                    }
                    trackStarredCoinsAdapter =TrackStarredCoinsAdapter(starredCoinList!!)
                    rvStarredList?.adapter = trackStarredCoinsAdapter
                    trackAllCoinsAdapter = TrackAllCoinsAdapter(coinList!!)
                    rvTrackCoins?.adapter = trackAllCoinsAdapter
                } else {
                    ViewUtils.showSnackBar(view, "" + homeModel.message)
                }
            })
        }
    }
    private fun slideUpDownBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.spinner_bottom_sheet, null)
        dialog = BottomSheetDialog(requireContext())
        dialog?.setContentView(dialogView)
        val ivClose = dialog?.ivClose as ImageView
        val rvList=dialog?.rvList as RecyclerView
        ivClose.setOnClickListener {
           closeDialog()

        }

        val optionList = arrayOf<String>(
                getString(R.string.largeCap),
                getString(R.string.small_cap),
                getString(R.string.top_performance),
                getString(R.string.worst_performance),
                getString(R.string.volume_ascending),
                getString(R.string.volume_descending),
                getString(R.string.alphabetical)
        )
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvList.layoutManager = llm
        trackCoinSpinnerAdapter = TrackCoinSpinnerAdapter(optionList, this)
        rvList.setHasFixedSize(true)
        rvList.adapter = trackCoinSpinnerAdapter
        if (dialog!=null){
            YoYo.with(Techniques.SlideInUp)
                    .duration(1000)
                    .playOn(bottomSheet)
        }
        dialog?.show()
    }

    override fun getItemClicked(itemId: String?) {
        when (itemId){
            "0" -> {
                tvFilterName.text = getString(R.string.largeCap)
                closeDialog()
                if (coinList!=null && coinList?.isNotEmpty()==true) {
                    val data= coinList?.sortedByDescending { it.PRICE }
                    val data2= starredCoinList?.sortedByDescending { it.PRICE }
                    sorListOrderNotify(data, data2)
                }
            }
            "1" -> {
                tvFilterName.text = getString(R.string.small_cap)
                closeDialog()
                if (coinList!=null && coinList?.isNotEmpty()==true) {
                    val data= coinList?.sortedBy { it.PRICE }
                    val data2= starredCoinList?.sortedBy { it.PRICE }
                    sorListOrderNotify(data, data2)
                }
            }
            "2" -> {
                tvFilterName.text = getString(R.string.top_performance)
                val data= coinList?.sortedByDescending { it.SUPPLY }
                val data2= starredCoinList?.sortedByDescending { it.SUPPLY }
                sorListOrderNotify(data, data2)
                closeDialog()
            }
            "3" -> {
                tvFilterName.text = getString(R.string.worst_performance)
                val data= coinList?.sortedBy { it.SUPPLY }
                val data2= starredCoinList?.sortedBy { it.SUPPLY }
                sorListOrderNotify(data, data2)
                closeDialog()
            }
            "4" -> {
                tvFilterName.text = getString(R.string.volume_ascending)
                closeDialog()
                if (coinList!=null && coinList?.isNotEmpty()==true) {
                    val data= coinList?.sortedBy { it.VOLUME24HOUR }
                    val data2= starredCoinList?.sortedBy { it.VOLUME24HOUR }
                    sorListOrderNotify(data, data2)
                }
            }
            "5" -> {
                tvFilterName.text = getString(R.string.volume_descending)
                closeDialog()
                if (coinList!=null && coinList?.isNotEmpty()==true) {
                    val data= coinList?.sortedByDescending { it.VOLUME24HOUR }
                    val data2= starredCoinList?.sortedByDescending { it.VOLUME24HOUR }
                    sorListOrderNotify(data, data2)
                }
            }
            "6" -> {
                tvFilterName.text = getString(R.string.alphabetical)
                closeDialog()
                if (coinList!=null && coinList?.isNotEmpty()==true) {
                   val data= coinList?.sortedByDescending { it.FULLNAME }
                    val data2= starredCoinList?.sortedByDescending { it.FULLNAME }
                    sorListOrderNotify(data,data2)

                }
            }

        }
    }

    fun closeDialog(){
        if (dialog!=null){
                YoYo.with(Techniques.SlideOutDown)
                        .duration(1000)
                        .playOn(bottomSheet)
            dialog?.dismiss()
        }
    }


    override fun onDestroyView() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams. SOFT_INPUT_ADJUST_RESIZE)
        WindowCompat.setDecorFitsSystemWindows(activity?.window!!, false)
        ViewCompat.setOnApplyWindowInsetsListener(requireView()) { _, insets ->
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            requireView().setPadding(0, 0, 0, imeHeight)
            insets
        }
        etSearch.text.clear()
        super.onDestroyView()
    }

    /***
     *  Method to search data
     */
    fun searchCoinByName(view:View){
        view.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                try {
                    if (coinList?.isNotEmpty() == true || coinList != null) {
                        if (s.trim().isNotEmpty()) {
                            //var tempList: List<String> = coinList.filter { s -> s == "January" }
                            var tempList = coinList!!.filter {
                               it.FULLNAME
                                        .startsWith(s.trim(), true)

                            }
                            var tempList1 = starredCoinList!!.filter {
                                it.FULLNAME
                                        .startsWith(s.trim(), true)

                            }
                            trackStarredCoinsAdapter = TrackStarredCoinsAdapter(tempList1)
                            rvStarredList?.adapter = trackStarredCoinsAdapter
                            trackStarredCoinsAdapter.notifyDataSetChanged()
                            trackAllCoinsAdapter = TrackAllCoinsAdapter(tempList)
                            rvTrackCoins?.adapter = trackAllCoinsAdapter
                            trackAllCoinsAdapter.notifyDataSetChanged()

                        } else {
                            trackAllCoinsAdapter = TrackAllCoinsAdapter(coinList!!)
                            rvTrackCoins?.adapter = trackAllCoinsAdapter
                            trackAllCoinsAdapter.notifyDataSetChanged()
                            trackStarredCoinsAdapter = TrackStarredCoinsAdapter(starredCoinList!!)
                            rvStarredList?.adapter = trackStarredCoinsAdapter
                            trackStarredCoinsAdapter.notifyDataSetChanged()

                        }
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }
        })
    }
    fun sorListOrderNotify(data: List<TopGainer>?, data2: List<TopGainer>?) {
        etSearch.text.clear()
        try {
            ViewUtils.hideKeyBoard(requireActivity())
        }catch (e:Exception){
            e.printStackTrace()
        }
            if(starredCoinList?.isEmpty() == true){
                rvStarredList.visibility=View.GONE

            }else {
                tvStarred.visibility = View.VISIBLE
                rvStarredList.visibility = View.VISIBLE
            }
        trackAllCoinsAdapter = TrackAllCoinsAdapter(data!!)
        rvTrackCoins?.adapter = trackAllCoinsAdapter
        trackAllCoinsAdapter.notifyDataSetChanged()
        trackStarredCoinsAdapter =TrackStarredCoinsAdapter(data2!!)
        rvStarredList?.adapter = trackStarredCoinsAdapter
        trackStarredCoinsAdapter.notifyDataSetChanged()
    }

}


