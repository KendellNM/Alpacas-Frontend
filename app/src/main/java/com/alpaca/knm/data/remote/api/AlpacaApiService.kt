package com.alpaca.knm.data.remote.api

import com.alpaca.knm.data.remote.dto.AlpacaCreateRequest
import com.alpaca.knm.data.remote.dto.AlpacaDto
import retrofit2.Response
import retrofit2.http.*

interface AlpacaApiService {
    
    @GET("alpacas")
    suspend fun getAllAlpacas(): Response<List<AlpacaDto>>
    
    @GET("alpacas/ganadero/{ganaderoId}")
    suspend fun getAlpacasByGanadero(@Path("ganaderoId") ganaderoId: Long): Response<List<AlpacaDto>>
    
    @GET("alpacas/{id}")
    suspend fun getAlpacaById(@Path("id") id: Long): Response<AlpacaDto>
    
    @POST("alpacas")
    suspend fun createAlpaca(@Body request: AlpacaCreateRequest): Response<AlpacaDto>
    
    @PUT("alpacas/{id}")
    suspend fun updateAlpaca(@Path("id") id: Long, @Body request: AlpacaCreateRequest): Response<AlpacaDto>
    
    @DELETE("alpacas/{id}")
    suspend fun deleteAlpaca(@Path("id") id: Long): Response<Unit>
}
