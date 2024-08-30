package com.example.remoteaccountingapplication.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "names")
data class Names(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo val name: String
)
