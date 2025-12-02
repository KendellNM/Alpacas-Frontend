package com.alpaca.knm.data.remote.api

import com.alpaca.knm.data.remote.dto.DashboardStatsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * API Service para Dashboard
 */
interface DashboardApiService {
    
    @GET("dashboard/stats")
    suspend fun getStats(
        @Header("Authorization") token: String
    ): Response<DashboardStatsResponse>
}
