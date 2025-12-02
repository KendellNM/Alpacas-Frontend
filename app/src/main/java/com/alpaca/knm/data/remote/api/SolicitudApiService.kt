package com.alpaca.knm.data.remote.api

import com.alpaca.knm.data.remote.dto.SolicitudActionRequest
import com.alpaca.knm.data.remote.dto.SolicitudDto
import retrofit2.Response
import retrofit2.http.*

interface SolicitudApiService {
    
    @GET("advances")
    suspend fun getAllSolicitudes(): Response<List<SolicitudDto>>
    
    @GET("advances")
    suspend fun getSolicitudesByStatus(
        @Query("status") status: String
    ): Response<List<SolicitudDto>>
    
    @GET("advances/{id}")
    suspend fun getSolicitudById(
        @Path("id") id: Long
    ): Response<SolicitudDto>
    
    @POST("advances/{id}/approve")
    suspend fun approveSolicitud(
        @Path("id") id: Long,
        @Body request: SolicitudActionRequest
    ): Response<SolicitudDto>
    
    @POST("advances/{id}/reject")
    suspend fun rejectSolicitud(
        @Path("id") id: Long,
        @Body request: SolicitudActionRequest
    ): Response<SolicitudDto>
}
