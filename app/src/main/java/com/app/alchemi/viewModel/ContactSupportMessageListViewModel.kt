package com.app.alchemi.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.GetContactSupportModel
import com.app.alchemi.repository.MainActivityRepository


internal class ContactSupportMessageListViewModel : ViewModel() {
    private var mutableLiveData: MutableLiveData<GetContactSupportModel>? = null
    fun getContactSupportMessageList(view: View?,token:String): LiveData<GetContactSupportModel>? {
        mutableLiveData = MainActivityRepository.getContactSupportMessageList(view,token)
        return mutableLiveData
    }
}


