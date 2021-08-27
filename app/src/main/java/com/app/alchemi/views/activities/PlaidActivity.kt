

package com.app.alchemi.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.CheckUserPinViewModel
import com.app.alchemi.viewModel.LinkTokenViewModel
import com.plaid.link.OpenPlaidLink
import com.plaid.link.Plaid
import com.plaid.link.PlaidActivityResultContract
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkResult
import com.plaid.link.result.LinkResultHandler
import com.plaid.link.result.LinkSuccess
import kotlinx.android.synthetic.main.activity_plaid.*

class PlaidActivity : AppCompatActivity() {
    private lateinit var linkTokenViewModel: LinkTokenViewModel
    private  lateinit var checkUserPinViewModel: CheckUserPinViewModel
    var publicToken=""
//  private lateinit var result: TextView
//  private lateinit var tokenResult: TextView

  private val myPlaidResultHandler by lazy {
    LinkResultHandler(
            onSuccess = {
              Log.e("test>>","success>>"+it)
                publicToken= it.publicToken
                getOrExchangeAccessToken()

            },
            onExit = {
//              tokenResult.text = ""
              if (it.error != null) {
                Log.e("test>>","error>>"+it.error?.displayMessage+">>>>"+it.error?.errorCode)

              } else {
                Log.e("test>>","unknownerror>>"+it.metadata.status?.jsonValue)

              }
            }
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_plaid)
      linkTokenViewModel = ViewModelProvider(this).get(LinkTokenViewModel::class.java)

      getCreateToken()
      setOptionalEventListener()

  }

  /**
   * Optional, set an [event listener](https://plaid.com/docs/link/android/#handling-onevent).
   */
  private fun setOptionalEventListener() = Plaid.setLinkEventListener { event ->
    Log.e("Event", event.toString())
  }


  private fun onLinkTokenSuccess(linkToken: String) {
      val tokenConfiguration = LinkTokenConfiguration.Builder()
      .token(linkToken)
      .build()
    Plaid.create(
            this.application,
            tokenConfiguration
    ).open(this)
  }

    override fun onResume() {
        super.onResume()

    }

//
    @OptIn(PlaidActivityResultContract::class)
    // Experimental API using ActivityResultContract for androidx.fragment:1.3.0+
    private val openPlaidLink = this.registerForActivityResult(
        OpenPlaidLink()
      ) { linkResult: LinkResult ->
        when (linkResult) {
            is LinkSuccess -> {
                Log.e("test>>!ss", linkResult.publicToken)

            }
            is LinkExit -> {
                if (linkResult.error != null) {
                    Log.e(
                        "test>>!er",
                        linkResult.error?.displayMessage + "<<<<<" +
                                linkResult.error?.errorCode
                    )
                } else {

                    Log.e(
                        "test>>!", linkResult.metadata.status?.jsonValue ?: "unknown"
                    )
                }
            }
            else -> {
                throw RuntimeException("Got unexpected result:$linkResult")
            }
        }
    }


  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, intent)
    if (!myPlaidResultHandler.onActivityResult(requestCode, resultCode, data)) {
        Log.e("test>>", "test" + requestCode + "Res" + resultCode + "data" + data)
      Log.e(PlaidActivity::class.java.simpleName, "Not handled")
    }
  }
    /***
     *  Method to get link Token
     */
    fun getCreateToken(){
        ViewUtils.hideKeyBoard(this)
        if (!ViewUtils.verifyAvailableNetwork(this)) {
            ViewUtils.showSnackBar(llLayout,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            ViewUtils.showProgress(this)
            linkTokenViewModel.getCreateLinkToken(llLayout,Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(this, Observer { liveData ->
                ViewUtils.dismissProgress()
                if (liveData.code==Constants.CODE_200){
                    onLinkTokenSuccess(liveData.data.link_token)
                }else{
                    ViewUtils.showSnackBar(llLayout,""+liveData.message)
                }

            })
        }
    }



    /***
     *  method to hit add/ remove favourite coin
     */
    fun getOrExchangeAccessToken() {
        if (publicToken.isEmpty()){
            ViewUtils.showSnackBar(llLayout, getString(R.string.invalid_access_token))
        }
        else if (!ViewUtils.verifyAvailableNetwork(this)) {
            ViewUtils.showSnackBar(llLayout, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        } else {
            val hashMap = HashMap<String, String>()
            hashMap[Constants.KEY_PUBLIC_TOKEN]=""+publicToken
            ViewUtils.showProgress(this)
            checkUserPinViewModel.getOrExchangeAccessToken(hashMap,  llLayout,Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(this) { liveDataModel ->
                ViewUtils.dismissProgress()
                if (liveDataModel.code == Constants.CODE_200) {
                    onBackPressed()
                }

            }
        }
    }

  //  success>>public-sandbox-92b01e42-c6dc-406b-b55f-11e08c09a29e
    /****
     *  Event response from Plaid Login
     */
  //  LinkEvent(eventName=TRANSITION_VIEW, metadata=LinkEventMetadata(errorCode=, errorMessage=, errorType=, exitStatus=, institutionId=ins_3, institutionName=Chase, institutionSearchQuery=, linkSessionId=b0bb7df8-b1d2-4087-b5b2-3bd9e3479366, mfaType=, requestId=, timestamp=2021-08-02T06:34:10.261Z, viewName=com.plaid.link.event.LinkEventViewName$LOADING@95c4e6a, metadataJson=null))
    //LinkEvent(eventName=TRANSITION_VIEW, metadata=LinkEventMetadata(errorCode=, errorMessage=, errorType=, exitStatus=, institutionId=ins_3, institutionName=Chase, institutionSearchQuery=, linkSessionId=b0bb7df8-b1d2-4087-b5b2-3bd9e3479366, mfaType=, requestId=zLz6BOH94UiDkzn, timestamp=2021-08-02T06:34:15.253Z, viewName=com.plaid.link.event.LinkEventViewName$CONNECTED@a169af5, metadataJson=null))
   // LinkEvent(eventName=HANDOFF, metadata=LinkEventMetadata(errorCode=, errorMessage=, errorType=, exitStatus=, institutionId=ins_3, institutionName=Chase, institutionSearchQuery=, linkSessionId=b0bb7df8-b1d2-4087-b5b2-3bd9e3479366, mfaType=, requestId=zLz6BOH94UiDkzn, timestamp=2021-08-02T06:36:33.447Z, viewName=null, metadataJson=null))
   // success>>public-sandbox-a1563379-87e9-4aa0-8c58-b5a3dde0b8b0
}
