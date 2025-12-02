package com.alpaca.knm.data.source.remote

import com.alpaca.knm.data.remote.ErrorHandler
import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.DashboardApiService
import com.alpaca.knm.domain.model.ApiException
import com.alpaca.knm.domain.model.DashboardStats

/**
 * Fuente de datos remota para Dashboard
 * Obtiene estadísticas desde la base de datos con manejo de errores
 */
class DashboardRemoteDataSource {
    
    private val apiService: DashboardApiService = 
        RetrofitClient.createService(DashboardApiService::class.java)
    
    /**
     * Obtiene estadísticas del dashboard desde el backend
     * Endpoint: GET /api/dashboard/stats
     * @throws ApiException en caso de error
     */
    suspend fun getStats(token: String): DashboardStats {
        try {
            val response = apiService.getStats("Bearer $token")
            
            if (response.isSuccessful) {
                val body = response.body() 
                    ?: throw ApiException.ServerException("Respuesta vacía del servidor")
                
                return DashboardStats(
                    alpacasCount = body.alpacasCount,
                    pendingRequests = body.pendingRequests,
                    totalAdvances = body.totalAdvances
                )
            } else {
                throw ErrorHandler.handleError(response)
            }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ErrorHandler.handleException(e)
        }
    }
}
