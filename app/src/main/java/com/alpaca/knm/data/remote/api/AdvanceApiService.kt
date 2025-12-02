package com.alpaca.knm.data.remote.api

import com.alpaca.knm.data.remote.dto.AdvanceRequestDto
import com.alpaca.knm.data.remote.dto.AdvanceRequestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * API Service para solicitudes de anticipo
 */
interface AdvanceApiService {
    
    @POST("advances/request")
    suspend fun createAdvanceRequest(
        @Header("Authorization") token: String,
        @Body request: AdvanceRequestDto
    ): Response<AdvanceRequestResponse>
}
