package com.example.remoteaccountingapplication.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleTypeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSaleType(type: SaleType)

    @Query("SELECT * FROM sale_type")
    fun getSaleTypes(): Flow<List<SaleType>>

    @Query("SELECT (SELECT COUNT(*) FROM sale_type WHERE `sale type` == :saleType) != 0")
    fun ifSaleTypeExist(saleType: String): Flow<Boolean>

    @Query("SELECT `sale type` FROM sale_type")
    fun dailySaleTypeBackUp(): Flow<List<String>>

    @Query("DELETE FROM sale_type WHERE id=:itemId")
    suspend fun deleteSaleTypeById(itemId: Int)

    @Query("SELECT * FROM sale_type WHERE id = :itemId")
    fun getSaleTypeById(itemId: Int): Flow<SaleType>

    @Query("UPDATE sale_type SET `sale type` = :saleType WHERE id = :itemId")
    suspend fun updateSaleTypeById(itemId: Int, saleType: String)

    @Query("SELECT COUNT(*) FROM sale_type")
    fun getSaleTypesRowsNumber(): Flow<Int>
}