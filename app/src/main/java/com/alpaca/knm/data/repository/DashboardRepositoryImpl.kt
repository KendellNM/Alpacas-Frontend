package com.alpaca.knm.data.repository

import com.alpaca.knm.data.source.remote.DashboardRemoteDataSource
import com.alpaca.knm.domain.model.DashboardStats
import com.alpaca.knm.domain.repository.DashboardRepository
import kotlinx.coroutines.delay

/**
 * Implementación del repositorio de Dashboard
 */
class DashboardRepositoryImpl(
    private val remoteDataSource: DashboardRemoteDataSource
) : DashboardRepository {
    
    override suspend fun getDashboardStats(token: String): Result<DashboardStats> {
        return try {
            // Llamada real a la API
            val stats = remoteDataSource.getStats(token)
            
            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
