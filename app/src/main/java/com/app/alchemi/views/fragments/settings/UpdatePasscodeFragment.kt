package com.app.alchemi.views.fragments.settings

import Constants
import android.R.attr.button
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.AddUserPinViewModel
import com.app.alchemi.views.activities.HomeActivity
import kotlinx.android.synthetic.main.change_pin_layout.*
import kotlinx.android.synthetic.main.change_pin_layout.etPin
import kotlinx.android.synthetic.main.change_pin_layout.ivShow
import kotlinx.android.synthetic.main.change_pin_layout.llPassword
import kotlinx.android.synthetic.main.change_pin_layout.scrollView
import kotlinx.android.synthetic.main.change_pin_layout.tvShow
import kotlinx.android.synthetic.main.toolbar_layout.*


class UpdatePasscodeFragment : Fragment() {
    internal lateinit var addUserPinViewModel: AddUserPinViewModel
    companion object {
        fun newInstance() = UpdatePasscodeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.change_pin_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewUtils.showAnimation(context, llTop, R.anim.slide_in_left)
        ViewUtils.showAnimation(context, tvUpdatePasscode, R.anim.slide_in_right)
        addUserPinViewModel = ViewModelProvider(this).get(AddUserPinViewModel::class.java)

        etOldPin.transformationMethod = HideReturnsTransformationMethod.getInstance()
        ivOldShow.setImageResource(R.drawable.ic_hide)
        tvOldShow.text = getString(R.string.hide)
        /***
         *  Click to Check Validations and hit API
         */
        tvUpdatePasscode.setOnClickListener {
            checkValidationsAndUpdatePasscode()
        }
        llOldPassword.setOnClickListener {
            ShowHidePass(llOldPassword)
        }
        llPassword.setOnClickListener {
            ShowHidePass(llPassword)
        }
        llConfirmPassword.setOnClickListener {
            ShowHidePass(llConfirmPassword)
        }

    }






    /***
     *  method to check validation and call api
     */
    fun checkValidationsAndUpdatePasscode(){
        ViewUtils.hideKeyBoard(requireActivity())
        val oldPin = etOldPin.text.toString().trim()
        val pin = etPin.text.toString().trim()
        val confirmPin = etConfirmPin.text.toString().trim()
        if (oldPin.isEmpty()) {
            ViewUtils.showSnackBar(view, getString(R.string.enter_your_old_passcode))
        }
        else if (pin.isEmpty()) {
            ViewUtils.showSnackBar(view, getString(R.string.enter_your_new_passcode))
        } else if (confirmPin.isEmpty()) {
            ViewUtils.showSnackBar(view, getString(R.string.enter_your_confirm_passcode))
        }
        else if (pin!=confirmPin) {
            ViewUtils.showSnackBar(view, getString(R.string.new_passcode_and_confirm_passcode_is_mismatched))
        }
        else if (pin.length<6){
            ViewUtils.showSnackBar(view,getString(R.string.pin_should_be_six_digits))
        }
         else if (AlchemiApplication.alchemiApplication?.getPassCode()!=null &&AlchemiApplication.alchemiApplication?.getPassCode()!="null" && (""+AlchemiApplication.alchemiApplication?.getPassCode()).isNotEmpty()){
            if (oldPin.toInt()!=AlchemiApplication.alchemiApplication?.getPassCode()?.toInt()){
                ViewUtils.showSnackBar(view, getString(R.string.incorrect_old_passcode)
                )
            }else{
                hitApi(pin.trim())
            }
        }else{
            hitApi(pin.trim())
        }

    }

    /***
     *  <ethod to hit web service
     */
    fun hitApi(pin: String){
         if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(
                    scrollView,
                    getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry)
            )
        }
        else {
            ViewUtils.showProgress(requireContext())
            val hashMap= HashMap<String, String>()
            hashMap[Constants.KEY_PIN]=pin.trim()
            hashMap[Constants.KEY_USER]=""+ AlchemiApplication.alchemiApplication?.getUUID()

            addUserPinViewModel.addUserPin(
                    hashMap,
                    Constants.Token + AlchemiApplication.alchemiApplication?.getToken(),
                    view
            )!!.observe(viewLifecycleOwner, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code == Constants.CODE_200) {
                    ViewUtils.showSnackBar(scrollView, "" + getString(R.string.passcode_updated_successfully))
                    Handler(Looper.getMainLooper()).postDelayed({
                        (activity as HomeActivity).clearStack(1)
                    }, 300)


                    /// showEmailConfirmationScreen()
                } else {
                    val msg = emailConfirmationRequestModel.message
                    ViewUtils.showSnackBar(scrollView, "" + msg)
                }

            })
        }
    }

    /**
     *  Method to manage Passcode view
     */
    fun ShowHidePass(view: View){

        if(view.id ==R.id.llPassword){

            if(etPin.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
               ivShow.setImageResource(R.drawable.ic_hide)
                tvShow.text = getString(R.string.hide)

                //Show Password
                etPin.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else{
                ivShow.setImageResource(R.drawable.ic_show)
                tvShow.text = getString(R.string.show)

                //Hide Password
                etPin.transformationMethod = PasswordTransformationMethod.getInstance()

            }
        }
        else if(view.id ==R.id.llConfirmPassword){
            if(etConfirmPin.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
                ivConfirmShow.setImageResource(R.drawable.ic_hide)
                tvConfirmShow.text = getString(R.string.hide)

                //Show Password
                etConfirmPin.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else{
                ivConfirmShow.setImageResource(R.drawable.ic_show)
                tvConfirmShow.text = getString(R.string.show)

                //Hide Password
                etConfirmPin.transformationMethod = PasswordTransformationMethod.getInstance()

            }
        }

        else if(view.id ==R.id.llOldPassword){
            if(etOldPin.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
                ivOldShow.setImageResource(R.drawable.ic_hide)
                tvOldShow.text = getString(R.string.hide)

                //Show Password
                etOldPin.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else{
                ivOldShow.setImageResource(R.drawable.ic_show)
                tvOldShow.text = getString(R.string.show)

                //Hide Password
                etOldPin.transformationMethod = PasswordTransformationMethod.getInstance()

            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).tab_layout?.visibility= View.GONE
        requireActivity().toolbar_title.text  = getString(R.string.change_passcode)
        requireActivity().toolbar_title.gravity= Gravity.LEFT
        requireActivity().ivChat.visibility= View.GONE
        requireActivity().rlAcx.visibility= View.GONE
    }



    override fun onDestroyView() {
        (activity as HomeActivity).tab_layout?.visibility= View.VISIBLE
        requireActivity().toolbar_title.gravity= Gravity.NO_GRAVITY
        try {
            ViewUtils.hideKeyBoard(requireActivity())
        }catch (e:Exception){
            e.printStackTrace()
        }
        super.onDestroyView()
    }
}