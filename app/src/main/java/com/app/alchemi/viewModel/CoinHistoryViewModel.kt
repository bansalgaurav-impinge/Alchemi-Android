package com.app.alchemi.viewModel


import android.provider.SyncStateContract
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.CoinHistoryModel
import com.app.alchemi.repository.MainActivityRepository


internal class CoinHistoryViewModel : ViewModel() {
    private var mutableLiveData: MutableLiveData<CoinHistoryModel>? = null
    fun getCoinHistory(hashMap: HashMap<String, String>,token:String, view: View?): LiveData<CoinHistoryModel>? {
        mutableLiveData=MainActivityRepository.getCoinHistory2(hashMap[Constants.KEY_CRYPTO]!!,hashMap[Constants.KEY_LIMIT]!!,token,view)
        return mutableLiveData
    }

}


