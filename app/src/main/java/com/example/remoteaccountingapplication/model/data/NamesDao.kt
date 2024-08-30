package com.example.remoteaccountingapplication.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NamesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertName(name: Names)

    @Query("SELECT * FROM names")
    fun getNames(): Flow<List<Names>>

    @Query("SELECT (SELECT COUNT(*) FROM names WHERE name == :name) != 0")
    fun ifNameExist(name: String): Flow<Boolean>

    @Query("SELECT name FROM names")
    fun dailyNamesBackUp(): Flow<List<String>>

    @Query("DELETE FROM names WHERE id=:itemId")
    suspend fun deleteNameById(itemId: Int)

    @Query("SELECT * FROM names WHERE id = :itemId")
    fun getNameById(itemId: Int): Flow<Names>

    @Query("UPDATE names SET name = :name WHERE id = :itemId")
    suspend fun updateNameById(itemId: Int, name: String)

    @Query("SELECT COUNT(*) FROM names")
    fun getNamesRowsNumber(): Flow<Int>
}