package com.alpaca.knm.data.source.remote

import com.alpaca.knm.data.remote.ErrorHandler
import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.AdvanceApiService
import com.alpaca.knm.data.remote.dto.AdvanceRequestDto
import com.alpaca.knm.domain.model.ApiException

/**
 * Fuente de datos remota para solicitudes de anticipo
 * Guarda las solicitudes en la base de datos con manejo de errores
 */
class AdvanceRemoteDataSource {
    
    private val apiService: AdvanceApiService = 
        RetrofitClient.createService(AdvanceApiService::class.java)
    
    /**
     * Crea una nueva solicitud de anticipo en la base de datos
     * Endpoint: POST /api/advances/request
     * @throws ApiException en caso de error
     */
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
                    ?: throw ApiException.ServerException("Respuesta vacía del servidor")
                
                // Retornar mensaje de éxito desde el backend
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
