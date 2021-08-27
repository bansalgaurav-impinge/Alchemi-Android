
import android.annotation.SuppressLint
import com.app.alchemi.utils.CryptoHelper
import android.content.ContentValues.TAG
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.app.alchemi.utils.AlchemiApplication
import org.json.JSONObject
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


object Constants {

    val Token: String="token "
    val CODE_200=200
    var defaultSelectedIndex=0
    const val KEY_AUTHORIZATION="Authorization"
    const val DEVICE_NAME="android"
//eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6Ijk2N2VhYzY4LWI0NTgtNDRlNC05NDBjLWQxMDlhY2Y2ODdmOCIsImVtYWlsIjoiaGFyZGVlcGthdXJ1cHBhbEBnbWFpbC5jb20iLCJqd3Rfc2VjcmV0IjoiMmZmYWM2OWQtNjAzYy00YjkzLTkyNzMtMjY0NGY5MmJhMjJkIn0.jMguUd5dHWWv_ha-25d_ft-c--8wsVeknsR4hPZ83H8

    const val KEY_PRICE="price"
    const val KEY_PRICE_VARIATIONS="price_variation"
    const val KEY_RANK="rank"
    //Key values
    const val AES_SECRET_KEY = "8]UXrY-rF<^~-QR4"
    const val ALGORITHM = "HmacSHA512"
    const val EMPTY="EMPTY"
    const val KEY_TITLE="title"
    const val KEY_BODY="body"
    const val KEY_TYPE="type"
    const val KEY_DESCRIPTION="description"
    const val KEY_REDIRECT_URL="redirect_url"
    const val KEY_EMAIL="email"
    const val KEY_USER="user"
    const val KEY_PHONE="phone"
    const val KEY_OTP="otp"
    const val KEY_IMAGE="image"
    const val KEY_IS_VERIFIED="isVerified"
    const val KEY_SELFIE="selfie"
    const val KEY_FIRST_NAME="firstname"
    const val KEY_LAST_NAME="lastname"
    const val KEY_COUNTRY_NAME="country_name"
    const val KEY_COUNTRY_CODE="country_code"
    const val KEY_SSN_NUMBER="ssn_number"
    const val KEY_PIN="pin"
    const val KEY_TOKEN="token"
    const val KEY_PASSCODE="passcode"
    const val KEY_FIRSTNAME="firstName"
    const val KEY_LASTNAME="lastName"
    const val KEY_TIMESTAMP="timestamp"
    //const val KEY_NOTIFICATION_STATUS="notification_status"
    //const val KEY_PIN_STATUS="pin_status"
    const val KEY_STATUS="status"
    const val KEY_ANTI_PHISHING_CODE="anti_phishing_code"
    const val KEY_CRYPTO="crypto"
    const val KEY_LIMIT="limit"
    const val KEY_CODE="code"
    const val KEY_NAME="name"
    const val KEY_REFERRAL_CODE="referral_code"
    const val KEY_PUBLIC_TOKEN="public_token"
    const val KEY_CARD_ID="card_id"
    const val KEY_IS_USER_ACTIVATE="is_user_active"
    const val KEY_STAKING_STATUS ="staking_status"
    const val KEY_MESSAGE="message"
    const val KEY_DEVICE_TOKEN="device_token"
    const val KEY_DEVICE_TYPE="device_type"
    const val KEY_DEVICE_ID="device_id"



    var KEY_Index="0"
    var EMAIL="email"
    var isSignIn=false
    var isEditProfile=false
    var phoneNumber="phoneNumber"
    var selectedTab=0
    var KEY_HISTORY_TYPE="hour"
    var KEY_SELECTED_SPINNER_VALUE="Flexible"
    var KEY_REFRESH_DATA ="refresh_data"
    var KEY_REFRESH_HOME_SCREEN_DATA ="refresh_home_screen_data"



    //URLS
    const val WS_BASE_URL="http://163.47.212.61:8084/api/v1/"
    const val IMAGE_URL="https://www.cryptocompare.com/"
    const val LOGIN = "login"
    const val WS_EMAIL_CONFIRMATION_REQUEST ="request-confirm-email/"
    const val WS_REQUEST_CONFIRM_PHONE="request-confirm-phone/"
    const val WS_VERIFY_OTP="confirm-phone-otp"
    const val WS_UPLOAD_DOCUMENTS="uploadDocuments"
    const val WS_LOGIN="login-request-confirm-email"
    const val WS_ADD_USER_SSN="addUserSsnNumber"
    const val WS_ADD_USER_PIN="addUserPin"
    const val  WS_GET_COUNTRY_LIST="getCountryList"
    const val WS_GET_USER_DETAIL_LIST="userDetailsList"
    const val WS_UPDATE_PROFILE="updateProfile"
    const val WS_REQUEST_CONFIRM_UPDATE_PHONE="request-confirm-update-phone"
    const val WS_CONFIRM_UPDATE_OTP="confirm-update-phone-otp"
    const val WS_UPDATE_CONFIRM_EMAIL="request-update-confirm-email"
    const val WS_UPDATE_USER_PIN="updateUserPin"
    const val WS_LOGOUT="logout"
    const val WS_HOME="home"
    const val WS_GET_VERIFF_SESSION_URL="getVeriffSessionUrl"
    const val WS_GET_TOP_NEWS="getTopNews"
    const val WS_GET_TOP_MARKET_CAP_CRYPTO_CURRENCY="getTopMarketCapCryptocurrency"
    const val WS_CHECK_USER_PIN="checkUserPin"
    const val WS_UPDATE_NOTIFICATION_STATUS="updateNotificationStatus"
    const val WS_UPDATE_PIN_STATUS="updateUserPinStatus"
    const val WS_ADD_UPDATE_ANTI_PHISHING_CODE="addOrUpdateAntiPhishingCode"
    const val WS_SYSTEM_STATUS="getThirdPartyApiSystemResponse"
    const val WS_HISTORY_CRYPTO_DATA="getHistoricalCryptocurrencyData"
    const val WS_FORGOT_PASSCODE="forgotPasscode"
    const val WS_VERIFY_REFERRAL_CODE="verifyReferralCode"
    const val WS_CREATE_REFERRAL_CODE="createReferralCode"
    const val WS_STAKING_OPTIONS="getStakingOptions"
    const val WS_GET_TRACK_LIST="getTrackCoinList"
    const val WS_ADD_USER_FAVOURITE_COIN="addUserFavouriteCoin"
    const val WS_REMOVE_USER_FAVORITE="removeUserFavouriteCoin"
    const val WS_GET_CREATE_TOKEN="getOrCreateLinkToken"
    const val WS_GET_EXCHANGE_ACCESS_TOKEN="getOrExchangeAccessToken"
    const val WS_GET_CARD_LIST="getCardList"
    const val WS_ACTIVATE_DEACTIVATE_CARD="activateOrDeactivateCrad"
    const val WS_ACTIVATE_DEACTIVATE_STAKE_STATUS="activateOrDeactivateStakeStatus"
    const val WS_UPDATE_COIN_NOTIFICATIONS= "updateUserCoinNotificationStatus"
    const val WS_SAVE_CONTACT_SUPPORT_MESSAGE="saveContactSupportMessage"
    const val WS_GET_CONTACT_SUPPORT_MESSAGE_LIST="getContactSupportMessageList"


    /*
        method to convert string into HMAC
     */
    private fun getHMAC(data: String, key: String, algorithm: String): String {
        var mac: Mac? = null
        var value = ""
        try {
            val secretKeySpec = SecretKeySpec(key.toByteArray(), algorithm)
            mac = Mac.getInstance(algorithm)
            mac!!.init(secretKeySpec)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }
        if (mac != null) {
            value = toHexString(mac.doFinal(data.toByteArray()))
        }
        return value
    }

    /*
        Part of getHMAC method
     */
    private fun toHexString(bytes: ByteArray): String {
        val formatter = Formatter()
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }

    fun getFinalHmac(value: HashMap<String, String>,context1: Context): String {
        var user_id = ""
        if (AlchemiApplication.alchemiApplication!!.getUUID() != "") {
            user_id = AlchemiApplication.alchemiApplication!!.getUUID()
        }
        var hmac = ""
        val key = getDeviceID(context1)+ user_id
        try {
            val encryptedValue = CryptoHelper.encrypt(JSONObject(value as Map<*, *>).toString())
            hmac = getHMAC(encryptedValue, key, Constants.ALGORITHM)
        } catch (e: Exception) {
        }
        Log.e(TAG, "\nKey:\n$key")
        Log.e(TAG, "\nHMAC:\n$hmac")
        return hmac
    }

    fun getEncryptedValue(value: HashMap<*, *>): String {
        var output = ""
        try {
            Log.e("jsonvalue>>", "???" + JSONObject(value))
            output = CryptoHelper.encrypt(JSONObject(value).toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e(TAG, "\nEnryptedvalue:\n$output")
        return output
    }


    @SuppressLint("HardwareIds")
     fun getDeviceID(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    //Keys
    const val UUID = "uuid"
    const val USER_TYPE="user_type"
    const val HMAC = "HMAC"
    const val DATA = "data"
    const val Intro=false
    const val Index="index"

    var firstName="fname"
    var lasttName="lname"
    var countryCode="countrycode"
    var countryName="countryName"
    const val APP_URL="http://com.app.alchemi"


}
