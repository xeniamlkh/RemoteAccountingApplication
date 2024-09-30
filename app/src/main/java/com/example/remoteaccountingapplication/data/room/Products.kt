package com.example.remoteaccountingapplication.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Products(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo val product: String,
    @ColumnInfo val price: Double,
    @ColumnInfo val remains: Int,
    @ColumnInfo val physical: Boolean
)
