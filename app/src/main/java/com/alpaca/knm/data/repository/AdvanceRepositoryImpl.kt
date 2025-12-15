package com.alpaca.knm.data.repository

import com.alpaca.knm.data.remote.datasource.AdvanceRemoteDataSource
import com.alpaca.knm.domain.model.AdvanceRequest
import com.alpaca.knm.domain.repository.AdvanceRepository

class AdvanceRepositoryImpl(
    private val remoteDataSource: AdvanceRemoteDataSource
) : AdvanceRepository {
    
    override suspend fun createAdvanceRequest(token: String, request: AdvanceRequest): Result<String> {
        return try {
            val message = remoteDataSource.createAdvanceRequest(
                token = token,
                estimatedKg = request.estimatedKg,
                requestedAmount = request.requestedAmount,
                referencePrice = request.referencePrice
            )
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
