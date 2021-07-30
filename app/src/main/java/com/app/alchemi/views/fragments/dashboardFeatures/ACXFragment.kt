package com.app.alchemi.views.fragments.dashboardFeatures

import Constants
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.Target
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.utils.navigateTo
import com.app.alchemi.viewModel.CheckUserPinViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.ACXAdapter
import kotlinx.android.synthetic.main.acx_fragment_layout.*
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.toolbar_layout.*


class ACXFragment: Fragment() {
    private lateinit var acxAdapter: ACXAdapter
    private lateinit var checkUserPinViewModel: CheckUserPinViewModel
    companion object {
        fun newInstance() = ACXFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.acx_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUserPinViewModel = ViewModelProvider(this).get(CheckUserPinViewModel::class.java)
        requireActivity().ivBack.setOnClickListener {
            (activity as HomeActivity).selectPage(0)
        }

        val  hash=HashMap<String, String>()
        hash[Constants.KEY_FIRST_NAME]=getString(R.string.alchemi_wallet)
        hash[Constants.KEY_IMAGE]="1"
        val  hash1=HashMap<String, String>()
        hash1[Constants.KEY_FIRST_NAME]=getString(R.string.track)
        hash[Constants.KEY_IMAGE]="2"
        val  hash2=HashMap<String, String>()
        hash2[Constants.KEY_FIRST_NAME]=getString(R.string.nfts)

        val  hash3=HashMap<String, String>()
        hash3[Constants.KEY_FIRST_NAME]=getString(R.string.mortgages)

        val  hash4=HashMap<String, String>()
        hash4[Constants.KEY_FIRST_NAME]=getString(R.string.p2p_lending)
        val  hash5=HashMap<String, String>()
        hash5[Constants.KEY_FIRST_NAME]=getString(R.string.blockchain_bonds)
        val dataList=ArrayList<HashMap<String, String>>()

        dataList.add(hash)
        dataList.add(hash1)
        dataList.add(hash2)
        dataList.add(hash3)
        dataList.add(hash4)
        dataList.add(hash5)
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvList?.layoutManager = llm


        acxAdapter = ACXAdapter(dataList)
        rvList?.setHasFixedSize(true)
        rvList?.adapter = acxAdapter

        cvEarn.setOnClickListener {
            val bundle =Bundle()
            (activity as HomeActivity).replaceFragment(EarnFragment(), "" + EarnFragment, bundle)
        }
        rlWallet.setOnClickListener {
            (activity as HomeActivity).selectPage(1)
        }
        CvPay.setOnClickListener {
            navigateTo(requireContext(), Target.ACX_PAY)
        }
        rlReferralCode.setOnClickListener {
            getReferralCode()
        }
    }
    fun shareApp(context: Context,code:String) {
        val appPackageName: String = context.packageName
        val sendIntent = Intent()
        val referralLink= "$appPackageName/$code"
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.referral_code_link)+" https://$referralLink"+ " to sign up for https://alchemi.com and we both get $25 USD :)" )
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    }

    /***
     *  method to check validation and call api
     */
    fun getReferralCode(){
        ViewUtils.hideKeyBoard(requireActivity())
         if (!ViewUtils.verifyAvailableNetwork(requireActivity())) {
            ViewUtils.showSnackBar(view,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            val hashMap= HashMap<String,String>()
             hashMap[Constants.KEY_USER]=""+AlchemiApplication.alchemiApplication?.getUUID()
            checkUserPinViewModel.createReferralCode(hashMap, view,Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(viewLifecycleOwner, Observer { emailConfirmationRequestModel ->
                ViewUtils.dismissProgress()
                if (emailConfirmationRequestModel.code==Constants.CODE_200){
                    shareApp(requireContext(),emailConfirmationRequestModel.data.referral_code)

                }else{
                    ViewUtils.showSnackBar(view,""+emailConfirmationRequestModel.message)
                }

            })
        }
    }
}