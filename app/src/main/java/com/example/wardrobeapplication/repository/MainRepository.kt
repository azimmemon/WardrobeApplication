package com.example.wardrobeapplication.repository

import androidx.lifecycle.LiveData
import com.example.wardrobeapplication.model.PairDataModel


class MainRepository(private var mMainDataStoreFactory: MainDataStoreFactory) {

    suspend fun insertPair(data: PairDataModel){
        mMainDataStoreFactory.mMainStorageSource.insertPair(data)
    }


    fun getWear(): LiveData<List<PairDataModel>>{
        return mMainDataStoreFactory.mMainStorageSource.getWear()
    }

    fun getBottomWear(bottomWear: String): LiveData<List<PairDataModel>>{
        return mMainDataStoreFactory.mMainStorageSource.getBottomWear(bottomWear)
    }

    fun getTopWear(topWear: String): LiveData<List<PairDataModel>>{
        return mMainDataStoreFactory.mMainStorageSource.getTopWear(topWear)
    }



}