package com.app.alchemi.views.fragments.settings

import android.os.Bundle
import android.view.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.AntiPhishingCodeViewModel
import com.app.alchemi.views.activities.HomeActivity
import kotlinx.android.synthetic.main.anti_phshing_code_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class AntiPhishingCodeFragment: Fragment() {
    private lateinit var antiPhishingCodeViewModel: AntiPhishingCodeViewModel
    companion object {
        fun newInstance() = AntiPhishingCodeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.anti_phshing_code_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        antiPhishingCodeViewModel = ViewModelProvider(this).get(AntiPhishingCodeViewModel::class.java)
        etAntiPhishingCode.requestFocus()
        if (AlchemiApplication.alchemiApplication?.getAntiPhishingCode().toString().isNotEmpty() && AlchemiApplication.alchemiApplication?.getAntiPhishingCode()!=null) {
            etAntiPhishingCode.setText(AlchemiApplication.alchemiApplication?.getAntiPhishingCode().toString())
            etAntiPhishingCode.setSelection(AlchemiApplication.alchemiApplication?.getAntiPhishingCode().toString().length)
        }
            try {
            if (activity!=null) {
                (activity as HomeActivity).ivBack.setOnClickListener {
                    (activity as HomeActivity).onBackPressed()
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        tvSave.setOnClickListener {
            checkValidationAndSaveAntiPhishing()
        }


    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).tab_layout?.visibility= View.GONE
        requireActivity().toolbar_title.text  = getString(R.string.anti_phishing_code)
        requireActivity().toolbar_title.gravity= Gravity.START
        requireActivity().ivBack.setImageResource(R.drawable.ic_back)
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
    fun checkValidationAndSaveAntiPhishing(){
        ViewUtils.hideKeyBoard(this!!.requireActivity())
        val antiPhishingCode = etAntiPhishingCode.text.toString().trim()

        if (antiPhishingCode.isEmpty()) {
            ViewUtils.showSnackBar(view, getString(R.string.enter_aniti_phishing_code))
        }
        else if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            saveUpdateAntiPhishingCode(antiPhishingCode)
        }
    }

    /***
     *  save Anti-phishing code
     */
    fun saveUpdateAntiPhishingCode(antiPhishingCode:String) {
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        } else {
            val hashMap = HashMap<String,String>()
            hashMap[Constants.KEY_ANTI_PHISHING_CODE] = antiPhishingCode
            ViewUtils.showProgress(requireContext())
            antiPhishingCodeViewModel.antiPhishingCode(hashMap, view, Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(viewLifecycleOwner, Observer { liveDataModel ->
                ViewUtils.dismissProgress()
                if (liveDataModel.code == Constants.CODE_200) {
                    ViewUtils.showSnackBar(view, "" + liveDataModel.message)
                } else {
                    ViewUtils.showSnackBar(view, "" + liveDataModel.message)
                }

            })
        }
    }


}