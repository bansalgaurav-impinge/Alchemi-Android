package com.app.alchemi.views.fragments.signup

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.app.alchemi.viewModel.AddSSNViewModel
import kotlinx.android.synthetic.main.ssn_fragment_layout.*
import kotlinx.android.synthetic.main.ssn_fragment_layout.llBottoms
import kotlinx.android.synthetic.main.ssn_fragment_layout.llTop
import kotlinx.android.synthetic.main.toolbar_layout.*

class SSNFragment:  Fragment() {
    internal lateinit var addSSNViewModel: AddSSNViewModel
    companion object {
        fun newInstance() = SSNFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.ssn_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewUtils.showAnimation(context,llBottoms,R.anim.slide_in_left)

        ViewUtils.showAnimation(context,llTop,R.anim.slide_in_left)
        addSSNViewModel = ViewModelProvider(this).get(AddSSNViewModel::class.java)
        /***
         *  Click to Check Validations and hit API
         */
        tvContinue.setOnClickListener {
            checkSSN()
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().toolbar_title.text  = getString(R.string.ssn_address)
    }

    fun showCreatePinScreen(){
        FragmentNavigation
            .Builder(activity?.supportFragmentManager, R.id.fragmentContainer, CreatePinFragment.newInstance())
            .addToBackstack()
            .execute()
    }



    /***
     *  method to check validation and call api
     */
    fun checkSSN(){
        ViewUtils.hideKeyBoard(requireActivity())
        val ssn = etSsn.text.toString().trim()
        if (ssn.isEmpty()) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.enter_your_ssn_address_number_validation))
        } else if (ssn.length<11) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.enter_valid_ssn))
        }
        else if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(scrollView,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(requireContext())
            val hashMap= HashMap<String,String>()
            hashMap[Constants.KEY_SSN_NUMBER]=ssn.trim()
            hashMap[Constants.KEY_USER]=""+AlchemiApplication.alchemiApplication?.getUUID()
            addSSNViewModel.addUserSSN(hashMap,view)!!.observe(viewLifecycleOwner, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code==Constants.CODE_200){
                    ViewUtils.showSnackBar(scrollView,""+emailConfirmationRequestModel.message)
                    Handler(Looper.getMainLooper()).postDelayed({
                        showCreatePinScreen()

                    }, 200)

                }else{
                    val msg =emailConfirmationRequestModel.message
                    ViewUtils.showSnackBar(scrollView,""+msg)
                }

            })
        }
    }

}