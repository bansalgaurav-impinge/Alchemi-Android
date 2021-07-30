package com.app.alchemi.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.multidex.BuildConfig
import com.app.alchemi.views.activities.MainActivity


class AlchemiApplication : Application {
    override fun onCreate() {
        super.onCreate()
        alchemiApplication = AlchemiApplication(applicationContext)
    }
    constructor()
    constructor(context: Context) {
       mSharedPreferences = context.getSharedPreferences(mySharedPreferences, Context.MODE_PRIVATE)
    }

    fun saveUserDetails(uuid:String) {
        mEditor = mSharedPreferences!!.edit()
        try {
            mEditor!!.putString(Constants.UUID, CryptoHelper.encrypt(isStringEmpty(uuid)))
            mEditor!!.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun saveUserToken(token:String) {
        mEditor = mSharedPreferences!!.edit()
        try {
            mEditor!!.putString(Constants.KEY_TOKEN, CryptoHelper.encrypt(isStringEmpty(token)))
            mEditor!!.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun savePassCodeSettings(onOff:String) {
        mEditor = mSharedPreferences!!.edit()
        try {
            mEditor!!.putString(Constants.KEY_STATUS, CryptoHelper.encrypt(isStringEmpty(onOff)))
            mEditor!!.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun savePasscode(passcode:String) {
        mEditor = mSharedPreferences!!.edit()
        try {
            mEditor!!.putString(Constants.KEY_PASSCODE, CryptoHelper.encrypt(isStringEmpty(passcode)))
            mEditor!!.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun saveAntiPhishingCode(passcode:String) {
        mEditor = mSharedPreferences!!.edit()
        try {
            mEditor!!.putString(Constants.KEY_ANTI_PHISHING_CODE, CryptoHelper.encrypt(isStringEmpty(passcode)))
            mEditor!!.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveUserEmail(email:String) {
        mEditor = mSharedPreferences!!.edit()
        try {
            mEditor!!.putString(Constants.KEY_EMAIL, CryptoHelper.encrypt(isStringEmpty(email)))
            mEditor!!.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun saveScreenIndex(index:String) {
        mEditor = mSharedPreferences!!.edit()
        try {
            mEditor!!.putString(Constants.Index, CryptoHelper.encrypt(isStringEmpty(index)))
            mEditor!!.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveCoutryNameAndCode(countryName:String, countryCode:String) {
        mEditor = mSharedPreferences!!.edit()
        try {
            mEditor!!.putString(Constants.KEY_COUNTRY_NAME, CryptoHelper.encrypt(isStringEmpty(countryName)))
            mEditor!!.putString(Constants.KEY_COUNTRY_CODE, CryptoHelper.encrypt(isStringEmpty(countryCode)))
            mEditor!!.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun getCountryName(): String {
        if (mSharedPreferences != null) {
            return try {
                CryptoHelper.decrypt(mSharedPreferences?.getString(Constants.KEY_COUNTRY_NAME, "")!!)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
        return ""
    }
    fun getCountryCode(): String {
        if (mSharedPreferences != null) {
            return try {
                CryptoHelper.decrypt(mSharedPreferences?.getString(Constants.KEY_COUNTRY_CODE, "")!!)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
        return ""
    }
    fun saveInrtro(Intro:Boolean) {
        mEditor = mSharedPreferences!!.edit()
        try {
            mEditor!!.putBoolean(Constants.Intro.toString(),true)
            mEditor!!.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAntiPhishingCode(): String {
        if (mSharedPreferences != null) {
            return try {
                CryptoHelper.decrypt(mSharedPreferences?.getString(Constants.KEY_ANTI_PHISHING_CODE, "")!!)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
        return ""
    }

    fun getUUID(): String {
        if (mSharedPreferences != null) {
            return try {
                CryptoHelper.decrypt(mSharedPreferences?.getString(Constants.UUID, "")!!)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
        return ""
    }

    fun getPassCode(): String {
        if (mSharedPreferences != null) {
            return try {
                CryptoHelper.decrypt(mSharedPreferences?.getString(Constants.KEY_PASSCODE, "")!!)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
        return ""
    }

    fun getToken(): String {
        if (mSharedPreferences != null) {
            return try {
                CryptoHelper.decrypt(mSharedPreferences?.getString(Constants.KEY_TOKEN, "")!!)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
        return ""
    }
    fun getEmail(): String {
        if (mSharedPreferences != null) {
            return try {
                CryptoHelper.decrypt(mSharedPreferences?.getString(Constants.KEY_EMAIL, "")!!)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
        return ""
    }
    fun getIndex(): String {
        if (mSharedPreferences != null) {
            return try {
                CryptoHelper.decrypt(mSharedPreferences?.getString(Constants.Index, "")!!)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
        return ""
    }


    fun getPassCodeSettings(): String {
        if (mSharedPreferences != null) {
            return try {
                CryptoHelper.decrypt(mSharedPreferences?.getString(Constants.KEY_STATUS, "")!!)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
        return ""
    }


    fun clearAllData() {
        mEditor = mSharedPreferences?.edit()
        mEditor!!.clear().apply()
    }

    fun removeData() {
        mEditor = mSharedPreferences?.edit()
       // mEditor.remove(ConstantsKey.UUID);
        mEditor!!.apply()
    }

    companion object {
        private const val mySharedPreferences = BuildConfig.APPLICATION_ID
        private var mSharedPreferences: SharedPreferences? = null
        private var mEditor: SharedPreferences.Editor? = null
        var alchemiApplication: AlchemiApplication? = null
    }

    private fun isStringEmpty(string:String):String{
        return if (string == ""){
            Constants.EMPTY
        }else{
            string
        }
    }

    /***
     *  method to decrypt encrypted value
     */
    fun getDecryptedValue(string: String):String{
        return if (mSharedPreferences != null){
            try {
                if (CryptoHelper.decrypt(string)==Constants.EMPTY){
                    ""
                }else{
                    CryptoHelper.decrypt(string)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }else{
            ""
        }
    }


}

