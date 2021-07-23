package com.example.wardrobeapplication.storage.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wardrobeapplication.model.PairDataModel

@Dao
interface PairDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWear(pairDataModel: PairDataModel)

    @Query("SELECT * FROM clothes")
    fun getAllWear(): LiveData<List<PairDataModel>>

    @Query("SELECT * FROM clothes where title = :topsWear")
    fun getTopsWear(topsWear: String): LiveData<List<PairDataModel>>

    @Query("SELECT * FROM clothes where title = :bottomWear")
    fun getBottomWear(bottomWear: String): LiveData<List<PairDataModel>>

}