package com.alpaca.knm.data.remote.datasource

import com.alpaca.knm.data.remote.api.SolicitudApiService
import com.alpaca.knm.data.remote.dto.SolicitudActionRequest
import com.alpaca.knm.data.remote.dto.SolicitudDto
import com.alpaca.knm.data.remote.ErrorHandler

class SolicitudRemoteDataSource(
    private val apiService: SolicitudApiService
) {
    suspend fun getAllSolicitudes(): Result<List<SolicitudDto>> {
        return try {
            val response = apiService.getAllSolicitudes()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ErrorHandler.handleError(response))
            }
        } catch (e: Exception) {
            Result.failure(ErrorHandler.handleException(e))
        }
    }
    
    suspend fun getSolicitudesByStatus(status: String): Result<List<SolicitudDto>> {
        return try {
            val response = apiService.getSolicitudesByStatus(status)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ErrorHandler.handleError(response))
            }
        } catch (e: Exception) {
            Result.failure(ErrorHandler.handleException(e))
        }
    }
    
    suspend fun getSolicitudById(id: Long): Result<SolicitudDto> {
        return try {
            val response = apiService.getSolicitudById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ErrorHandler.handleError(response))
            }
        } catch (e: Exception) {
            Result.failure(ErrorHandler.handleException(e))
        }
    }
    
    suspend fun approveSolicitud(id: Long, comment: String?): Result<SolicitudDto> {
        return try {
            val request = SolicitudActionRequest(comment)
            val response = apiService.approveSolicitud(id, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ErrorHandler.handleError(response))
            }
        } catch (e: Exception) {
            Result.failure(ErrorHandler.handleException(e))
        }
    }
    
    suspend fun rejectSolicitud(id: Long, comment: String): Result<SolicitudDto> {
        return try {
            val request = SolicitudActionRequest(comment)
            val response = apiService.rejectSolicitud(id, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ErrorHandler.handleError(response))
            }
        } catch (e: Exception) {
            Result.failure(ErrorHandler.handleException(e))
        }
    }
}
