package com.example.remoteaccountingapplication.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Sales::class, Products::class, Names::class, PaymentType::class, SaleType::class, Receipt::class],
    version = 14,
    exportSchema = false
)
abstract class RemoteAccountingDatabase : RoomDatabase() {

    abstract fun salesDao(): SalesDao
    abstract fun productsDao(): ProductsDao
    abstract fun namesDao(): NamesDao
    abstract fun paymentTypeDao(): PaymentTypeDao
    abstract fun saleTypeDao(): SaleTypeDao
    abstract fun acceptanceDao(): ReceiptDao

    companion object {
        @Volatile
        private var INSTANCE: RemoteAccountingDatabase? = null

        fun getDatabase(context: Context): RemoteAccountingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RemoteAccountingDatabase::class.java,
                    "remote_accounting_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}