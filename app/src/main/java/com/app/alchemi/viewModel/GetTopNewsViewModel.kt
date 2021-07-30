package com.app.alchemi.viewModel


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.AllNewsModel
import com.app.alchemi.repository.MainActivityRepository.getAllTopNews


class GetTopNewsViewModel : ViewModel() {
    private var getTopNewsMutableLiveData: MutableLiveData<AllNewsModel>? = null
     fun getTopNews(token:String, view: View?): LiveData<AllNewsModel>?{
         getTopNewsMutableLiveData = getAllTopNews(token, view)
        return getTopNewsMutableLiveData
    }
}


