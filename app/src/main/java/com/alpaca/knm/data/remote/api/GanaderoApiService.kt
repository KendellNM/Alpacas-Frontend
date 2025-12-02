package com.alpaca.knm.data.remote.api

import com.alpaca.knm.data.remote.dto.GanaderoRequest
import com.alpaca.knm.data.remote.dto.GanaderoResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service para CRUD de Ganaderos
 */
interface GanaderoApiService {
    
    /**
     * Listar todos los ganaderos
     * GET /api/ganaderos
     */
    @GET("ganaderos")
    suspend fun getGanaderos(
        @Header("Authorization") token: String
    ): Response<List<GanaderoResponse>>
    
    /**
     * Obtener un ganadero por ID
     * GET /api/ganaderos/{id}
     */
    @GET("ganaderos/{id}")
    suspend fun getGanaderoById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<GanaderoResponse>
    
    /**
     * Crear nuevo ganadero
     * POST /api/ganaderos
     */
    @POST("ganaderos")
    suspend fun createGanadero(
        @Header("Authorization") token: String,
        @Body request: GanaderoRequest
    ): Response<GanaderoResponse>
    
    /**
     * Actualizar ganadero
     * PUT /api/ganaderos/{id}
     */
    @PUT("ganaderos/{id}")
    suspend fun updateGanadero(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: GanaderoRequest
    ): Response<GanaderoResponse>
    
    /**
     * Eliminar ganadero
     * DELETE /api/ganaderos/{id}
     */
    @DELETE("ganaderos/{id}")
    suspend fun deleteGanadero(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>
}
