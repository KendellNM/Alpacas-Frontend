package com.alpaca.knm.data.sync

import android.content.Context
import android.util.Log
import com.alpaca.knm.data.local.AppDatabase
import com.alpaca.knm.data.local.entity.PendingSolicitudEntity
import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.SolicitudApiService
import com.alpaca.knm.data.remote.dto.SolicitudCreateRequest
import com.alpaca.knm.utils.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SolicitudSyncManager(context: Context) {
    
    private val database = AppDatabase.getDatabase(context)
    private val pendingDao = database.pendingSolicitudDao()
    private val networkMonitor = NetworkMonitor(context)
    private val apiService = RetrofitClient.createService(SolicitudApiService::class.java)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    companion object {
        private const val TAG = "SolicitudSyncManager"
        private const val MAX_RETRIES = 3
        
        @Volatile
        private var INSTANCE: SolicitudSyncManager? = null
        
        fun getInstance(context: Context): SolicitudSyncManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SolicitudSyncManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
    
    init {
        observeNetworkAndSync()
    }
    
    private fun observeNetworkAndSync() {
        scope.launch {
            networkMonitor.isOnline.collect { isOnline ->
                if (isOnline) {
                    Log.d(TAG, "Conexion restaurada, sincronizando...")
                    syncPendingSolicitudes()
                }
            }
        }
    }
    
    suspend fun savePendingSolicitud(
        ganaderoId: Long,
        kilograms: Double,
        totalAmount: Double
    ): Long {
        val pending = PendingSolicitudEntity(
            ganaderoId = ganaderoId,
            kilograms = kilograms,
            totalAmount = totalAmount
        )
        val localId = pendingDao.insert(pending)
        Log.d(TAG, "Solicitud guardada localmente: $localId")
        
        if (networkMonitor.isCurrentlyOnline()) {
            syncPendingSolicitudes()
        }
        
        return localId
    }
    
    suspend fun syncPendingSolicitudes(): SyncResult {
        val pending = pendingDao.getAll()
        if (pending.isEmpty()) {
            return SyncResult(0, 0, emptyList())
        }
        
        Log.d(TAG, "Sincronizando ${pending.size} solicitudes pendientes")
        
        var synced = 0
        var failed = 0
        val errors = mutableListOf<String>()
        
        for (solicitud in pending) {
            if (solicitud.retryCount >= MAX_RETRIES) {
                Log.w(TAG, "Solicitud ${solicitud.localId} excedio reintentos")
                errors.add("Solicitud local #${solicitud.localId}: maximo de reintentos")
                continue
            }
            
            try {
                val request = SolicitudCreateRequest(
                    ganaderoId = solicitud.ganaderoId,
                    kilograms = solicitud.kilograms,
                    totalAmount = solicitud.totalAmount
                )
                
                val response = apiService.createSolicitud(request)
                
                if (response.isSuccessful) {
                    pendingDao.deleteById(solicitud.localId)
                    synced++
                    Log.d(TAG, "Solicitud ${solicitud.localId} sincronizada")
                } else {
                    val error = response.errorBody()?.string() ?: "Error desconocido"
                    pendingDao.incrementRetry(solicitud.localId, error)
                    failed++
                    errors.add("Error servidor: $error")
                }
            } catch (e: Exception) {
                pendingDao.incrementRetry(solicitud.localId, e.message ?: "Error de red")
                failed++
                errors.add(e.message ?: "Error de conexion")
                Log.e(TAG, "Error sincronizando: ${e.message}")
            }
        }
        
        return SyncResult(synced, failed, errors)
    }
    
    fun getPendingCount(): Flow<Int> = pendingDao.getPendingCountFlow()
    
    fun getPendingSolicitudes(): Flow<List<PendingSolicitudEntity>> = pendingDao.getAllFlow()
    
    suspend fun deletePending(localId: Long) {
        pendingDao.deleteById(localId)
    }
    
    fun isOnline(): Boolean = networkMonitor.isCurrentlyOnline()
    
    fun observeNetwork(): Flow<Boolean> = networkMonitor.isOnline
}

data class SyncResult(
    val synced: Int,
    val failed: Int,
    val errors: List<String>
)
