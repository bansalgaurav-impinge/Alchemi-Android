package com.app.alchemi.views.fragments.dashboardFeatures

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.provider.Settings

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.app.alchemi.R
import com.app.alchemi.utils.Target
import com.app.alchemi.utils.navigateTo
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.qr_code_reader_fragment_layout.*


class QRCodeFragment: AppCompatActivity() {
    var alert: AlertDialog?=null
    var codeScanner:CodeScanner?=null
    companion object {
        fun newInstance() = QRCodeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_code_reader_fragment_layout)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        ) {
            initQRCodeReaderView()
        } else {
            checkPermissions()
        }
       ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    /**
     *  Init QR Reader
     */
    private fun initQRCodeReaderView() {

        codeScanner = CodeScanner(this, scannerView)
        codeScanner?.startPreview()
        codeScanner?.decodeCallback = DecodeCallback {
            runOnUiThread {
                if (it.text.isNotEmpty()){
                    val  bundle= Bundle()
                    bundle.putSerializable(Constants.KEY_TITLE, it.text)
                    navigateTo(this,Target.AMOUNT,bundle)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()

        toolbar_title.text=getString(R.string.qr_code)
        codeScanner?.startPreview()
    }
    override fun onPause() {
        super.onPause()
        if (codeScanner != null) {
            codeScanner?.releaseResources()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    /**
     * check Cmera permissions
     */
    fun checkPermissions(){

        Dexter.withContext(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        initQRCodeReaderView()
                        if (codeScanner != null) {
                            codeScanner?.startPreview()
                        }
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog(this@QRCodeFragment)
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

    /***
     * navigating user to app settings
     */

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
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