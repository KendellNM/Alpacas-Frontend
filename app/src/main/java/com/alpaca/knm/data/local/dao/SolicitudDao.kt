package com.alpaca.knm.data.local.dao

import androidx.room.*
import com.alpaca.knm.data.local.entity.SolicitudEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de Solicitud en Room
 */
@Dao
interface SolicitudDao {
    
    @Query("SELECT * FROM solicitudes ORDER BY createdAt DESC")
    fun getAllFlow(): Flow<List<SolicitudEntity>>
    
    @Query("SELECT * FROM solicitudes ORDER BY createdAt DESC")
    suspend fun getAll(): List<SolicitudEntity>
    
    @Query("SELECT * FROM solicitudes WHERE id = :id")
    suspend fun getById(id: String): SolicitudEntity?
    
    @Query("SELECT * FROM solicitudes WHERE ganaderoId = :ganaderoId ORDER BY createdAt DESC")
    fun getByGanaderoId(ganaderoId: String): Flow<List<SolicitudEntity>>
    
    @Query("SELECT * FROM solicitudes WHERE status = :status ORDER BY createdAt DESC")
    fun getByStatus(status: String): Flow<List<SolicitudEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(solicitud: SolicitudEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(solicitudes: List<SolicitudEntity>)
    
    @Update
    suspend fun update(solicitud: SolicitudEntity)
    
    @Query("DELETE FROM solicitudes WHERE id = :id")
    suspend fun deleteById(id: String)
    
    @Query("DELETE FROM solicitudes")
    suspend fun deleteAll()
}
