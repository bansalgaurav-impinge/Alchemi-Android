package com.app.alchemi.views.fragments.dashboardFeatures

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.UploadDocumentsViewModel
import com.app.alchemi.viewModel.UserDetailViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.activities.ImagePickerActivity
import com.app.alchemi.views.fragments.signup.FullLegalNameFragment
import com.app.alchemi.views.fragments.signup.PhoneNumberFragment


import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.edit_profile_fragment_layout.*
import kotlinx.android.synthetic.main.edit_profile_fragment_layout.ivProfile
import kotlinx.android.synthetic.main.edit_profile_fragment_layout.scrollView
import kotlinx.android.synthetic.main.edit_profile_fragment_layout.tvEmail
import kotlinx.android.synthetic.main.edit_profile_fragment_layout.tvName
import kotlinx.android.synthetic.main.toolbar_layout.*


class EditProfileFragment: Fragment() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    internal lateinit var userDetailViewModel: UserDetailViewModel
    internal lateinit var uploadDocumentsViewModel: UploadDocumentsViewModel
    var phone=""
    var alert: AlertDialog?=null
    var imagePath=""

    companion object {
        fun newInstance() = EditProfileFragment()
        const val REQUEST_IMAGE = 100
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.edit_profile_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ImagePickerActivity.clearCache(requireContext())
        userDetailViewModel = ViewModelProvider(this).get(UserDetailViewModel::class.java)
        uploadDocumentsViewModel= ViewModelProvider(this).get(UploadDocumentsViewModel::class.java)

        getUserDetail()
        /**
         * Edit Profile Image Click
         */

        ivEditIcon.setOnClickListener {
            slideUpDownBottomSheet()
        }
        ivEditUserIcon.setOnClickListener {
            Constants.isEditProfile=true
            val bundle= Bundle()
            if (tvUserName.text.isNotEmpty()){
            bundle.putSerializable(Constants.KEY_FIRST_NAME,tvUserName.text.split(" ")[0])
            bundle.putSerializable(Constants.KEY_LAST_NAME,tvUserName.text.split(" ")[1])
            }
            (activity as HomeActivity).replaceFragment(FullLegalNameFragment(),""+FullLegalNameFragment,bundle)

        }
        tvUserName.setOnClickListener {
            ivEditUserIcon.performClick()
        }
        ivEditPhoneIcon.setOnClickListener {
            Constants.isEditProfile=true
            val bundle=Bundle()
            bundle.putSerializable("phone",tvPhoneNumber.text.toString())
            (activity as HomeActivity).replaceFragment(PhoneNumberFragment(),""+PhoneNumberFragment,bundle)
        }
        ivEditEmailIcon.setOnClickListener {
            Constants.isEditProfile=true
            val bundle=Bundle()
            bundle.putSerializable(Constants.KEY_EMAIL,tvEmail.text.toString())
            (activity as HomeActivity).replaceFragment(UpdateEmailFragment(),""+UpdateEmailFragment,bundle)
        }
//        (activity as HomeActivity).ivBack.setOnClickListener {
//            (activity as HomeActivity).onBackPressed()
//        }
        /**
         * Bottom Sheet Behaviour
         */
        bottomSheetBehavior = BottomSheetBehavior.from<LinearLayout>(bottomSheet)
        bottomSheet.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

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

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).tab_layout?.visibility=View.GONE
        Constants.isEditProfile=false
        requireActivity().toolbar_title.text  = getString(R.string.edit_profile)
        requireActivity().ivChat.visibility=View.GONE
        requireActivity().rlAcx.visibility=View.GONE

    }



    override fun onDestroyView() {
        (activity as HomeActivity).tab_layout?.visibility=View.VISIBLE
        super.onDestroyView()
    }

    /***
     * Slide Up bottomsheet
     */
    private fun slideUpDownBottomSheet() {
        var dialogView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        var dialog = BottomSheetDialog(requireContext(),R.style.SheetDialog)

        dialog.setContentView(dialogView)
        var ivClose = dialog.ivCrossIcon as ImageView
        var tvGallery = dialog.tvGallery as TextView
        var tvCamera = dialog.tvCamera as TextView
        ivClose.setOnClickListener {
            dialog.dismiss()
        }
        tvCamera.setOnClickListener {
            dialog.dismiss()
         checkPermissions(1)
        }
        tvGallery.setOnClickListener {
            dialog.dismiss()
            checkPermissions(2)
        }

        dialog.show()

    }

    /***
     *  Check Permissions
     */
    fun checkPermissions(type: Int){

        Dexter.withContext(activity)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            if (type == 1) {
                                launchCameraIntent()
                            } else if (type == 2) {
                                launchGalleryIntent()
                            }
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

    /**
     *
     *  Launch Camera
     */
    private fun launchCameraIntent() {
        val intent = Intent(activity, ImagePickerActivity::class.java)
        intent.putExtra(
                ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
                ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        super.startActivityForResult(intent, REQUEST_IMAGE)
    }

    /**
     * Lauch Gallerh
     */

    private fun launchGalleryIntent() {
        val intent = Intent(activity, ImagePickerActivity::class.java)
        intent.putExtra(
                ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
                ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(intent,REQUEST_IMAGE)
    }
    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        super.startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // The last parameter value of shouldHandleResult() is the value you pass to setRequestCode().
        // If you did not call setRequestCode(), you could ignore the last parameter.
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri: Uri = data!!.getParcelableExtra<Uri>("path")!!
                try {
                    imagePath= uri.path.toString()
                    ViewUtils.loadImage(imagePath, ivProfile,requireContext())
                    updateUserDetail(imagePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)   // This line is REQUIRED in fragment mode
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
    /***
     * GET USER DETAIL FOR
     */

    fun getUserDetail(){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(requireContext())
            userDetailViewModel.getUserDetail(Constants.Token+ AlchemiApplication.alchemiApplication?.getToken(),view)!!.observe(viewLifecycleOwner, Observer { userDetailModel ->
                ViewUtils.dismissProgress()
                if (userDetailModel.code==Constants.CODE_200){
                    if (userDetailModel.data.firstname!=null && userDetailModel.data.firstname!="null") {
                        tvName.text = userDetailModel.data.firstname+ " "+userDetailModel.data.lastname
                        tvUserName.text = userDetailModel.data.firstname+ " "+userDetailModel.data.lastname
                    }


                    AlchemiApplication.alchemiApplication?.saveUserDetails(userDetailModel.data.user_id)
                    tvEmail.text=userDetailModel.data.email
                    if (userDetailModel.data.phone!=null && userDetailModel.data.phone!="null") {
                        tvPhoneNumber.text = userDetailModel.data.phone
                    }
                    ViewUtils.loadImage(userDetailModel.data.selfie,ivProfile,requireContext())
                }else{
                    val msg =userDetailModel.message
                    ViewUtils.showSnackBar(scrollView,""+msg)
                }

            })
        }
    }

    /***
     *  Update user Detail
     */
    fun updateUserDetail(filePath:String) {
        if (imagePath.isEmpty()) {
            ViewUtils.showSnackBar(scrollView, getString(R.string.take_selfie_to_upload))
        } else if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(scrollView, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        } else {
            ViewUtils.showProgress(requireContext())
            uploadDocumentsViewModel.uploadDocuments(true,""+ AlchemiApplication.alchemiApplication?.getUUID()?.trim(),"","",Constants.countryName,Constants.countryCode,filePath,false,Constants.Token+AlchemiApplication.alchemiApplication?.getToken(), view)!!.observe(viewLifecycleOwner, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code == Constants.CODE_200) {
                    ViewUtils.showSnackBar(scrollView, "" + emailConfirmationRequestModel.message)
                } else {
                    val msg = emailConfirmationRequestModel.message
                    ViewUtils.showSnackBar(scrollView, "" + msg)
                }

            })
        }
    }
}