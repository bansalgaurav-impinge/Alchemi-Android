package com.app.alchemi.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.CountryListModel
import com.app.alchemi.repository.MainActivityRepository


internal class CountryListViewModel : ViewModel() {
    private var countryListMutableLiveData: MutableLiveData<CountryListModel>? = null
    fun getCountryList(): LiveData<CountryListModel>? {
        countryListMutableLiveData = MainActivityRepository.getCountryListAPI()
        return countryListMutableLiveData
    }
}


