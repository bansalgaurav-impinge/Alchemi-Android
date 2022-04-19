package com.app.alchemi.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.EmailConfirmationRequestModel
import com.app.alchemi.models.LinkToken
import com.app.alchemi.repository.MainActivityRepository


internal class LinkTokenViewModel : ViewModel() {
    private var signUpMutableLiveData: MutableLiveData<LinkToken>? = null
    fun getCreateLinkToken(view: View?, token:String): LiveData<LinkToken>? {
        signUpMutableLiveData = MainActivityRepository.getCreateLinkToken(view,token = token)
        return signUpMutableLiveData
    }
}


