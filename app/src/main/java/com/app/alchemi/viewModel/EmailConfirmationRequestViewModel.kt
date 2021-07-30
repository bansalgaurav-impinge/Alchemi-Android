package com.app.alchemi.viewModel


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.EmailConfirmationRequestModel
import com.app.alchemi.repository.MainActivityRepository


internal class EmailConfirmationRequestViewModel : ViewModel() {
    private var signUpMutableLiveData: MutableLiveData<EmailConfirmationRequestModel>? = null
     fun getEmailConfimationRequest(hashMap: HashMap<String, String>, view: View?): LiveData<EmailConfirmationRequestModel>? {
         if (Constants.isSignIn){
             signUpMutableLiveData = MainActivityRepository.signInCall(hashMap,view)
         }else{
             signUpMutableLiveData = MainActivityRepository.getEmailConfirmationRquestApiCall(hashMap, view)
         }
        return signUpMutableLiveData
    }
    fun getUpdateEmailConfimationRequest(hashMap: HashMap<String, String>, view: View?,token:String): LiveData<EmailConfirmationRequestModel>? {
            signUpMutableLiveData = MainActivityRepository.getUpdateEmailConfirmationApi(hashMap, view,token)
        return signUpMutableLiveData
    }
}


