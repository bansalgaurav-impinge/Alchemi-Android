package com.app.alchemi.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.app.alchemi.views.activities.*
import com.app.alchemi.views.fragments.dashboardFeatures.AmountFragment
import com.app.alchemi.views.fragments.dashboardFeatures.QRCodeFragment

enum class Target {
    START,REFERRAL_CODE,SIGN_UP_EMAIL,ACX_PAY,NOTIFICATIONS,AMOUNT,PLAID,ADD_PAYMENT_METHOD,ADD_CARD_DETAIL
}

    /**
    * Navigates to the given [target] by starting the corresponding activity [Context]
    */
    fun navigateTo(context: Context?,
               target: Target,
               bundle: Bundle? = null,
               flags: Int? = null,
               activityOptions: Bundle? = null) {
        if (context == null) {
        Log.e("tag","Error in navigateTo: Context is null")
        return
        }
        val intent = getIntent(context, target)
        bundle?.let {
        intent.putExtras(it)
        }

        flags?.let {
        intent.addFlags(it)
        }
        context.startActivity(intent, activityOptions)


    }

    /**
 * Navigates to the given [target] by starting the corresponding activity for result [requestCode]
 */
    fun navigateForResult(activity: Activity?, target: Target, bundle: Bundle? = null, requestCode: Int) {
        if (activity == null) {
        Log.e("tag","Error in navigateForResult: Activity is null")
        return
    }
    val intent = getIntent(activity, target)
    bundle?.let {
        intent.putExtras(it)
    }
    activity.startActivityForResult(intent, requestCode)
    }

    private fun getIntent(context: Context?, target: Target): Intent {
        return when (target) {
            Target.START -> Intent(context, MainActivity::class.java)
            Target.REFERRAL_CODE-> Intent(context,ReferralCodeActivity::class.java)
            Target.SIGN_UP_EMAIL-> Intent(context,SignUpActivity::class.java)
            Target.ACX_PAY->Intent(context,QRCodeFragment::class.java)
            Target.NOTIFICATIONS->Intent(context,NotificationsActivity::class.java)
            Target.AMOUNT->Intent(context,AmountFragment::class.java)
            Target.PLAID->Intent(context,PlaidActivity::class.java)
            Target.ADD_PAYMENT_METHOD->Intent(context,AddPaymentMethodActivity::class.java)
            Target.ADD_CARD_DETAIL->Intent(context,AddCardDetailActivity::class.java)
    }
}