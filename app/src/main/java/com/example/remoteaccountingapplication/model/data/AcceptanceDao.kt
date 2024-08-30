package com.example.remoteaccountingapplication.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AcceptanceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAcceptProduct(acceptProduct: Acceptance)

    @Query("SELECT * FROM acceptance WHERE dateCalculation BETWEEN :startOfDay AND :endOfDay")
    fun getListOfAcceptancesByDate(startOfDay: Long, endOfDay: Long): Flow<List<Acceptance>>

    @Query("SELECT dateCalculation||','||date||','||name||','||product||','||price||','||number FROM acceptance")
    fun dailyAcceptancesBackUpFlow(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM acceptance")
    fun getAcceptanceRowsNumber(): Flow<Int>
}