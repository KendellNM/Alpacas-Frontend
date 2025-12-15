package com.alpaca.knm.data.remote.api

import com.alpaca.knm.data.remote.dto.GanaderoRequest
import com.alpaca.knm.data.remote.dto.GanaderoResponse
import retrofit2.Response
import retrofit2.http.*

interface GanaderoApiService {
    
    @GET("ganaderos")
    suspend fun getGanaderos(
        @Header("Authorization") token: String
    ): Response<List<GanaderoResponse>>
    
    @GET("ganaderos/{id}")
    suspend fun getGanaderoById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<GanaderoResponse>
    
    @POST("ganaderos")
    suspend fun createGanadero(
        @Header("Authorization") token: String,
        @Body request: GanaderoRequest
    ): Response<GanaderoResponse>
    
    @PUT("ganaderos/{id}")
    suspend fun updateGanadero(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: GanaderoRequest
    ): Response<GanaderoResponse>
    
    @DELETE("ganaderos/{id}")
    suspend fun deleteGanadero(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>
}
