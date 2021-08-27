package com.app.alchemi.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.AddPinModel
import com.app.alchemi.repository.MainActivityRepository


internal class AddUserPinViewModel : ViewModel() {
    private var signUpMutableLiveData: MutableLiveData<AddPinModel>? = null
    fun addUserPin(hashMap: HashMap<String, String>, token: String, view: View?): LiveData<AddPinModel>? {
        signUpMutableLiveData = MainActivityRepository.addUserPinCall(hashMap,token,view)
        return signUpMutableLiveData
    }
}


