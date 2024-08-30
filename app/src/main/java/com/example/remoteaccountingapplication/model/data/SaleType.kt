package com.example.remoteaccountingapplication.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sale_type")
data class SaleType(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "sale type") val saleType: String
)
