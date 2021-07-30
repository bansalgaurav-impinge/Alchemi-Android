package com.app.alchemi.viewModel


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.TopCryptoCurrencyModel
import com.app.alchemi.repository.MainActivityRepository.getAllTopCryptoCurrency


class GetTopCryptoCurrencyViewModel : ViewModel() {
    private var getTopCryptoCurrencyMutableLiveData: MutableLiveData<TopCryptoCurrencyModel>? = null
     fun getTopCryptoCurrency(token:String, view: View?): LiveData<TopCryptoCurrencyModel>?{
         getTopCryptoCurrencyMutableLiveData = getAllTopCryptoCurrency(token, view)
        return getTopCryptoCurrencyMutableLiveData
    }
}


