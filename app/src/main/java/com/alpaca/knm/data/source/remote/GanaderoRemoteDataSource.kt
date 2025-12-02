package com.alpaca.knm.data.source.remote

import com.alpaca.knm.data.remote.ErrorHandler
import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.GanaderoApiService
import com.alpaca.knm.data.remote.dto.GanaderoRequest
import com.alpaca.knm.domain.model.ApiException
import com.alpaca.knm.domain.model.Ganadero

/**
 * Fuente de datos remota para Ganaderos
 */
class GanaderoRemoteDataSource {
    
    private val apiService: GanaderoApiService = 
        RetrofitClient.createService(GanaderoApiService::class.java)
    
    suspend fun getGanaderos(token: String): List<Ganadero> {
        try {
            val response = apiService.getGanaderos("Bearer $token")
            
            if (response.isSuccessful) {
                val body = response.body() 
                    ?: throw ApiException.ServerException("Respuesta vacía")
                
                return body.map { dto ->
                    Ganadero(
                        id = dto.id,
                        firstName = dto.firstName,
                        lastName = dto.lastName,
                        dni = dto.dni,
                        phone = dto.phone,
                        email = dto.email,
                        address = dto.address,
                        district = dto.district,
                        province = dto.province,
                        department = dto.department,
                        alpacasCount = dto.alpacasCount,
                        createdAt = dto.createdAt
                    )
                }
            } else {
                throw ErrorHandler.handleError(response)
            }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ErrorHandler.handleException(e)
        }
    }
    
    suspend fun getGanaderoById(token: String, id: String): Ganadero {
        try {
            val response = apiService.getGanaderoById("Bearer $token", id)
            
            if (response.isSuccessful) {
                val dto = response.body() 
                    ?: throw ApiException.ServerException("Respuesta vacía")
                
                return Ganadero(
                    id = dto.id,
                    firstName = dto.firstName,
                    lastName = dto.lastName,
                    dni = dto.dni,
                    phone = dto.phone,
                    email = dto.email,
                    address = dto.address,
                    district = dto.district,
                    province = dto.province,
                    department = dto.department,
                    alpacasCount = dto.alpacasCount,
                    createdAt = dto.createdAt
                )
            } else {
                throw ErrorHandler.handleError(response)
            }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ErrorHandler.handleException(e)
        }
    }
    
    suspend fun createGanadero(token: String, ganadero: Ganadero): Ganadero {
        try {
            val request = GanaderoRequest(
                firstName = ganadero.firstName,
                lastName = ganadero.lastName,
                dni = ganadero.dni,
                phone = ganadero.phone,
                email = ganadero.email,
                address = ganadero.address,
                district = ganadero.district,
                province = ganadero.province,
                department = ganadero.department
            )
            
            val response = apiService.createGanadero("Bearer $token", request)
            
            if (response.isSuccessful) {
                val dto = response.body() 
                    ?: throw ApiException.ServerException("Respuesta vacía")
                
                return Ganadero(
                    id = dto.id,
                    firstName = dto.firstName,
                    lastName = dto.lastName,
                    dni = dto.dni,
                    phone = dto.phone,
                    email = dto.email,
                    address = dto.address,
                    district = dto.district,
                    province = dto.province,
                    department = dto.department,
                    alpacasCount = dto.alpacasCount,
                    createdAt = dto.createdAt
                )
            } else {
                throw ErrorHandler.handleError(response)
            }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ErrorHandler.handleException(e)
        }
    }
    
    suspend fun updateGanadero(token: String, id: String, ganadero: Ganadero): Ganadero {
        try {
            val request = GanaderoRequest(
                firstName = ganadero.firstName,
                lastName = ganadero.lastName,
                dni = ganadero.dni,
                phone = ganadero.phone,
                email = ganadero.email,
                address = ganadero.address,
                district = ganadero.district,
                province = ganadero.province,
                department = ganadero.department
            )
            
            val response = apiService.updateGanadero("Bearer $token", id, request)
            
            if (response.isSuccessful) {
                val dto = response.body() 
                    ?: throw ApiException.ServerException("Respuesta vacía")
                
                return Ganadero(
                    id = dto.id,
                    firstName = dto.firstName,
                    lastName = dto.lastName,
                    dni = dto.dni,
                    phone = dto.phone,
                    email = dto.email,
                    address = dto.address,
                    district = dto.district,
                    province = dto.province,
                    department = dto.department,
                    alpacasCount = dto.alpacasCount,
                    createdAt = dto.createdAt
                )
            } else {
                throw ErrorHandler.handleError(response)
            }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ErrorHandler.handleException(e)
        }
    }
    
    suspend fun deleteGanadero(token: String, id: String) {
        try {
            val response = apiService.deleteGanadero("Bearer $token", id)
            
            if (!response.isSuccessful) {
                throw ErrorHandler.handleError(response)
            }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ErrorHandler.handleException(e)
        }
    }
}
