package com.example.remoteaccountingapplication.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Products)

    @Query("SELECT * FROM products ORDER BY product")
    fun getProducts(): Flow<List<Products>>

    @Query("SELECT * FROM products WHERE product == :product")
    fun getProductByProductName(product: String): Flow<Products>

    @Query("SELECT (SELECT COUNT(*) FROM products WHERE product == :product) != 0")
    fun ifProductExist(product: String): Flow<Boolean>

    @Query("SELECT product||','||price||','||remains||','||physical FROM products")
    fun dailyProductsBackUp(): Flow<List<String>>

    @Query("DELETE FROM products WHERE id = :itemId")
    suspend fun deleteProductById(itemId: Int)

    @Query("SELECT * FROM products WHERE id = :itemId")
    fun getProductById(itemId: Int): Flow<Products>

    @Query("UPDATE products SET product = :product, price = :price, remains = :remains, physical = :physical WHERE id = :itemId")
    suspend fun updateProductById(
        itemId: Int,
        product: String,
        price: Double,
        remains: Int,
        physical: Boolean
    )

    @Query("UPDATE products SET price = :price, remains = :remains, physical = :physical WHERE product = :product")
    suspend fun updateProductByProduct(
        product: String,
        price: Double,
        remains: Int,
        physical: Boolean
    )

    @Query("UPDATE products SET remains = :remains WHERE product = :product")
    suspend fun updateProductNumberByProduct(product: String, remains: Int)

    @Query("SELECT COUNT(*) FROM products")
    fun getProductsRowsNumber(): Flow<Int>

}