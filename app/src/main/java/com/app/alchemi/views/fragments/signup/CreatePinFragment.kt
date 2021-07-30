package com.app.alchemi.views.fragments.signup

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.FragmentNavigation
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.AddUserPinViewModel
import com.app.alchemi.views.activities.MainActivity
import kotlinx.android.synthetic.main.change_pin_layout.*
import kotlinx.android.synthetic.main.create_pin_fragment_layout.*
import kotlinx.android.synthetic.main.create_pin_fragment_layout.etConfirmPin
import kotlinx.android.synthetic.main.create_pin_fragment_layout.etPin
import kotlinx.android.synthetic.main.create_pin_fragment_layout.ivConfirmShow
import kotlinx.android.synthetic.main.create_pin_fragment_layout.ivShow
import kotlinx.android.synthetic.main.create_pin_fragment_layout.llConfirmPassword
import kotlinx.android.synthetic.main.create_pin_fragment_layout.llPassword
import kotlinx.android.synthetic.main.create_pin_fragment_layout.scrollView
import kotlinx.android.synthetic.main.create_pin_fragment_layout.tvConfirmShow
import kotlinx.android.synthetic.main.create_pin_fragment_layout.tvShow
import kotlinx.android.synthetic.main.toolbar_layout.*

class CreatePinFragment : Fragment() {
    private lateinit var addUserPinViewModel: AddUserPinViewModel
    companion object {
        fun newInstance() = CreatePinFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.create_pin_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewUtils.showAnimation(context,llBottoms, R.anim.slide_in_left)
        addUserPinViewModel = ViewModelProvider(this).get(AddUserPinViewModel::class.java)

        etPin.transformationMethod = HideReturnsTransformationMethod.getInstance()
        ivShow.setImageResource(R.drawable.ic_hide)
        tvShow.text = getString(R.string.hide)
        /***
         *  Click to Check Validations and hit API
         */
        tvSetPin.setOnClickListener {
            checkvalidationsAndCreatPin()
        }
        llPassword.setOnClickListener {
            ShowHidePass(llPassword)
        }
        llConfirmPassword.setOnClickListener {
            ShowHidePass(llConfirmPassword)
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().toolbar_title.text  = getString(R.string.set_pin)
    }




    /***
     *  method to check validation and call api
     */
    fun checkvalidationsAndCreatPin(){
        ViewUtils.hideKeyBoard(requireActivity())
        val pin = etPin.text.toString().trim()
        val confirmPin = etConfirmPin.text.toString().trim()
        if (pin.isEmpty()) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.enter_your_create_pin))
        } else if (confirmPin.isEmpty()) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.enter_your_confirm_pin))
        }
        else if (pin!=confirmPin) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.create_pin_and_confirm_pin_is_mismatched))
        }
        else if (pin.length<6) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.pin_should_be_six_digits))
        }
        else if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(requireContext())
            val hashMap= HashMap<String,String>()
            hashMap[Constants.KEY_PIN]=pin.trim()
            hashMap[Constants.KEY_USER]=""+ AlchemiApplication.alchemiApplication?.getUUID()
            addUserPinViewModel.addUserPin(hashMap, "", view)!!.observe(viewLifecycleOwner, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code==Constants.CODE_200){
                AlchemiApplication.alchemiApplication?.clearAllData()
                startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                activity?.finish()
                   /// showEmailConfirmationScreen()
                }else{
                    val msg =emailConfirmationRequestModel.message
                    ViewUtils.showSnackBar(scrollView,""+msg)
                }

            })
        }
    }


    fun ShowHidePass(view:View){

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
    }

    override fun onDestroyView() {
        try {
            ViewUtils.hideKeyBoard(requireActivity())
        }catch (e:Exception){
            e.printStackTrace()
        }
        super.onDestroyView()
    }
}