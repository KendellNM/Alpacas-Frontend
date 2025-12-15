package com.alpaca.knm.data.remote.datasource

import com.alpaca.knm.data.remote.ErrorHandler
import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.AuthApiService
import com.alpaca.knm.data.remote.dto.LoginRequest
import com.alpaca.knm.domain.model.ApiException
import com.alpaca.knm.domain.model.User

class AuthRemoteDataSource {
    
    private val apiService: AuthApiService = 
        RetrofitClient.createService(AuthApiService::class.java)
    
    suspend fun login(username: String, password: String): User {
        try {
            val request = LoginRequest(username, password)
            val response = apiService.login(request)
            
            if (response.isSuccessful) {
                val body = response.body() 
                    ?: throw ApiException.ServerException("Respuesta vacia del servidor")
                
                if (body.token.isBlank()) {
                    throw ApiException.ServerException("Token no recibido del servidor")
                }
                
                return User(
                    id = body.id.toString(),
                    username = body.username,
                    email = "",
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
