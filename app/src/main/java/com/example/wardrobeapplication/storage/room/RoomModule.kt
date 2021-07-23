package com.example.wardrobeapplication.storage.room

class RoomModule(private var pairsDatabase: PairsDatabase) {

    fun getPairsDao(): PairDao{
        return pairsDatabase.pairDao
    }
}