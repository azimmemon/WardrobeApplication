package com.example.wardrobeapplication.storage.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wardrobeapplication.model.PairDataModel

@Database(entities = [PairDataModel::class], version = 3, exportSchema = false)
@TypeConverters()
abstract class PairsDatabase: RoomDatabase() {

    abstract val pairDao: PairDao

    companion object {
        private var INSTANCE: PairsDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): PairsDatabase {
            if (INSTANCE == null) {
                synchronized(lock) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            Room.databaseBuilder(context.applicationContext, PairsDatabase::class.java, "wears.db").build()
                    }
                    return INSTANCE as PairsDatabase
                }
            }
            return INSTANCE as PairsDatabase
        }
    }

}