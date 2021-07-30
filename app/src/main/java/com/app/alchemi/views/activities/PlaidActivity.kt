/*
 * Copyright (c) 2020 Plaid Technologies, Inc. <support@plaid.com>
 */

package com.app.alchemi.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.alchemi.R
import com.app.alchemi.retrofit.RetrofitClient
import com.plaid.link.Plaid
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkResultHandler

class PlaidActivity : AppCompatActivity() {

  private lateinit var result: TextView
  private lateinit var tokenResult: TextView

  private val myPlaidResultHandler by lazy {
    LinkResultHandler(
            onSuccess = {
              Log.e("test>>>","success>>"+it.publicToken)

            },
            onExit = {
//              tokenResult.text = ""
              if (it.error != null) {
                Log.e("test>>>","error>>"+it.error?.displayMessage+">>>>"+it.error?.errorCode)

              } else {
                Log.e("test>>>","unknownerror>>"+it.metadata.status?.jsonValue)

              }
            }
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.accounts_fragment_layout)


//    val plaidClient: PlaidClient = PlaidClient.newBuilder()
//            .clientIdAndSecret(PLAID_CLIENT_ID, PLAID_SECRET)
//            .sandboxBaseUrl()
//            .build()

      setOptionalEventListener()
//      val linkTokenConfiguration = linkTokenConfiguration {
//        token = "link-sandbox-33792986-2b9c-4b80-b1f2-518caaac6183"
//        val plaidHandler: PlaidHandler = Plaid.create(application, linkTokenConfiguration)
//        plaidHandler.open(this)
//
//
//      }
//      val tokenConfiguration = LinkTokenConfiguration.Builder()
//              .token("link-sandbox-33792986-2b9c-4b80-b1f2-518caaac6183")
//              .build()
//      Plaid.create(
//              this.application,
//              tokenConfiguration
//      ).open(this)

      openLink()

  }

  /**
   * Optional, set an [event listener](https://plaid.com/docs/link/android/#handling-onevent).
   */
  private fun setOptionalEventListener() = Plaid.setLinkEventListener { event ->
    Log.i("Event", event.toString())
  }

  /**
   * For all Link configuration options, have a look at the
   * [parameter reference](https://plaid.com/docs/link/android/#parameter-reference).
   */
  private fun openLink() {
    val tokenConfiguration = LinkTokenConfiguration.Builder()
            .token("link-sandbox-58866e60-98bb-4bea-9e7d-e8fea99a885e")
            .build()
    Plaid.create(
            this.application,
            tokenConfiguration
    ).open(this)
  //  RetrofitClient.token.subscribe(::onLinkTokenSuccess, ::onLinkTokenError)
  }

  private fun onLinkTokenSuccess(linkToken: String) {

    //TODO 19/05/2021
    Log.e("test>>", "success" + linkToken)
    val tokenConfiguration = LinkTokenConfiguration.Builder()
      .token(linkToken)
      .build()
    Plaid.create(
            this.application,
            tokenConfiguration
    ).open(this)
  }

  private fun onLinkTokenError(error: Throwable) {
    if (error is java.net.ConnectException) {
      Toast.makeText(this, "Please run `sh start_server.sh <client_id> <sandbox_secret>`", Toast.LENGTH_LONG).show()
      return
    }
      Log.e("test>>", "err" + error + "Res" + error.message)
    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, intent)
    if (!myPlaidResultHandler.onActivityResult(requestCode, resultCode, data)) {
        Log.e("test>>", "test" + requestCode + "Res" + resultCode + "data" + data)
      Log.i(PlaidActivity::class.java.simpleName, "Not handled")
    }
  }



}
