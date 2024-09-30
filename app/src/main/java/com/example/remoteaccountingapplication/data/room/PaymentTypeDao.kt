package com.example.remoteaccountingapplication.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentTypeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPaymentType(type: PaymentType)

    @Query("SELECT * FROM payment_type")
    fun getPaymentTypes(): Flow<List<PaymentType>>

    @Query("SELECT (SELECT COUNT(*) FROM payment_type WHERE `payment type` == :paymentType) != 0")
    fun ifPaymentTypeExist(paymentType: String): Flow<Boolean>

    @Query("SELECT `payment type` FROM payment_type")
    fun dailyPaymentTypeBackUp(): Flow<List<String>>

    @Query("DELETE FROM payment_type WHERE id=:itemId")
    suspend fun deletePaymentTypeById(itemId: Int)

    @Query("SELECT * FROM payment_type WHERE id = :itemId")
    fun getPaymentTypeById(itemId: Int): Flow<PaymentType>

    @Query("UPDATE payment_type SET `payment type` = :paymentType WHERE id = :itemId")
    suspend fun updatePaymentTypeById(itemId: Int, paymentType: String)

    @Query("SELECT COUNT(*) FROM payment_type")
    fun getPaymentTypesRowsNumber(): Flow<Int>
}