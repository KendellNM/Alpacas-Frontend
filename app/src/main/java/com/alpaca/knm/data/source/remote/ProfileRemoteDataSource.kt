package com.alpaca.knm.data.source.remote

import com.alpaca.knm.data.remote.ErrorHandler
import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.ProfileApiService
import com.alpaca.knm.domain.model.ApiException
import com.alpaca.knm.domain.model.UserProfile

/**
 * Fuente de datos remota para perfil de usuario
 * Obtiene datos del perfil desde la base de datos con manejo de errores
 */
class ProfileRemoteDataSource {
    
    private val apiService: ProfileApiService = 
        RetrofitClient.createService(ProfileApiService::class.java)
    
    /**
     * Obtiene el perfil del usuario desde el backend
     * Endpoint: GET /api/profile
     * @throws ApiException en caso de error
     */
    suspend fun getUserProfile(token: String): UserProfile {
        try {
            val response = apiService.getUserProfile("Bearer $token")
            
            if (response.isSuccessful) {
                val body = response.body() 
                    ?: throw ApiException.ServerException("Respuesta vacía del servidor")
                
                return UserProfile(
                    id = body.id,
                    firstName = body.firstName,
                    lastName = body.lastName,
                    gender = body.gender,
                    location = body.location,
                    avatarUrl = body.avatarUrl
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
