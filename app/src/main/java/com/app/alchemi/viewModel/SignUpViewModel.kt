package com.app.alchemi.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.ServicesSetterGetter
import com.app.alchemi.models.SignUpModel
import com.app.alchemi.repository.MainActivityRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class SignUpViewModel : ViewModel() {
    private var signUpMutableLiveData: MutableLiveData<SignUpModel>? = null
    suspend fun getUserInfo() : LiveData<SignUpModel>? {
        signUpMutableLiveData = MainActivityRepository.getSignUPApiCall()
        return signUpMutableLiveData
    }
}


