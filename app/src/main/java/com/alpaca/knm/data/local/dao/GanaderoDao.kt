package com.alpaca.knm.data.local.dao

import androidx.room.*
import com.alpaca.knm.data.local.entity.GanaderoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GanaderoDao {
    
    @Query("SELECT * FROM ganaderos ORDER BY firstName ASC")
    fun getAllFlow(): Flow<List<GanaderoEntity>>
    
    @Query("SELECT * FROM ganaderos ORDER BY firstName ASC")
    suspend fun getAll(): List<GanaderoEntity>
    
    @Query("SELECT * FROM ganaderos WHERE id = :id")
    suspend fun getById(id: String): GanaderoEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ganadero: GanaderoEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ganaderos: List<GanaderoEntity>)
    
    @Update
    suspend fun update(ganadero: GanaderoEntity)
    
    @Query("DELETE FROM ganaderos WHERE id = :id")
    suspend fun deleteById(id: String)
    
    @Query("DELETE FROM ganaderos")
    suspend fun deleteAll()
    
    @Query("SELECT * FROM ganaderos WHERE firstName LIKE '%' || :query || '%' OR lastName LIKE '%' || :query || '%' OR dni LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<GanaderoEntity>>
}
