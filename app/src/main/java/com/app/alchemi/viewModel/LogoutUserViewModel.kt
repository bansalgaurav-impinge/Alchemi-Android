package com.app.alchemi.viewModel


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.EmailConfirmationRequestModel
import com.app.alchemi.repository.MainActivityRepository


internal class LogoutUserViewModel : ViewModel() {
    private var logoutUserMutableLiveData: MutableLiveData<EmailConfirmationRequestModel>? = null
     fun logoutUser(token:String, view: View?): LiveData<EmailConfirmationRequestModel>? {
         logoutUserMutableLiveData = MainActivityRepository.logoutUser(token, view)
        return logoutUserMutableLiveData
    }
}


