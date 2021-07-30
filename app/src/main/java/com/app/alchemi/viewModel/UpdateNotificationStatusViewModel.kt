package com.app.alchemi.viewModel


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.EmailConfirmationRequestModel
import com.app.alchemi.models.SystemStatusModel
import com.app.alchemi.repository.MainActivityRepository


internal class UpdateNotificationStatusViewModel : ViewModel() {
    private var mutableLiveData: MutableLiveData<EmailConfirmationRequestModel>? = null
    private var mutableLiveSystemStatus: MutableLiveData<SystemStatusModel>? = null
    fun updateNotificationStatus(hashMap: HashMap<String, Boolean>, view: View?,token:String): LiveData<EmailConfirmationRequestModel>? {
        mutableLiveData = MainActivityRepository.updateNotificationStatus(hashMap, view,token)
        return mutableLiveData
    }
    fun updatePassCodeStatus(hashMap: HashMap<String, Boolean>, view: View?,token:String): LiveData<EmailConfirmationRequestModel>? {
        mutableLiveData = MainActivityRepository.updatePassCodeStatus(hashMap, view,token)
        return mutableLiveData
    }
    fun getSystemStatusFun(token:String,view: View?): LiveData<SystemStatusModel>? {
        mutableLiveSystemStatus =  MainActivityRepository.getSystemStatus(token, view)
        return mutableLiveSystemStatus
    }
}


