package com.app.alchemi.viewModel


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.EmailConfirmationRequestModel
import com.app.alchemi.repository.MainActivityRepository


internal class UploadDocumentsViewModel : ViewModel() {
    private var signUpMutableLiveData: MutableLiveData<EmailConfirmationRequestModel>? = null
     fun uploadDocuments(editProfile: Boolean,user: String,firstName:String,lastName:String, countryName: String,countryCode: String,selfiePath: String,isVerified:Boolean,token: String, view: View?): LiveData<EmailConfirmationRequestModel>? {
        if (editProfile){
            signUpMutableLiveData = MainActivityRepository.updateProfile(user, firstName, lastName, countryName, countryCode, selfiePath, isVerified, token,view)
        }else {
            signUpMutableLiveData = MainActivityRepository.uploadDocumentsCall(user, firstName, lastName, countryName, countryCode, isVerified, view)
        }
         return signUpMutableLiveData
    }
}


