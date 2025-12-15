package com.alpaca.knm.data.remote.api

import com.alpaca.knm.data.remote.dto.LoginRequest
import com.alpaca.knm.data.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
    
    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<Unit>
}
