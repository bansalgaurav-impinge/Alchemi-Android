package com.app.alchemi.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import com.app.alchemi.R
import com.app.alchemi.models.*
import com.app.alchemi.retrofit.RetrofitClient
import com.app.alchemi.utils.AlchemiApplication.Companion.alchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.views.activities.MainActivity
import id.zelory.compressor.Compressor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object MainActivityRepository {
    var alert: AlertDialog?=null
    val serviceSetterGetter = MutableLiveData<ServicesSetterGetter>()

    fun getServicesApiCall(): MutableLiveData<ServicesSetterGetter> {

        val call = RetrofitClient.apiInterface.getCategories()

        call.enqueue(object: Callback<ServicesSetterGetter> {
            override fun onFailure(call: Call<ServicesSetterGetter>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ServicesSetterGetter>,
                response: Response<ServicesSetterGetter>
            ) {
                Log.v("DEBUG : ", response.body().toString())
                val data = response.body()
                val msg = data!!.message
                serviceSetterGetter.value = ServicesSetterGetter(msg)
            }
        })

        return serviceSetterGetter
    }

    /**
     *  Register Call
     */
    suspend fun getSignUPApiCall(): MutableLiveData<SignUpModel> {
        val signupMutableLiveData = MutableLiveData<SignUpModel>()

        val call = RetrofitClient.apiInterface.signUP()

        call.enqueue(object: Callback<SignUpModel> {
            override fun onFailure(call: Call<SignUpModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<SignUpModel>,
                response: Response<SignUpModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                val data = response.body()
                signupMutableLiveData.value = data!!
            }
        })

        return signupMutableLiveData
    }

    /**
     *  email confirmation
     */
     fun getEmailConfirmationRquestApiCall(hashMap: HashMap<String, String>, view: View?): MutableLiveData<EmailConfirmationRequestModel> {
        val signupMutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.emailConfirmationRequest(hashMap).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                    call: Call<EmailConfirmationRequestModel>,
                    response: Response<EmailConfirmationRequestModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        signupMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,view?.context?.getString(R.string.error_message))
                        }else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return signupMutableLiveData
    }




    /***
     *  Confirm Phone API
     */
    fun getPhoneConfirmationRquestCall(hashMap: HashMap<String, String>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val signupMutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        var call: Call<EmailConfirmationRequestModel>? = null
        if (token.isEmpty()) {
            call = RetrofitClient.apiInterface.requestConfirmPhone(hashMap)
        }else{
            call = RetrofitClient.apiInterface.requestConfirmUpdatePhone(hashMap,token)
        }
        call.enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.dismissProgress()
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                call: Call<EmailConfirmationRequestModel>,
                response: Response<EmailConfirmationRequestModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        signupMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                        if (response.code()==400) {
                            ViewUtils.showSnackBar(view, view?.context?.getString(R.string.server_error))
                        }else {
                            ViewUtils.showSnackBar(view, response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    if (response.code()==200) {
                        ViewUtils.showSnackBar(view, view!!.context.getString(R.string.sent_otp_to_phone_number))
                    }
                        else{
                        ViewUtils.showSnackBar(view, view?.context?.getString(R.string.server_error))
                        }


                }
            }
        })

        return signupMutableLiveData
    }

    /***
     * VERIFY OTP
     */
    fun getVerifyOTPRquestCall(hashMap: HashMap<String, String>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val signupMutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        var call: Call<EmailConfirmationRequestModel>? = null
        if (token.isEmpty()) {
            call = RetrofitClient.apiInterface.VerifyOTP(hashMap)
        }else{
            call = RetrofitClient.apiInterface.VerifyUpdateOTP(hashMap,token)
        }
      call.enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.dismissProgress()
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                call: Call<EmailConfirmationRequestModel>,
                response: Response<EmailConfirmationRequestModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        signupMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==400) {
                            ViewUtils.showSnackBar(view, "Invalid OTP")
                        }else {
                            ViewUtils.showSnackBar(view, response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }

            }
        })

        return signupMutableLiveData
    }

    /***
     * Upload Documents
     */
    fun uploadDocumentsCall(
        user: String,
        firstName: String,
        lastName: String,
        countryName: String,
        countryCode: String,
        isVerified: Boolean,
        view: View?
    ): MutableLiveData<EmailConfirmationRequestModel> {
        val signupMutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        val tokenRequestUser = user.toRequestBody("text/plain".toMediaTypeOrNull())
        val tokenRequestFirstName = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
        val tokenRequestLastname = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
        val tokenRequestCountryName =
            countryName.toRequestBody("text/plain".toMediaTypeOrNull())
        val tokenRequestCountryCode =
            countryCode.toRequestBody("text/plain".toMediaTypeOrNull())
        val tokenRequestisVerified =
            ("" + isVerified).toRequestBody("text/plain".toMediaTypeOrNull())
        RetrofitClient.apiInterface.updateDocuments(
            tokenRequestUser,
            tokenRequestFirstName,
            tokenRequestLastname,
            tokenRequestCountryName,
            tokenRequestCountryCode,
            isVerified
        ).enqueue(object : Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.dismissProgress()
                ViewUtils.showSnackBar(view, "" + t.message.toString())
            }

            override fun onResponse(
                call: Call<EmailConfirmationRequestModel>,
                response: Response<EmailConfirmationRequestModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body() != null) {
                        val data = response.body()
                        //val msg = data!!.message
                        signupMutableLiveData.value = data!!
                    } else if (response.errorBody() != null) {
                       
                        ViewUtils.showSnackBar(view, response.message())
                    } else {
                        ViewUtils.showSnackBar(view, response.message())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view, view?.context?.getString(R.string.server_error))
                }

            }
        })

        return signupMutableLiveData
    }

    /***
     * Get Media mime type
     */
    fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    /**
     *  method to sign In
     */
    fun signInCall(hashMap: HashMap<String, String>, view: View?): MutableLiveData<EmailConfirmationRequestModel> {
        val signupMutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.emailConfirmationRequestLogin(hashMap).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                    call: Call<EmailConfirmationRequestModel>,
                    response: Response<EmailConfirmationRequestModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        signupMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,"User Does Not Exist Please Sign-Up")
                        }else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return signupMutableLiveData
    }

    /***
     * ADD SSN NUMBER
     */



    fun addUserSSNCall(hashMap: HashMap<String, String>, view: View?): MutableLiveData<EmailConfirmationRequestModel> {
        val signupMutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.addUserSSN(hashMap).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.dismissProgress()
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                call: Call<EmailConfirmationRequestModel>,
                response: Response<EmailConfirmationRequestModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        signupMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==400) {
                            ViewUtils.showSnackBar(view, view?.context?.getString(R.string.server_error))
                        }else {
                            ViewUtils.showSnackBar(view, response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    if (response.code()==200) {
                        ViewUtils.showSnackBar(view, "We have sent you an OTP to confirm your phone number")
                    }
                    else{
                        ViewUtils.showSnackBar(view, view?.context?.getString(R.string.server_error))
                    }


                }
            }
        })

        return signupMutableLiveData
    }

    /**
     * add pin code
     */
    fun addUserPinCall(hashMap: HashMap<String, String>, token: String, view: View?): MutableLiveData<AddPinModel> {
        val signupMutableLiveData = MutableLiveData<AddPinModel>()
        var call: Call<AddPinModel>? = null
        if (token.isEmpty()|| token.isNullOrEmpty()) {
            call = RetrofitClient.apiInterface.addUserPin(hashMap)
        }else{
            call = RetrofitClient.apiInterface.updateUserPin(hashMap,token)
        }
        call.enqueue(object: Callback<AddPinModel> {
            override fun onFailure(call: Call<AddPinModel>, t: Throwable) {
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.dismissProgress()
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                call: Call<AddPinModel>,
                response: Response<AddPinModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        signupMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==400) {
                            ViewUtils.showSnackBar(view, view?.context?.getString(R.string.server_error))
                        }else {
                            ViewUtils.showSnackBar(view, response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception) {
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view, view?.context?.getString(R.string.server_error))
                }
            }
        })

        return signupMutableLiveData
    }

    /**
     *  get country List
     */
    fun getCountryListAPI(): MutableLiveData<CountryListModel> {
        val countryListMutableLiveData = MutableLiveData<CountryListModel>()
        RetrofitClient.apiInterface.getCountryList().enqueue(object: Callback<CountryListModel> {
            override fun onFailure(call: Call<CountryListModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                    call: Call<CountryListModel>,
                    response: Response<CountryListModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                val data = response.body()
                countryListMutableLiveData.value = data!!
            }
        })

        return countryListMutableLiveData
    }
    /***
     * GET USER DETAIL API
     */

    fun getUserDetail(token: String, view: View?): MutableLiveData<UserDetailModel> {
        val userDetailMutableLiveData = MutableLiveData<UserDetailModel>()
        RetrofitClient.apiInterface.getUserDetail(token).enqueue(object: Callback<UserDetailModel> {
            override fun onFailure(call: Call<UserDetailModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                    call: Call<UserDetailModel>,
                    response: Response<UserDetailModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        userDetailMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,view?.context?.getString(R.string.error_message))
                        }else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return userDetailMutableLiveData
    }

    /**
     * update email
     */
    fun getUpdateEmailConfirmationApi(hashMap: HashMap<String, String>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val signupMutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.emailUpdateConfirmationRequest(hashMap,token).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                    call: Call<EmailConfirmationRequestModel>,
                    response: Response<EmailConfirmationRequestModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        signupMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,view?.context?.getString(R.string.error_message))
                        }else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return signupMutableLiveData
    }

    /***
     * Update Profile
     */

    fun updateProfile(
        user: String,
        firstName: String,
        lastName: String,
        countryName: String,
        countryCode: String,
        selfiePath: String,
        isVerified: Boolean,
        token: String,
        view: View?
    ): MutableLiveData<EmailConfirmationRequestModel> {

        val signupMutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        val tokenRequestUser = user.toRequestBody("text/plain".toMediaTypeOrNull())
        val tokenRequestFirstName = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
        val tokenRequestLastname = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
        val tokenRequestCountryName =
            countryName.toRequestBody("text/plain".toMediaTypeOrNull())
        val tokenRequestCountryCode =
            countryCode.toRequestBody("text/plain".toMediaTypeOrNull())
        var call: Call<EmailConfirmationRequestModel>? = null
        if (selfiePath.isNotEmpty() && selfiePath != "") {
            val file = File(selfiePath)
            val compressedImgFile = Compressor(view?.context).compressToFile(file)
            val filePartSelfie = MultipartBody.Part.createFormData(
                Constants.KEY_SELFIE,
                file.name,
                compressedImgFile.asRequestBody(
                    getMimeType(selfiePath).toString().toMediaTypeOrNull()
                )

            )
            call = RetrofitClient.apiInterface.updateProfileImage(
                tokenRequestUser,
                filePartSelfie,
                isVerified,
                token
            )

        }
        if (firstName.isNotEmpty() && firstName != "") {

            call = RetrofitClient.apiInterface.updateProfile(
                tokenRequestUser,
                tokenRequestFirstName,
                tokenRequestLastname,
                tokenRequestCountryName,
                tokenRequestCountryCode,
                isVerified,
                token
            )

        }

        call?.enqueue(object : Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.dismissProgress()
                ViewUtils.showSnackBar(view, "" + t.message.toString())
            }

            override fun onResponse(
                call: Call<EmailConfirmationRequestModel>,
                response: Response<EmailConfirmationRequestModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body() != null) {
                        val data = response.body()
                        //val msg = data!!.message
                        signupMutableLiveData.value = data!!
                    } else if (response.errorBody() != null) {
                       
                        ViewUtils.showSnackBar(view, response.message())
                    } else {
                        ViewUtils.showSnackBar(view, response.message())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view, view?.context?.getString(R.string.server_error))
                }

            }
        })

        return signupMutableLiveData
    }

    /**
     * Log out user
     */

    fun logoutUser(token: String, view: View?): MutableLiveData<EmailConfirmationRequestModel> {
        val logoutUserMutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.logout(token).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                    call: Call<EmailConfirmationRequestModel>,
                    response: Response<EmailConfirmationRequestModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        logoutUserMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==400 or 403){
                            showAlertDialog(view?.context!!,view!!)
                        }else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return logoutUserMutableLiveData
    }

    /**
     * Home API
     */

    fun homeRequestCall(token: String, view: View?): MutableLiveData<HomeModel> {
        val homeMutableLiveData = MutableLiveData<HomeModel>()
        RetrofitClient.apiInterface.homeData(token).enqueue(object: Callback<HomeModel> {
            override fun onFailure(call: Call<HomeModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<HomeModel>,
                response: Response<HomeModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        homeMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==403){
                           showAlertDialog(view!!.context,view!!)
                        }else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return homeMutableLiveData
    }

    /****
     * Get Top News API Data
     */
    fun getAllTopNews(token: String, view: View?): MutableLiveData<AllNewsModel> {
        val homeMutableLiveData = MutableLiveData<AllNewsModel>()
        RetrofitClient.apiInterface.getTopNews(token).enqueue(object: Callback<AllNewsModel> {
            override fun onFailure(call: Call<AllNewsModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<AllNewsModel>,
                                    response: Response<AllNewsModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        homeMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==403){
                            showAlertDialog(view?.context!!,view!!)
                        }else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return homeMutableLiveData
    }

    /**
     *  Get Top crypto currency
     */
    fun getAllTopCryptoCurrency(token: String, view: View?): MutableLiveData<TopCryptoCurrencyModel> {
        val mutableLiveData = MutableLiveData<TopCryptoCurrencyModel>()
        RetrofitClient.apiInterface.getTopCryptoCurrency(token).enqueue(object: Callback<TopCryptoCurrencyModel> {
            override fun onFailure(call: Call<TopCryptoCurrencyModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<TopCryptoCurrencyModel>,
                                    response: Response<TopCryptoCurrencyModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==403){
                            showAlertDialog(view?.context!!,view!!)
                        }else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /***
     * Check User PIN
     */
    fun checkUserPin(hashMap: HashMap<String, String>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val mutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.checkUserPin(hashMap,token).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                    call: Call<EmailConfirmationRequestModel>,
                    response: Response<EmailConfirmationRequestModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){

                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,view?.context?.getString(R.string.incorrect_pin))
                        }
                        else if (response.code()==403){
                          val activity= view?.context as Activity
                            showAlertDialog(view?.context!!,view!!)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }
    /***
     * GET Veriff Session Url
     */

    fun getVeriffSessionUrl(hashMap: HashMap<String, String>, view: View?): MutableLiveData<VeriffSDKSessionUrlModel> {
        val veriffSessionUrlMutableLiveData = MutableLiveData<VeriffSDKSessionUrlModel>()
        RetrofitClient.apiInterface.getVeriffSessionUrl(hashMap).enqueue(object: Callback<VeriffSDKSessionUrlModel> {
            override fun onFailure(call: Call<VeriffSDKSessionUrlModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<VeriffSDKSessionUrlModel>, response: Response<VeriffSDKSessionUrlModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        veriffSessionUrlMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==403){
                            showAlertDialog(view?.context!!,view!!)
                        }else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return veriffSessionUrlMutableLiveData
    }

    /***
     * Update Notification settings status to server
     */

    fun updateNotificationStatus(hashMap: HashMap<String, Boolean>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val mutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.updateNotificationStatus(hashMap,token).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<EmailConfirmationRequestModel>, response: Response<EmailConfirmationRequestModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==400){
                           // ViewUtils.showSnackBar(view,view?.context?.getString(R.string.incorrect_pin))
                        }
                        else if (response.code()==403){
                            showAlertDialog(view?.context!!,view!!)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /***
     *  Update Passcode settings status
     */
    fun updatePassCodeStatus(hashMap: HashMap<String, Boolean>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val mutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.updatePasscodeStatus(hashMap,token).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<EmailConfirmationRequestModel>, response: Response<EmailConfirmationRequestModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null) {
                       

                       if (response.code()==403){
                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /****
     *  save anti phishing Code
     */

    fun saveAntiPhishingCode(hashMap: HashMap<String, String>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val mutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.saveUpdateAntiPhishingCode(hashMap,token).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<EmailConfirmationRequestModel>, response: Response<EmailConfirmationRequestModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       

                        if (response.code()==403){
                            showAlertDialog(view?.context!!,view!!)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /***
     *  Get System Status API
     */
    fun getSystemStatus(token: String,view: View?): MutableLiveData<SystemStatusModel> {
        val mutableLiveData = MutableLiveData<SystemStatusModel>()
        RetrofitClient.apiInterface.getSystemStatus(token).enqueue(object: Callback<SystemStatusModel> {
            override fun onFailure(call: Call<SystemStatusModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<SystemStatusModel>, response: Response<SystemStatusModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       

                        if (response.code()==403){
                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /***
     *  Get History Of particular coin
     */

    fun getCoinHistory(hashMap: HashMap<String, String>, token: String, view: View?): MutableLiveData<CoinHistoryModel> {
        val mutableLiveData = MutableLiveData<CoinHistoryModel>()
        RetrofitClient.apiInterface.getCoinHistory(hashMap,token).enqueue(object: Callback<CoinHistoryModel> {
            override fun onFailure(call: Call<CoinHistoryModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<CoinHistoryModel>, response: Response<CoinHistoryModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==400){
                        }
                        else if (response.code()==403){
                            showAlertDialog(view?.context!!,view!!)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /***
     * History API 2
     */
    fun getCoinHistory2(firstName:String,lastName:String, token: String, view: View?): MutableLiveData<CoinHistoryModel> {

        val signupMutableLiveData = MutableLiveData<CoinHistoryModel>()

        var call: Call<CoinHistoryModel>? = null


            call =  RetrofitClient.apiInterface.getCoinHistory2(firstName,lastName, token)


        call?.enqueue(object: Callback<CoinHistoryModel> {
            override fun onFailure(call: Call<CoinHistoryModel>, t: Throwable) {
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.dismissProgress()
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                call: Call<CoinHistoryModel>,
                response: Response<CoinHistoryModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        signupMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        ViewUtils.showSnackBar(view,response.message())
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }

            }
        })

        return signupMutableLiveData
    }

    /***
     *   forgot password
     */

    fun forgotPasscode(hashMap: HashMap<String, String>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val mutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.forgotPasscode(hashMap,token).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                    call: Call<EmailConfirmationRequestModel>,
                    response: Response<EmailConfirmationRequestModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                        }
                        else if (response.code()==403){
                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }
    /***
     * Create ReferralCode
     */

    fun createReferralCode(hashMap: HashMap<String, String>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val mutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.createReferralCode(hashMap,token).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<EmailConfirmationRequestModel>, response: Response<EmailConfirmationRequestModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                        }
                        else if (response.code()==403){

                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /***
     * VErity refferal Code
     */
    fun verifyReferralCode(hashMap: HashMap<String, String>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val mutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.verifyReferralCode(hashMap).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                    call: Call<EmailConfirmationRequestModel>,
                    response: Response<EmailConfirmationRequestModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,"Invalid Code.")
                        }
                        else if (response.code()==403){
                            showAlertDialog(view?.context!!,view!!)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /***
     * Get Staking Option
     */

    fun getStakingOption(view: View?, token: String): MutableLiveData<StakingOptionsModel> {
        val mutableLiveData = MutableLiveData<StakingOptionsModel>()
        RetrofitClient.apiInterface.getStakingOptions(token).enqueue(object: Callback<StakingOptionsModel> {
            override fun onFailure(call: Call<StakingOptionsModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(
                    call: Call<StakingOptionsModel>,
                    response: Response<StakingOptionsModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,"Invalid Code.")
                        }
                        else if (response.code()==403){
                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /**
     *  GET TRACK LIST API
     */
    fun trackList(token: String, view: View?): MutableLiveData<HomeModel> {
        val homeMutableLiveData = MutableLiveData<HomeModel>()
        RetrofitClient.apiInterface.getTrackList(token).enqueue(object: Callback<HomeModel> {
            override fun onFailure(call: Call<HomeModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<HomeModel>,
                                    response: Response<HomeModel>
            ) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        homeMutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                       
                        if (response.code()==403){
                            showAlertDialog(view!!.context,view!!)
                        }else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return homeMutableLiveData
    }

    /**
     *  Method tp add/ remove favourite coin
     */

    fun addRemoveFavouriteCOIN(hashMap: HashMap<String, String>, view: View?, token: String,requestType: Int): MutableLiveData<EmailConfirmationRequestModel> {
        val mutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        var call: Call<EmailConfirmationRequestModel>? = null
        if (requestType==1){
         call =RetrofitClient.apiInterface.adduserFavouriteCoin(hashMap,token)
        }else{
            call =RetrofitClient.apiInterface.removeUserFavouriteCoin(hashMap,token)
        }
        call.enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<EmailConfirmationRequestModel>, response: Response<EmailConfirmationRequestModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,    view?.context?.getString(R.string.server_error))
                        }
                        else if (response.code()==403){
                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /**
     *  Method to exchange Token
     */

    fun getOrExchangeAccessToken(hashMap: HashMap<String, String>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val mutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.getOrExchangeAccessToken(hashMap,token).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<EmailConfirmationRequestModel>, response: Response<EmailConfirmationRequestModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,    view?.context?.getString(R.string.server_error))
                        }
                        else if (response.code()==403){
                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }



    /***
     *  GET OR CREATE LINK TOKEN
     */
    fun getCreateLinkToken(view: View?, token: String): MutableLiveData<LinkToken> {
        val mutableLiveData = MutableLiveData<LinkToken>()
        RetrofitClient.apiInterface.getLinkToken(token).enqueue(object: Callback<LinkToken> {
            override fun onFailure(call: Call<LinkToken>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<LinkToken>, response: Response<LinkToken>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                        }
                        else if (response.code()==403){
                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /****
     *  Get Card List
     */

    fun getCardList(view: View?, token: String): MutableLiveData<CardModel> {
        val mutableLiveData = MutableLiveData<CardModel>()
        RetrofitClient.apiInterface.getCardList(token).enqueue(object: Callback<CardModel> {
            override fun onFailure(call: Call<CardModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<CardModel>, response: Response<CardModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                        if (response.code()==403){
                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /***
     *  Activate or deactivate card
     */

    fun activateOrDeactivate(hashMap: HashMap<String, String>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val mutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.activateOrDeactivateCard(hashMap,token).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<EmailConfirmationRequestModel>, response: Response<EmailConfirmationRequestModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,    view?.context?.getString(R.string.server_error))
                        }
                        else if (response.code()==403){
                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }
    /***
     *  Activate deactivate stake status
     */
    fun activateOrDeactivateStakeStatus(hashMap: HashMap<String, String>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val mutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.activateOrDeactivateStake(hashMap,token).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<EmailConfirmationRequestModel>, response: Response<EmailConfirmationRequestModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,    view?.context?.getString(R.string.server_error))
                        }
                        else if (response.code()==403){
                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /****
     * Update Coin Notifications
     */
    fun updateCoinNotifications(hashMap: HashMap<String, String>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val mutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.updateCoinNotifications(hashMap,token).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<EmailConfirmationRequestModel>, response: Response<EmailConfirmationRequestModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,    view?.context?.getString(R.string.server_error))
                        }
                        else if (response.code()==403){
                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /****
     *  Save Contact support Message
     */
    fun saveContactSupportMessage(hashMap: HashMap<String, String>, view: View?, token: String): MutableLiveData<EmailConfirmationRequestModel> {
        val mutableLiveData = MutableLiveData<EmailConfirmationRequestModel>()
        RetrofitClient.apiInterface.saveContactSupportMessage(hashMap,token).enqueue(object: Callback<EmailConfirmationRequestModel> {
            override fun onFailure(call: Call<EmailConfirmationRequestModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<EmailConfirmationRequestModel>, response: Response<EmailConfirmationRequestModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,    view?.context?.getString(R.string.server_error))
                        }
                        else if (response.code()==403){
                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /***
     *  GET CONTACT SUPPORT MESSAGE LIST
     */
    fun getContactSupportMessageList(view: View?, token: String): MutableLiveData<GetContactSupportModel> {
        val mutableLiveData = MutableLiveData<GetContactSupportModel>()
        RetrofitClient.apiInterface.getContactSupportMessage(token).enqueue(object: Callback<GetContactSupportModel> {
            override fun onFailure(call: Call<GetContactSupportModel>, t: Throwable) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", t.message.toString())
                ViewUtils.showSnackBar(view,""+t.message.toString())
            }

            override fun onResponse(call: Call<GetContactSupportModel>, response: Response<GetContactSupportModel>) {
                ViewUtils.dismissProgress()
                Log.e("DEBUG : ", response.body().toString())
                try {
                    if (response.body()!=null) {
                        val data = response.body()
                        //val msg = data!!.message
                        mutableLiveData.value = data!!
                    }else if(response.errorBody()!=null){
                        if (response.code()==400){
                            ViewUtils.showSnackBar(view,    view?.context?.getString(R.string.server_error))
                        }
                        else if (response.code()==403){
                            showAlertDialog(view?.context!!,view)
                        }
                        else{
                            ViewUtils.showSnackBar(view,response.message())
                        }
                    }
                    else{
                        ViewUtils.showSnackBar(view,response.message())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    ViewUtils.showSnackBar(view,view?.context?.getString(R.string.server_error))
                }
            }
        })

        return mutableLiveData
    }

    /**
     *  clear stack
     */
    fun clearDataLandToLoginScreen(view: View){
        try {
            alchemiApplication?.clearAllData()
            val activity = view.context as Activity
            activity.startActivity(Intent(view.context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }catch (e:Exception){
            e.toString()
        }

    }

    /**
     *  show dialog
     */
    fun showAlertDialog(context: Context, view: View) {
        if (alert != null && alert!!.isShowing) {
            alert!!.dismiss()
        }
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(context.getString(R.string.attention))
        dialogBuilder.setMessage(context.getString(R.string.login_another_device))
                .setCancelable(false)
                .setPositiveButton(context.resources.getString(R.string.ok)) { dialog, id ->
                    dialog.cancel()
                   clearDataLandToLoginScreen(view)

                }
        ViewUtils.alert = dialogBuilder.create()
        ViewUtils.alert!!.show()
    }
}

