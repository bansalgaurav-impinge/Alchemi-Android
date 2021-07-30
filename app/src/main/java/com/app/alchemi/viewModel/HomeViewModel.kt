package com.app.alchemi.viewModel


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.alchemi.models.HomeModel
import com.app.alchemi.repository.MainActivityRepository.homeRequestCall
import com.app.alchemi.repository.MainActivityRepository.trackList


class HomeViewModel : ViewModel() {
    private var mutableLiveData: MutableLiveData<HomeModel>? = null
    fun homeData(token: String, view: View?): LiveData<HomeModel>? {
        mutableLiveData = homeRequestCall(token, view)
        return mutableLiveData
    }

    fun getTrackList(token: String, view: View?): LiveData<HomeModel>? {
        mutableLiveData = trackList(token, view)
        return mutableLiveData
    }


}

