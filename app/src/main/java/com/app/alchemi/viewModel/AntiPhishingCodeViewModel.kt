package com.app.alchemi.viewModel


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.EmailConfirmationRequestModel
import com.app.alchemi.repository.MainActivityRepository


internal class AntiPhishingCodeViewModel : ViewModel() {
    private var mutableLiveData: MutableLiveData<EmailConfirmationRequestModel>? = null
    fun antiPhishingCode(hashMap: HashMap<String, String>, view: View?,token:String): LiveData<EmailConfirmationRequestModel>? {
        mutableLiveData = MainActivityRepository.saveAntiPhishingCode(hashMap, view,token)
        return mutableLiveData
    }

}


