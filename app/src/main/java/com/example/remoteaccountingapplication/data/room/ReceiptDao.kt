package com.example.remoteaccountingapplication.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReceiptProduct(receiptProduct: Receipt)

    @Query("SELECT * FROM receipt WHERE dateCalculation BETWEEN :startOfDay AND :endOfDay")
    fun getListOfReceiptByDate(startOfDay: Long, endOfDay: Long): Flow<List<Receipt>>

    @Query("SELECT dateCalculation||','||date||','||name||','||product||','||price||','||number FROM receipt")
    fun dailyReceiptBackUpFlow(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM receipt")
    fun getReceiptRowsNumber(): Flow<Int>
}