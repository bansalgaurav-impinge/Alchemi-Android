package com.app.alchemi.views.fragments.dashboardFeatures

import Constants
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.alchemi.R
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.activities.MainActivity
import kotlinx.android.synthetic.main.amount_fragment_layout.*


class AmountFragment: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.amount_fragment_layout)
        ivBack.setOnClickListener {
            onBackPressed()
        }
       tvSend.setOnClickListener {
           validateView()
       }
    }


    override fun onResume() {
        super.onResume()
        tvTitle.text = getString(R.string.amount)

        if (intent!=null){
          etWalletAddress.setText(intent.getStringExtra(Constants.KEY_TITLE).toString())
        }
    }

    /***
     * Vali9date View
     */
    fun validateView(){
        ViewUtils.hideKeyBoard(this)
        val walletAddress = etWalletAddress.text.toString().trim()
        val amount = etAmount.text.toString().trim()
        if (walletAddress.isEmpty()) {
            ViewUtils.showSnackBar(scrollview, getString(R.string.enter_wallet_address_validation))
        } else if (amount.isEmpty()) {
            ViewUtils.showSnackBar(scrollview, getString(R.string.enter_amount_value))
        }
        else if (!ViewUtils.verifyAvailableNetwork(this)) {
            ViewUtils.showSnackBar(scrollview, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            startActivity(Intent(this, HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            finish()

        }
    }


}