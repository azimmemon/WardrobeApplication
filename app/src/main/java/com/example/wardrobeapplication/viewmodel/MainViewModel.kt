package com.example.wardrobeapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.wardrobeapplication.model.PairDataModel
import com.example.wardrobeapplication.repository.MainRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(private var mMainRepository: MainRepository): ViewModel() {


    fun observePairLiveData() : LiveData<List<PairDataModel>>{
        return mMainRepository.getWear()
    }

    fun insertPair(data: PairDataModel){
        GlobalScope.launch {
            mMainRepository.insertPair(data)
        }
    }


    fun getBottomWear(bottomWear: String): LiveData<List<PairDataModel>>{
        return mMainRepository.getBottomWear(bottomWear)
    }

    fun getTopWear(topWear: String): LiveData<List<PairDataModel>>{
        return mMainRepository.getTopWear(topWear)
    }

}