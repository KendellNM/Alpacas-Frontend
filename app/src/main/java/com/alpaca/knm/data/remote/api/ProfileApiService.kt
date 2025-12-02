package com.alpaca.knm.data.remote.api

import com.alpaca.knm.data.remote.dto.UserProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * API Service para perfil de usuario
 */
interface ProfileApiService {
    
    @GET("profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<UserProfileResponse>
}
