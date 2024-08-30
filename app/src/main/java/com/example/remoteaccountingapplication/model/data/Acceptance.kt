package com.example.remoteaccountingapplication.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "acceptance")
data class Acceptance(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo val dateCalculation: Long,
    @ColumnInfo val date: String,
    @ColumnInfo val name: String,
    @ColumnInfo val product: String,
    @ColumnInfo val price: Double,
    @ColumnInfo val number: Int
)