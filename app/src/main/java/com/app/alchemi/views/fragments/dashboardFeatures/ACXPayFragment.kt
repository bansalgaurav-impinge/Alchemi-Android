package com.app.alchemi.views.fragments.dashboardFeatures

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.alchemi.R
import com.app.alchemi.utils.Target
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.utils.navigateTo
import com.app.alchemi.viewModel.GetTopNewsViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.TransactionAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.acx_pay_layout.*
import kotlinx.android.synthetic.main.acx_pay_layout.ivScan
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class ACXPayFragment: Fragment() {
    private lateinit var getTopNewsViewModel: GetTopNewsViewModel
    private lateinit var transactionAdapter: TransactionAdapter
    var alert: AlertDialog? = null

    companion object {
        fun newInstance() = ACXFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.acx_pay_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewUtils.showAnimation(requireContext(), llLayout, R.anim.slide_in_right)
        ViewUtils.showAnimation(requireContext(), llGift, R.anim.slide_in_right)
        tvRefer.setOnClickListener {
            navigateTo(requireContext(), Target.REFERRAL_CODE)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
        }
        tvReferView.setOnClickListener {
            tvReferView.visibility = View.GONE
            tvRefer.visibility = View.VISIBLE
            ViewUtils.showAnimation(requireContext(), tvRefer, R.anim.slide_in_right)
        }
        ivClose.setOnClickListener {
            (activity as HomeActivity).clearStack(1)
        }
        ivScan.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                val bundle = Bundle()
                bundle.putString(Constants.KEY_TITLE, "1")
                navigateTo(requireContext(), Target.ACX_PAY)
            } else {
                checkPermissions()
            }
        }
        ivSend.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                val bundle = Bundle()
                bundle.putString(Constants.KEY_TITLE, "1")
                navigateTo(requireContext(), Target.ACX_PAY)
            } else {
                checkPermissions()
            }
        }


        changeTextColor(tvGiftTxt, getString(R.string.refer_alchemi_to_friends), ContextCompat.getColor(requireContext(), R.color.black), 6, 13, 16, 28)
        getTopNewsViewModel = ViewModelProvider(this).get(GetTopNewsViewModel::class.java)
        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL
        rvList?.layoutManager = llm
        val hash = HashMap<String, String>()
        hash[Constants.KEY_FIRST_NAME] = "Bitcoin Received"
        hash[Constants.KEY_LAST_NAME] = "0.0067 BTC"
        hash[Constants.KEY_STATUS] = "1"
        val hash1 = HashMap<String, String>()
        hash1[Constants.KEY_FIRST_NAME] = "Tron Sent"
        hash1[Constants.KEY_LAST_NAME] = "0.0063 TRX"
        hash1[Constants.KEY_STATUS] = "2"
        val hash2 = HashMap<String, String>()
        hash2[Constants.KEY_FIRST_NAME] = "Binance Transfer"
        hash2[Constants.KEY_LAST_NAME] = "10,000.00 BNB"
        hash2[Constants.KEY_STATUS] = "3"
        val hash3 = HashMap<String, String>()
        hash3[Constants.KEY_FIRST_NAME] = "Bitcoin Deposit"
        hash3[Constants.KEY_LAST_NAME] = "27,000.00 BTC"
        hash3[Constants.KEY_STATUS] = "1"
        val dataList = ArrayList<HashMap<String, String>>()
        dataList.add(hash)
        dataList.add(hash1)
        dataList.add(hash2)
        dataList.add(hash3)
        transactionAdapter = TransactionAdapter(dataList)
        rvList?.adapter = transactionAdapter
//        swipeRefresh.setOnRefreshListener {
//            getTopNews(true)              // refresh your list contents somehow
//        }
    }

    override fun onResume() {
        super.onResume()
        tvReferView.visibility = View.VISIBLE
        tvRefer.visibility = View.GONE
        (activity as HomeActivity).tab_layout?.visibility = View.GONE
        (activity as HomeActivity).toolbar?.visibility = View.GONE
        // getTopNews(false)
    }

    fun changeTextColor(textView: TextView, text: String, color: Int, startLength: Int, endLength: Int, startLength2: Int, endLength2: Int) {
        val spannable = SpannableString(text)
        spannable.setSpan(RelativeSizeSpan(1.25f), startLength, endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(RelativeSizeSpan(1.25f), startLength2, endLength2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val font =ResourcesCompat.getFont(requireContext(), R.font.roboto_bold)
//        val font = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            requireContext().resources.getFont(R.font.roboto_bold)
//        }
        spannable.setSpan(StyleSpan(Typeface.BOLD), startLength, endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(font,startLength, endLength,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(font,startLength2, endLength2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(StyleSpan(Typeface.BOLD), startLength, endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(StyleSpan(Typeface.BOLD), startLength2, endLength2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(color), startLength, endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(color), startLength2, endLength2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannable
    }

    /***
     * Check Camera Permissions
     */
    fun checkPermissions() {

        Dexter.withContext(requireContext())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            val bundle = Bundle()
                            bundle.putString(Constants.KEY_TITLE, "1")
                            navigateTo(requireContext(), Target.ACX_PAY)
                            //   (activity as HomeActivity).replaceFragment(QRCodeFragment(), "" + QRCodeFragment, bundle)

                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog(requireContext())
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            permissions: List<PermissionRequest>,
                            token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }

                }).check()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        super.startActivityForResult(intent, 101)
    }

    /***
     *  Show settings Dialog
     */
    fun showSettingsDialog(context: Context) {
        if (alert != null && alert!!.isShowing) {
            alert!!.dismiss()
        }
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(context.resources.getString(R.string.dialog_permission_title))
        dialogBuilder.setMessage(context.resources.getString(R.string.dialog_permission_message))
                .setCancelable(false)
                .setPositiveButton(context.resources.getString(R.string.go_to_settings)) { dialog, id ->
                    dialog.cancel()
                    openSettings()

                }
                .setNegativeButton(context.resources.getString(R.string.cancel)) { dialog, id ->
                    dialog.cancel()

                }
        alert = dialogBuilder.create()
        alert!!.show()
    }

    override fun onDestroyView() {
        (activity as HomeActivity).tab_layout?.visibility = View.VISIBLE
        (activity as HomeActivity).toolbar?.visibility = View.VISIBLE
        super.onDestroyView()
    }

}