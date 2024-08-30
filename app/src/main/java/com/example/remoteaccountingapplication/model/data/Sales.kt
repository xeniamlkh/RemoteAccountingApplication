package com.example.remoteaccountingapplication.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class Sales(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo val dateCalculation: Long,
    @ColumnInfo val date: String,
    @ColumnInfo val product: String,
    @ColumnInfo val price: Double,
    @ColumnInfo val number: Int,
    @ColumnInfo(name = "payment type") val paymentType: String,
    @ColumnInfo(name = "sale type") val saleType: String,
    @ColumnInfo val name: String,
    @ColumnInfo val comment: String,
    @ColumnInfo val total: Double
)
