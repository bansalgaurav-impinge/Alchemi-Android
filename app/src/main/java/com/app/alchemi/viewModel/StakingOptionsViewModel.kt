package com.app.alchemi.viewModel


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.EmailConfirmationRequestModel
import com.app.alchemi.models.StakingOptionsModel
import com.app.alchemi.repository.MainActivityRepository


internal class StakingOptionsViewModel : ViewModel() {
    private var mutableLiveData: MutableLiveData<StakingOptionsModel>? = null
    fun getStakingOptions(hashMap: HashMap<String, String>, view: View?,token:String): LiveData<StakingOptionsModel>? {
        mutableLiveData = MainActivityRepository.getStakingOption(hashMap, view, token)
        return mutableLiveData
    }
}


