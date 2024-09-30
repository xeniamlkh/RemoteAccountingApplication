package com.example.remoteaccountingapplication.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SalesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSale(sale: Sales)

    @Query("SELECT * FROM sales WHERE dateCalculation BETWEEN :startOfDay AND :endOfDay")
    fun getListOfSalesByDate(startOfDay: Long, endOfDay: Long): Flow<List<Sales>>

    @Query("SELECT SUM(total) FROM sales WHERE dateCalculation BETWEEN :startOfDay AND :endOfDay")
    fun getTotalSum(startOfDay: Long, endOfDay: Long): Flow<Double>

    @Query("SELECT date||','||product||','||price||','||number||','||total||','||`payment type`||','||`sale type`||','||name||','||comment FROM sales WHERE dateCalculation >= :startDate ORDER BY dateCalculation")
    fun exportMonthSalesCsv(startDate: Long): Flow<List<String>>

    @Query("SELECT date||','||product||','||price||','||number||','||total||','||`payment type`||','||`sale type`||','||name||','||comment FROM sales WHERE dateCalculation BETWEEN :startDate AND :endDate ORDER BY dateCalculation")
    fun exportRangeSalesCsv(startDate: Long, endDate: Long): Flow<List<String>>

    @Query("SELECT dateCalculation||','||date||','||product||','||price||','||number||','||`payment type`||','||`sale type`||','||name||','||comment||','||total FROM sales")
    fun dailyAllSalesBackUp(): Flow<List<String>>

    @Query("DELETE FROM sales WHERE id = :itemId")
    suspend fun deleteSaleItemById(itemId: Int)

    @Query("SELECT * FROM sales WHERE id = :itemId")
    fun getSaleItemById(itemId: Int): Flow<Sales>

    @Query("UPDATE sales SET product = :product, price = :price, number = :number, `payment type` = :paymentType, `sale type` = :saleType, name = :name, comment = :comment, total = :total WHERE id = :itemId")
    suspend fun updateSaleItemById(itemId: Int, product: String, price: Double, number: Int, paymentType: String, saleType: String, name: String, comment: String, total: Double)

    @Query("SELECT COUNT(*) FROM sales")
    fun getSalesRowsNumber(): Flow<Int>

    @Query("SELECT name FROM sales WHERE id = (SELECT MAX(id) FROM sales)")
    fun getNameByLastId(): Flow<String>
}