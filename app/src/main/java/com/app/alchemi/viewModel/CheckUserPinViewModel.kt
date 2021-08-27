package com.app.alchemi.viewModel


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.EmailConfirmationRequestModel
import com.app.alchemi.repository.MainActivityRepository


internal class CheckUserPinViewModel : ViewModel() {
    private var mutableLiveData: MutableLiveData<EmailConfirmationRequestModel>? = null
    fun verifyUserPin(hashMap: HashMap<String, String>, view: View?,token:String): LiveData<EmailConfirmationRequestModel>? {
        mutableLiveData = MainActivityRepository.checkUserPin(hashMap, view,token)
        return mutableLiveData
    }
    fun forgotPassCode(hashMap: HashMap<String, String>, view: View?,token:String): LiveData<EmailConfirmationRequestModel>? {
        mutableLiveData = MainActivityRepository.forgotPasscode(hashMap, view,token)
        return mutableLiveData
    }
    fun verifyReferralCode(hashMap: HashMap<String, String>, view: View?,token:String): LiveData<EmailConfirmationRequestModel>? {
        mutableLiveData = MainActivityRepository.verifyReferralCode(hashMap, view,token)
        return mutableLiveData
    }
    fun createReferralCode(hashMap: HashMap<String, String>, view: View?,token:String): LiveData<EmailConfirmationRequestModel>? {
        mutableLiveData = MainActivityRepository.createReferralCode(hashMap, view, token)
        return mutableLiveData
    }
    fun addRemoveFavouriteCoin(hashMap: HashMap<String, String>, view: View?,token:String,requestType:Int): LiveData<EmailConfirmationRequestModel>? {
        mutableLiveData = MainActivityRepository.addRemoveFavouriteCOIN(hashMap, view, token,requestType)
        return mutableLiveData
    }
    fun getOrExchangeAccessToken(hashMap: HashMap<String, String>, view: View?,token:String): LiveData<EmailConfirmationRequestModel>? {
        mutableLiveData = MainActivityRepository.getOrExchangeAccessToken(hashMap, view, token)
        return mutableLiveData
    }
    fun activateOrDeactivateCard(hashMap: HashMap<String, String>, view: View?,token:String): LiveData<EmailConfirmationRequestModel>? {
        mutableLiveData = MainActivityRepository.activateOrDeactivate(hashMap, view, token)
        return mutableLiveData
    }
    fun activateOrDeactivateStakeStatus(hashMap: HashMap<String, String>, view: View?,token:String): LiveData<EmailConfirmationRequestModel>? {
        mutableLiveData = MainActivityRepository.activateOrDeactivateStakeStatus(hashMap, view, token)
        return mutableLiveData
    }
    fun updateCoinNotifications(hashMap: HashMap<String, String>, view: View?,token:String): LiveData<EmailConfirmationRequestModel>? {
        mutableLiveData = MainActivityRepository.updateCoinNotifications(hashMap, view, token)
        return mutableLiveData
    }
    fun saveContactSupportMessage(hashMap: HashMap<String, String>, view: View?,token:String): LiveData<EmailConfirmationRequestModel>? {
        mutableLiveData = MainActivityRepository.saveContactSupportMessage(hashMap, view, token)
        return mutableLiveData
    }
}


