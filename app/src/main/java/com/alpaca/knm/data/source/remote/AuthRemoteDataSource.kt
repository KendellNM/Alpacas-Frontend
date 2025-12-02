package com.alpaca.knm.data.source.remote

import com.alpaca.knm.data.remote.ErrorHandler
import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.AuthApiService
import com.alpaca.knm.data.remote.dto.LoginRequest
import com.alpaca.knm.domain.model.ApiException
import com.alpaca.knm.domain.model.User

/**
 * Fuente de datos remota para autenticación
 * Maneja las llamadas a la API del backend con manejo de errores robusto
 */
class AuthRemoteDataSource {
    
    private val apiService: AuthApiService = 
        RetrofitClient.createService(AuthApiService::class.java)
    
    /**
     * Realiza login contra el backend
     * Endpoint: POST /api/auth/login
     * @throws ApiException en caso de error
     */
    suspend fun login(username: String, password: String): User {
        try {
            val request = LoginRequest(username, password)
            val response = apiService.login(request)
            
            if (response.isSuccessful) {
                val body = response.body() 
                    ?: throw ApiException.ServerException("Respuesta vacía del servidor")
                
                // Validar que el token no esté vacío
                if (body.token.isNullOrBlank()) {
                    throw ApiException.ServerException("Token no recibido del servidor")
                }
                
                return User(
                    id = body.id,
                    username = body.username,
                    email = body.email ?: "",
                    token = body.token,
                    role = body.role
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
    
    /**
     * Cierra sesión en el backend
     * Endpoint: POST /api/auth/logout
     * @throws ApiException en caso de error
     */
    suspend fun logout(token: String) {
        try {
            val response = apiService.logout("Bearer $token")
            if (!response.isSuccessful) {
                throw ErrorHandler.handleError(response)
            }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ErrorHandler.handleException(e)
        }
    }
}
