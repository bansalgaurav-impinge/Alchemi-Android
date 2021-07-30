package com.app.alchemi.viewModel


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.EmailConfirmationRequestModel
import com.app.alchemi.models.UserDetailModel
import com.app.alchemi.repository.MainActivityRepository


internal class UserDetailViewModel : ViewModel() {
    private var userDetailMutableLiveData: MutableLiveData<UserDetailModel>? = null
     fun getUserDetail(token:String, view: View?): LiveData<UserDetailModel>? {
         userDetailMutableLiveData = MainActivityRepository.getUserDetail(token, view)
        return userDetailMutableLiveData
    }
}


