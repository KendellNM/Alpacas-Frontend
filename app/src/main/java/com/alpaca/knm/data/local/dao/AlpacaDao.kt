package com.alpaca.knm.data.local.dao

import androidx.room.*
import com.alpaca.knm.data.local.entity.AlpacaEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de Alpaca en Room
 */
@Dao
interface AlpacaDao {
    
    @Query("SELECT * FROM alpacas ORDER BY name ASC")
    fun getAllFlow(): Flow<List<AlpacaEntity>>
    
    @Query("SELECT * FROM alpacas ORDER BY name ASC")
    suspend fun getAll(): List<AlpacaEntity>
    
    @Query("SELECT * FROM alpacas WHERE id = :id")
    suspend fun getById(id: String): AlpacaEntity?
    
    @Query("SELECT * FROM alpacas WHERE ganaderoId = :ganaderoId")
    fun getByGanaderoId(ganaderoId: String): Flow<List<AlpacaEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alpaca: AlpacaEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(alpacas: List<AlpacaEntity>)
    
    @Update
    suspend fun update(alpaca: AlpacaEntity)
    
    @Query("DELETE FROM alpacas WHERE id = :id")
    suspend fun deleteById(id: String)
    
    @Query("DELETE FROM alpacas")
    suspend fun deleteAll()
}
