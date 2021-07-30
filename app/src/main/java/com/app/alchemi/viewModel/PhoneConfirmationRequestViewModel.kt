package com.app.alchemi.viewModel


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.EmailConfirmationRequestModel
import com.app.alchemi.repository.MainActivityRepository


internal class PhoneConfirmationRequestViewModel : ViewModel() {
    private var signUpMutableLiveData: MutableLiveData<EmailConfirmationRequestModel>? = null
     fun getPhoneConfirmationRquest(hashMap: HashMap<String, String>, view: View?, token: String,): LiveData<EmailConfirmationRequestModel>? {
        signUpMutableLiveData = MainActivityRepository.getPhoneConfirmationRquestCall(hashMap,view,token)
        return signUpMutableLiveData
    }
}


