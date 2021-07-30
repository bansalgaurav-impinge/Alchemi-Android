package com.app.alchemi.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 *
 */
object AddressUtils {
    var ADDRESS_DEFAULT_BASE = "https://www.geetest.com/demo/gt/"
    fun getRegister(context: Context, typeEnum: String): String {
        return joinUrl(context, typeEnum, true)
    }

    fun getValidate(context: Context, typeEnum: String): String {
        return joinUrl(context, typeEnum, false)
    }

    private fun joinUrl(context: Context, typeEnum: String, isRegister: Boolean): String {
        val base = getPreferences(context)
            .getString("settings_address_base", ADDRESS_DEFAULT_BASE)
        return base + (if (isRegister) "register-" else "validate-") + typeEnum
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}