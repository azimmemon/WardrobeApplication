package com.example.wardrobeapplication.storage

import androidx.lifecycle.LiveData
import com.example.wardrobeapplication.model.PairDataModel
import com.example.wardrobeapplication.storage.room.PairsDatabase

class MainStorageSource(private var pairsDatabase: PairsDatabase) {

    suspend fun insertPair(data: PairDataModel){
        pairsDatabase.pairDao.saveWear(data)
    }


    fun getWear(): LiveData<List<PairDataModel>> {
        return pairsDatabase.pairDao.getAllWear()
    }


    fun getBottomWear(bottomWear: String): LiveData<List<PairDataModel>>{
        return pairsDatabase.pairDao.getBottomWear(bottomWear)
    }

    fun getTopWear(topWear: String): LiveData<List<PairDataModel>>{
        return pairsDatabase.pairDao.getTopsWear(topWear)
    }


}