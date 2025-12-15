package com.alpaca.knm.data.local.dao

import androidx.room.*
import com.alpaca.knm.data.local.entity.PendingSolicitudEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingSolicitudDao {
    
    @Query("SELECT * FROM pending_solicitudes ORDER BY createdAt ASC")
    fun getAllFlow(): Flow<List<PendingSolicitudEntity>>
    
    @Query("SELECT * FROM pending_solicitudes ORDER BY createdAt ASC")
    suspend fun getAll(): List<PendingSolicitudEntity>
    
    @Query("SELECT COUNT(*) FROM pending_solicitudes")
    fun getPendingCountFlow(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM pending_solicitudes")
    suspend fun getPendingCount(): Int
    
    @Insert
    suspend fun insert(solicitud: PendingSolicitudEntity): Long
    
    @Update
    suspend fun update(solicitud: PendingSolicitudEntity)
    
    @Query("DELETE FROM pending_solicitudes WHERE localId = :localId")
    suspend fun deleteById(localId: Long)
    
    @Query("DELETE FROM pending_solicitudes")
    suspend fun deleteAll()
    
    @Query("UPDATE pending_solicitudes SET retryCount = retryCount + 1, lastError = :error WHERE localId = :localId")
    suspend fun incrementRetry(localId: Long, error: String)
}
