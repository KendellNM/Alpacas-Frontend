package com.alpaca.knm.data.remote.datasource

import com.alpaca.knm.data.remote.api.AlpacaApiService
import com.alpaca.knm.data.remote.dto.AlpacaCreateRequest
import com.alpaca.knm.data.remote.dto.AlpacaDto
import com.alpaca.knm.data.remote.ErrorHandler

class AlpacaRemoteDataSource(
    private val apiService: AlpacaApiService
) {
    suspend fun getAllAlpacas(): Result<List<AlpacaDto>> {
        return try {
            val response = apiService.getAllAlpacas()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ErrorHandler.handleError(response))
            }
        } catch (e: Exception) {
            Result.failure(ErrorHandler.handleException(e))
        }
    }
    
    suspend fun getAlpacasByGanadero(ganaderoId: Long): Result<List<AlpacaDto>> {
        return try {
            val response = apiService.getAlpacasByGanadero(ganaderoId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ErrorHandler.handleError(response))
            }
        } catch (e: Exception) {
            Result.failure(ErrorHandler.handleException(e))
        }
    }
    
    suspend fun getAlpacaById(id: Long): Result<AlpacaDto> {
        return try {
            val response = apiService.getAlpacaById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ErrorHandler.handleError(response))
            }
        } catch (e: Exception) {
            Result.failure(ErrorHandler.handleException(e))
        }
    }
    
    suspend fun createAlpaca(request: AlpacaCreateRequest): Result<AlpacaDto> {
        return try {
            val response = apiService.createAlpaca(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ErrorHandler.handleError(response))
            }
        } catch (e: Exception) {
            Result.failure(ErrorHandler.handleException(e))
        }
    }
    
    suspend fun updateAlpaca(id: Long, request: AlpacaCreateRequest): Result<AlpacaDto> {
        return try {
            val response = apiService.updateAlpaca(id, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ErrorHandler.handleError(response))
            }
        } catch (e: Exception) {
            Result.failure(ErrorHandler.handleException(e))
        }
    }
    
    suspend fun deleteAlpaca(id: Long): Result<Unit> {
        return try {
            val response = apiService.deleteAlpaca(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(ErrorHandler.handleError(response))
            }
        } catch (e: Exception) {
            Result.failure(ErrorHandler.handleException(e))
        }
    }
}
