package com.app.alchemi.views.fragments.dashboardFeatures

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.alchemi.R
import com.app.alchemi.models.ExpandableListData.data
import com.app.alchemi.utils.Target
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.utils.navigateTo
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.CustomExpandableListAdapter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.accounts_fragment_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.text.DecimalFormat
import java.text.NumberFormat


class AccountsFragment: Fragment() {
    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null
    private var titleList: List<String>? = null
    var alert: AlertDialog?=null
    companion object {
        fun newInstance() = AccountsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.accounts_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewUtils.showAnimation(context, tvTotalBalance, R.anim.slide_in_right)
        ViewUtils.showAnimation(context, tvBalance, R.anim.slide_in_right)
        YoYo.with(Techniques.SlideInUp).duration(300).playOn(ivShowHideText)
        var str = "12568.90877"
        val ft = DecimalFormat("##,###.00")
        // str=ft.format(str)
        str = NumberFormat.getInstance().format(155568.90)

        tvAmount.text = "+$" + NumberFormat.getInstance().format(1007.96)
        ViewUtils.changeTextColor(tvTotalBalance, (getString(R.string.dollar_sign) + " " + str + " " + getString(R.string.usd)), ContextCompat.getColor(requireContext(), R.color.white), 1, (getString(R.string.dollar_sign) + " " + str + " " + getString(R.string.usd)).length - 4, false)
        /***
         *  To show and hide text
         */
        tvTotalBalance.transformationMethod = HideReturnsTransformationMethod.getInstance()
        ivShowHideText.setImageResource(R.drawable.ic_show)
        ivShowHideText.setOnClickListener {
            ShowHideText(ivShowHideText)
        }
        requireActivity().ivScan.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            ) {
                val bundle = Bundle()
                bundle.putString(Constants.KEY_TITLE,"2")
                navigateTo(requireContext(), Target.ACX_PAY)
            } else {
                checkPermissions()
            }

        }
        /***
         *  click to open Pie chart
         */
        requireActivity().ivChart.setOnClickListener {
            val bundle = Bundle()
            (activity as HomeActivity).replaceFragment(AccountAllocationFragment(), "" + AccountAllocationFragment, bundle)
        }
        expandableListView = view.findViewById(R.id.expendableList)
        if (expandableListView != null) {

            val listData = data
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableListAdapter(requireContext(), titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)
            expandableListView!!.refreshDrawableState()
            expandableListView?.collapseGroup(1)


            expandableListView!!.setOnGroupExpandListener { groupPosition ->

            }
            expandableListView!!.setOnGroupCollapseListener { groupPosition ->
            }
            expandableListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ -> false
            }
        }


    }






    fun ShowHideText(view:View) {

        if (view.id == R.id.ivShowHideText) {

            if (tvTotalBalance.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
                ivShowHideText.setImageResource(R.drawable.ic_show)
                //Show Password
                tvTotalBalance.transformationMethod = HideReturnsTransformationMethod.getInstance()
                tvAmount.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                ivShowHideText.setImageResource(R.drawable.ic_hide)

                //Hide Password
                tvTotalBalance.transformationMethod = PasswordTransformationMethod.getInstance()
                tvAmount.transformationMethod = PasswordTransformationMethod.getInstance()

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    /**
     *  Check Camera permissions
     */
    fun checkPermissions(){

        Dexter.withContext(activity)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            val bundle = Bundle()
                            bundle.putString(Constants.KEY_TITLE,"2")
                            navigateTo(requireContext(), Target.ACX_PAY)
//                            (activity as HomeActivity).replaceFragment(QRCodeFragment(), "" + QRCodeFragment, bundle)

                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog(context!!)
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

}