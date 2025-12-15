package com.alpaca.knm.data.remote.datasource

import com.alpaca.knm.data.remote.ErrorHandler
import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.AdvanceApiService
import com.alpaca.knm.data.remote.dto.AdvanceRequestDto
import com.alpaca.knm.domain.model.ApiException

class AdvanceRemoteDataSource {
    
    private val apiService: AdvanceApiService = 
        RetrofitClient.createService(AdvanceApiService::class.java)
    
    suspend fun createAdvanceRequest(
        token: String,
        estimatedKg: Double,
        requestedAmount: Double,
        referencePrice: Double
    ): String {
        try {
            val request = AdvanceRequestDto(estimatedKg, requestedAmount, referencePrice)
            val response = apiService.createAdvanceRequest("Bearer $token", request)
            
            if (response.isSuccessful) {
                val body = response.body() 
                    ?: throw ApiException.ServerException("Respuesta vacia del servidor")
                return body.message
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
