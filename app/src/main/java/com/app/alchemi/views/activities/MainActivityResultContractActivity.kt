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
import com.plaid.link.OpenPlaidLink
import com.plaid.link.Plaid
import com.plaid.link.PlaidActivityResultContract
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkResult
import com.plaid.link.result.LinkSuccess

class MainActivityResultContractActivity : AppCompatActivity() {

  private lateinit var result: TextView
  private lateinit var tokenResult: TextView

  @OptIn(PlaidActivityResultContract::class)
  // Experimental API using ActivityResultContract for androidx.fragment:1.3.0+
  private val openPlaidLink = this.registerForActivityResult(
    OpenPlaidLink()
  ) { linkResult: LinkResult ->
    when (linkResult) {
      is LinkSuccess -> {
        Log.e("tes>>!ss" , linkResult.publicToken)

      }
      is LinkExit -> {
        tokenResult.text = ""
        if (linkResult.error != null) {
          Log.e("tes>>!er" ,
            linkResult.error?.displayMessage+"<<<<<"+
            linkResult.error?.errorCode
          )
        } else {

           Log.e("tes>>!" ,linkResult.metadata.status?.jsonValue ?: "unknown"
          )
        }
      }
      else -> {
        throw RuntimeException("Got unexpected result:$linkResult")
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setActionBar(findViewById(R.id.toolbar))

      setOptionalEventListener()
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
    RetrofitClient.token.subscribe(::onLinkTokenSuccess, ::onLinkTokenError)
  }

  private fun onLinkTokenSuccess(linkToken: String) {
    val tokenConfiguration = LinkTokenConfiguration.Builder()
      .token(linkToken)
      .build()

    // Experimental API using ActivityResultContract for androidx.fragment:1.3.0+
    openPlaidLink.launch(tokenConfiguration)
  }

  private fun onLinkTokenError(error: Throwable) {
    if (error is java.net.ConnectException) {
      Toast.makeText(this, "Please run `sh start_server.sh <client_id> <sandbox_secret>`", Toast.LENGTH_LONG).show()
      return
    }
    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
  }


}
