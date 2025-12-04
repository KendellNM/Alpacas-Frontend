package com.alpaca.knm.data.repository

import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.AlpacaRegistroApiService
import com.alpaca.knm.data.remote.dto.AlpacaRegistroRequest
import com.alpaca.knm.domain.model.AlpacaRegistro
import com.alpaca.knm.data.remote.dto.RazaInfo
import com.alpaca.knm.domain.repository.AlpacaRegistroRepository

/**
 * Implementación del repositorio de registro de alpacas
 */
class AlpacaRegistroRepositoryImpl : AlpacaRegistroRepository {
    
    private val apiService: AlpacaRegistroApiService = 
        RetrofitClient.createService(AlpacaRegistroApiService::class.java)
    
    override suspend fun getRazas(): Result<List<RazaInfo>> {
        return try {
            val response = apiService.getRazas()
            if (response.isSuccessful) {
                val razas = response.body()?.map { 
                    RazaInfo(nombre = it.nombre, descripcion = it.descripcion) 
                } ?: emptyList()
                Result.success(razas)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getRegistros(): Result<List<AlpacaRegistro>> {
        return try {
            val response = apiService.getRegistros()
            if (response.isSuccessful) {
                val registros = response.body()?.map { it.toDomain() } ?: emptyList()
                Result.success(registros)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getMisRegistros(token: String): Result<List<AlpacaRegistro>> {
        return try {
            val response = apiService.getMisRegistros("Bearer $token")
            if (response.isSuccessful) {
                val registros = response.body()?.map { it.toDomain() } ?: emptyList()
                Result.success(registros)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRegistroById(id: Int): Result<AlpacaRegistro> {
        return try {
            val response = apiService.getRegistroById(id)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Registro no encontrado"))
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun crearRegistro(
        ganaderoId: Int, 
        raza: String, 
        cantidad: Int, 
        adultos: Int
    ): Result<AlpacaRegistro> {
        return try {
            val request = AlpacaRegistroRequest(ganaderoId, raza, cantidad, adultos)
            val response = apiService.crearRegistro(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Error al crear registro"))
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun actualizarRegistro(
        id: Int,
        ganaderoId: Int, 
        raza: String, 
        cantidad: Int, 
        adultos: Int
    ): Result<AlpacaRegistro> {
        return try {
            val request = AlpacaRegistroRequest(ganaderoId, raza, cantidad, adultos)
            val response = apiService.actualizarRegistro(id, request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Error al actualizar registro"))
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun eliminarRegistro(id: Int): Result<Boolean> {
        return try {
            val response = apiService.eliminarRegistro(id)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun com.alpaca.knm.data.remote.dto.AlpacaRegistroResponse.toDomain() = AlpacaRegistro(
        id = id,
        ganaderoId = ganaderoId,
        raza = try { com.alpaca.knm.domain.model.AlpacaRaza.valueOf(raza.uppercase()) } 
               catch (e: Exception) { com.alpaca.knm.domain.model.AlpacaRaza.HUACAYA },
        cantidad = cantidad,
        adultos = adultos,
        crias = crias,
        fechaRegistro = fechaRegistro
    )
}
