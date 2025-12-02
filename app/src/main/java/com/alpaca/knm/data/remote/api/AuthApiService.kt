package com.alpaca.knm.data.remote.api

import com.alpaca.knm.data.remote.dto.LoginRequest
import com.alpaca.knm.data.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * API Service para autenticación
 * Base URL: http://10.0.2.2:8080/api/
 */
interface AuthApiService {
    
    /**
     * Login de usuario
     * POST /api/auth/login
     */
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
    
    /**
     * Logout de usuario
     * POST /api/auth/logout
     */
    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<Unit>
}
