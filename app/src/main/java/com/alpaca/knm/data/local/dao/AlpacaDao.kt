package com.alpaca.knm.data.local.dao

import androidx.room.*
import com.alpaca.knm.data.local.entity.AlpacaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlpacaDao {
    
    @Query("SELECT * FROM alpacas ORDER BY nombre ASC")
    fun getAllFlow(): Flow<List<AlpacaEntity>>
    
    @Query("SELECT * FROM alpacas ORDER BY nombre ASC")
    suspend fun getAll(): List<AlpacaEntity>
    
    @Query("SELECT * FROM alpacas WHERE id = :id")
    suspend fun getById(id: Long): AlpacaEntity?
    
    @Query("SELECT * FROM alpacas WHERE ganaderoId = :ganaderoId ORDER BY nombre ASC")
    suspend fun getByGanaderoId(ganaderoId: Long): List<AlpacaEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alpaca: AlpacaEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(alpacas: List<AlpacaEntity>)
    
    @Update
    suspend fun update(alpaca: AlpacaEntity)
    
    @Query("DELETE FROM alpacas WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("DELETE FROM alpacas")
    suspend fun deleteAll()
    
    @Query("SELECT * FROM alpacas WHERE nombre LIKE '%' || :query || '%' OR color LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<AlpacaEntity>>
}
