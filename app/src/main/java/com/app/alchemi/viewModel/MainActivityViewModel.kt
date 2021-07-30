package com.app.alchemi.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.ServicesSetterGetter
import com.app.alchemi.repository.MainActivityRepository

class MainActivityViewModel : ViewModel() {
    var servicesLiveData: MutableLiveData<ServicesSetterGetter>? = null
    fun getUser() : LiveData<ServicesSetterGetter>? {
        servicesLiveData = MainActivityRepository.getServicesApiCall()
        return servicesLiveData
    }

}