package com.app.alchemi.viewModel


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.VeriffSDKSessionUrlModel
import com.app.alchemi.repository.MainActivityRepository


internal class VeriffSessionUrlViewModel : ViewModel() {
    private var veriffSessionUrlMutableLiveData: MutableLiveData<VeriffSDKSessionUrlModel>? = null
     fun getVeriffSessionUrl(hashMap: HashMap<String, String>, view: View?): LiveData<VeriffSDKSessionUrlModel>? {
         veriffSessionUrlMutableLiveData = MainActivityRepository.getVeriffSessionUrl(hashMap, view)
        return veriffSessionUrlMutableLiveData
    }

}


