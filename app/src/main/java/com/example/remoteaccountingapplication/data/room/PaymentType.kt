package com.example.remoteaccountingapplication.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_type")
data class PaymentType(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "payment type") val paymentType: String
)
