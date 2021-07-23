package com.example.wardrobeapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clothes")
data class PairDataModel(@ColumnInfo(name = "title") var title: String, @ColumnInfo(name = "image") var imageString: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}