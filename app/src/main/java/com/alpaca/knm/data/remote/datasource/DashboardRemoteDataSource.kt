package com.alpaca.knm.data.remote.datasource

import com.alpaca.knm.data.remote.ErrorHandler
import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.DashboardApiService
import com.alpaca.knm.domain.model.ApiException
import com.alpaca.knm.domain.model.DashboardStats

class DashboardRemoteDataSource {
    
    private val apiService: DashboardApiService = 
        RetrofitClient.createService(DashboardApiService::class.java)
    
    suspend fun getStats(token: String): DashboardStats {
        try {
            val response = apiService.getStats("Bearer $token")
            
            if (response.isSuccessful) {
                val body = response.body() 
                    ?: throw ApiException.ServerException("Respuesta vacia del servidor")
                
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
