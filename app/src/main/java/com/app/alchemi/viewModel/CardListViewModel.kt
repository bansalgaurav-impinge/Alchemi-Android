package com.app.alchemi.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.CardModel
import com.app.alchemi.repository.MainActivityRepository


internal class CardListViewModel : ViewModel() {
    private var mutableLiveData: MutableLiveData<CardModel>? = null
    fun getCardList(view: View?,token:String): LiveData<CardModel>? {
        mutableLiveData = MainActivityRepository.getCardList( view, token)
        return mutableLiveData
    }
}


