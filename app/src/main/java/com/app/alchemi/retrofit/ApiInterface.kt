package com.app.alchemi.retrofit

import com.app.alchemi.models.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST(Constants.WS_GET_CREATE_TOKEN)
    fun getLinkToken(@Header(Constants.KEY_AUTHORIZATION) token: String): Call<LinkToken>
    @GET(Constants.LOGIN)
    fun getCategories(): Call<ServicesSetterGetter>

    @GET(Constants.LOGIN)
    suspend fun signUP(): Call<SignUpModel>

    @FormUrlEncoded
    @POST(Constants.LOGIN)
   suspend fun login(@Header(Constants.HMAC) hmac: String, @Field(Constants.DATA) data: String): Call<Any>

    @POST(Constants.WS_EMAIL_CONFIRMATION_REQUEST)
     fun emailConfirmationRequest(@Body hashMap: HashMap<String, String>): Call<EmailConfirmationRequestModel>

    @POST(Constants.WS_LOGIN)
    fun emailConfirmationRequestLogin(@Body hashMap: HashMap<String, String>): Call<EmailConfirmationRequestModel>
    @POST(Constants.WS_REQUEST_CONFIRM_PHONE)
     fun requestConfirmPhone(@Body hashMap: HashMap<String, String>): Call<EmailConfirmationRequestModel>

    @POST(Constants.WS_VERIFY_OTP)
    fun VerifyOTP(@Body hashMap: HashMap<String, String>): Call<EmailConfirmationRequestModel>

    @PUT(Constants.WS_ADD_USER_SSN)
    fun addUserSSN(@Body hashMap: HashMap<String, String>): Call<EmailConfirmationRequestModel>

    @PUT(Constants.WS_ADD_USER_PIN)
    fun addUserPin(@Body hashMap: HashMap<String, String>): Call<AddPinModel>

    @GET(Constants.WS_GET_COUNTRY_LIST)
    fun getCountryList(): Call<CountryListModel>
    @Multipart
    @POST(Constants.WS_UPLOAD_DOCUMENTS)
    fun updateDocuments(@Part(Constants.KEY_USER) user: RequestBody, @Part(Constants.KEY_FIRST_NAME) firstName: RequestBody,@Part(Constants.KEY_LAST_NAME) lastName: RequestBody,
                      @Part(Constants.KEY_COUNTRY_NAME) countryName: RequestBody,@Part(Constants.KEY_COUNTRY_CODE) countryCode: RequestBody, @Part(Constants.KEY_IS_VERIFIED) isVerified: Boolean): Call<EmailConfirmationRequestModel>

    @GET(Constants.WS_GET_USER_DETAIL_LIST)
    fun getUserDetail(@Header(Constants.KEY_AUTHORIZATION) token: String): Call<UserDetailModel>

    @POST(Constants.WS_REQUEST_CONFIRM_UPDATE_PHONE)
    fun requestConfirmUpdatePhone(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>

   @POST(Constants.WS_CONFIRM_UPDATE_OTP)
   fun VerifyUpdateOTP(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>

    @POST(Constants.WS_UPDATE_CONFIRM_EMAIL)
    fun emailUpdateConfirmationRequest(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>

    @Multipart
    @PUT(Constants.WS_UPDATE_PROFILE)
    fun updateProfile(@Part(Constants.KEY_USER) user: RequestBody, @Part(Constants.KEY_FIRST_NAME) firstName: RequestBody,@Part(Constants.KEY_LAST_NAME) lastName: RequestBody,
                        @Part(Constants.KEY_COUNTRY_NAME) countryName: RequestBody,@Part(Constants.KEY_COUNTRY_CODE) countryCode: RequestBody ,@Part(Constants.KEY_IS_VERIFIED) isVerified: Boolean,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>

    @Multipart
    @PUT(Constants.WS_UPDATE_PROFILE)
    fun updateProfileImage(@Part(Constants.KEY_USER) user: RequestBody, @Part selfie: MultipartBody.Part?,@Part(Constants.KEY_IS_VERIFIED) isVerified: Boolean,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>

   @POST(Constants.WS_LOGOUT)
   fun logout(@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>

    @PUT(Constants.WS_UPDATE_USER_PIN)
    fun updateUserPin(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<AddPinModel>

    @GET(Constants.WS_HOME)
    fun homeData(@Header(Constants.KEY_AUTHORIZATION) token: String): Call<HomeModel>

    @GET(Constants.WS_GET_TOP_NEWS)
    fun getTopNews(@Header(Constants.KEY_AUTHORIZATION) token: String): Call<AllNewsModel>

    @GET(Constants.WS_GET_TOP_MARKET_CAP_CRYPTO_CURRENCY)
    fun getTopCryptoCurrency(@Header(Constants.KEY_AUTHORIZATION) token: String): Call<TopCryptoCurrencyModel>

    @POST(Constants.WS_CHECK_USER_PIN)
    fun checkUserPin(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>

    @POST(Constants.WS_GET_VERIFF_SESSION_URL)
    fun getVeriffSessionUrl(@Body hashMap: HashMap<String, String>): Call<VeriffSDKSessionUrlModel>

    @PUT(Constants.WS_UPDATE_NOTIFICATION_STATUS)
    fun updateNotificationStatus(@Body hashMap: HashMap<String, Boolean>, @Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>

    @PUT(Constants.WS_UPDATE_PIN_STATUS)
    fun updatePasscodeStatus(@Body hashMap: HashMap<String, Boolean>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>

    @PUT(Constants.WS_ADD_UPDATE_ANTI_PHISHING_CODE)
    fun saveUpdateAntiPhishingCode(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>

    @GET(Constants.WS_SYSTEM_STATUS)
    fun getSystemStatus(@Header(Constants.KEY_AUTHORIZATION) token: String): Call<SystemStatusModel>

    @POST(Constants.WS_HISTORY_CRYPTO_DATA)
    fun getCoinHistory(@Body hashMap: HashMap<String, String>, @Header(Constants.KEY_AUTHORIZATION) token: String): Call<CoinHistoryModel>

    @Multipart
   @POST(Constants.WS_HISTORY_CRYPTO_DATA)
   fun getCoinHistory2(@Part(Constants.KEY_CRYPTO) crypto: String,@Part(Constants.KEY_LIMIT) limit: String,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<CoinHistoryModel>
   @POST(Constants.WS_FORGOT_PASSCODE)
   fun forgotPasscode(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>
   @POST(Constants.WS_VERIFY_REFERRAL_CODE)
   fun verifyReferralCode(@Body hashMap: HashMap<String, String>): Call<EmailConfirmationRequestModel>
   @POST(Constants.WS_CREATE_REFERRAL_CODE)
   fun createReferralCode(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>

   @GET(Constants.WS_STAKING_OPTIONS)
   fun getStakingOptions(@Header(Constants.KEY_AUTHORIZATION) token: String): Call<StakingOptionsModel>

    @GET(Constants.WS_GET_TRACK_LIST)
    fun getTrackList(@Header(Constants.KEY_AUTHORIZATION) token: String): Call<HomeModel>

    @POST(Constants.WS_ADD_USER_FAVOURITE_COIN)
    fun adduserFavouriteCoin(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>
    @POST(Constants.WS_REMOVE_USER_FAVORITE)
    fun removeUserFavouriteCoin(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>
    @POST(Constants.WS_GET_EXCHANGE_ACCESS_TOKEN)
    fun getOrExchangeAccessToken(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>
    @GET(Constants.WS_GET_CARD_LIST)
    fun getCardList(@Header(Constants.KEY_AUTHORIZATION) token: String): Call<CardModel>
    @POST(Constants.WS_ACTIVATE_DEACTIVATE_CARD)
    fun activateOrDeactivateCard(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>
    @POST(Constants.WS_ACTIVATE_DEACTIVATE_STAKE_STATUS)
    fun activateOrDeactivateStake(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>
    @PUT(Constants.WS_UPDATE_COIN_NOTIFICATIONS)
    fun updateCoinNotifications(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>
    @POST(Constants.WS_SAVE_CONTACT_SUPPORT_MESSAGE)
    fun saveContactSupportMessage(@Body hashMap: HashMap<String, String>,@Header(Constants.KEY_AUTHORIZATION) token: String): Call<EmailConfirmationRequestModel>
    @GET(Constants.WS_GET_CONTACT_SUPPORT_MESSAGE_LIST)
    fun getContactSupportMessage(@Header(Constants.KEY_AUTHORIZATION) token: String): Call<GetContactSupportModel>
}