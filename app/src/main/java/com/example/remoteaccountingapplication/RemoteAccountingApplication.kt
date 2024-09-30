package com.example.remoteaccountingapplication

import android.app.Application
import com.example.remoteaccountingapplication.data.room.RemoteAccountingDatabase
import com.example.remoteaccountingapplication.data.repository.RoomRepository

class RemoteAccountingApplication : Application() {

    val database: RemoteAccountingDatabase by lazy { RemoteAccountingDatabase.getDatabase(this) }
    val repository: RoomRepository by lazy {
        RoomRepository(
            database.salesDao(),
            database.productsDao(),
            database.namesDao(),
            database.paymentTypeDao(),
            database.saleTypeDao(),
            database.receiptDao()
        )
    }
}