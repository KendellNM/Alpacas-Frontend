package com.alpaca.knm.data.repository

import com.alpaca.knm.data.remote.datasource.DashboardRemoteDataSource
import com.alpaca.knm.domain.model.DashboardStats
import com.alpaca.knm.domain.repository.DashboardRepository

class DashboardRepositoryImpl(
    private val remoteDataSource: DashboardRemoteDataSource
) : DashboardRepository {
    
    override suspend fun getDashboardStats(token: String): Result<DashboardStats> {
        return try {
            val stats = remoteDataSource.getStats(token)
            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
