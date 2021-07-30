package com.app.alchemi.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.EmailConfirmationRequestModel
import com.app.alchemi.repository.MainActivityRepository


internal class AddSSNViewModel : ViewModel() {
    private var signUpMutableLiveData: MutableLiveData<EmailConfirmationRequestModel>? = null
    fun addUserSSN(hashMap: HashMap<String, String>, view: View?): LiveData<EmailConfirmationRequestModel>? {
        signUpMutableLiveData = MainActivityRepository.addUserSSNCall(hashMap,view)
        return signUpMutableLiveData
    }
}


