package com.alpaca.knm.data.remote.api

import com.alpaca.knm.data.remote.dto.AlpacaRegistroRequest
import com.alpaca.knm.data.remote.dto.AlpacaRegistroResponse
import com.alpaca.knm.data.remote.dto.RazaInfo
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service para registro simplificado de alpacas
 */
interface AlpacaRegistroApiService {
    
    @GET("alpacas/razas")
    suspend fun getRazas(): Response<List<RazaInfo>>
    
    @GET("alpacas/registros")
    suspend fun getRegistros(): Response<List<AlpacaRegistroResponse>>
    
    @GET("alpacas/mis-registros")
    suspend fun getMisRegistros(
        @Header("Authorization") token: String
    ): Response<List<AlpacaRegistroResponse>>
    
    @GET("alpacas/registros/{id}")
    suspend fun getRegistroById(
        @Path("id") id: Int
    ): Response<AlpacaRegistroResponse>
    
    @POST("alpacas/registro")
    suspend fun crearRegistro(
        @Body request: AlpacaRegistroRequest
    ): Response<AlpacaRegistroResponse>
    
    @PUT("alpacas/registro/{id}")
    suspend fun actualizarRegistro(
        @Path("id") id: Int,
        @Body request: AlpacaRegistroRequest
    ): Response<AlpacaRegistroResponse>
    
    @DELETE("alpacas/registro/{id}")
    suspend fun eliminarRegistro(
        @Path("id") id: Int
    ): Response<Unit>
}
