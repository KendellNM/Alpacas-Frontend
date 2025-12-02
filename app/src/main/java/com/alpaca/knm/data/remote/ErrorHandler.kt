package com.alpaca.knm.data.remote

import com.alpaca.knm.data.remote.dto.ErrorResponse
import com.alpaca.knm.domain.model.ApiException
import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Manejador centralizado de errores de API
 */
object ErrorHandler {
    
    private val gson = Gson()
    
    /**
     * Convierte una respuesta HTTP en una excepción apropiada
     */
    fun <T> handleError(response: Response<T>): ApiException {
        val code = response.code()
        val errorBody = response.errorBody()?.string()
        
        // Intentar parsear el error del backend
        val errorResponse = try {
            errorBody?.let { gson.fromJson(it, ErrorResponse::class.java) }
        } catch (e: Exception) {
            null
        }
        
        val message = errorResponse?.message ?: getDefaultMessage(code)
        
        return when (code) {
            400 -> {
                val fieldErrors = errorResponse?.errors?.associate { 
                    it.field to it.message 
                }
                ApiException.BadRequestException(message, fieldErrors)
            }
            401 -> ApiException.UnauthorizedException(message)
            403 -> ApiException.ForbiddenException(message)
            404 -> ApiException.NotFoundException(message)
            409 -> ApiException.ConflictException(message)
            in 500..599 -> ApiException.ServerException(message)
            else -> ApiException.UnknownException(message)
        }
    }
    
    /**
     * Convierte una excepción de red en ApiException
     */
    fun handleException(exception: Throwable): ApiException {
        return when (exception) {
            is ApiException -> exception
            is UnknownHostException -> ApiException.NetworkException(
                "Sin conexión a internet. Verifica tu red"
            )
            is SocketTimeoutException -> ApiException.NetworkException(
                "Tiempo de espera agotado. Intenta nuevamente"
            )
            is IOException -> ApiException.NetworkException(
                "Error de conexión. Verifica tu internet"
            )
            else -> ApiException.UnknownException(
                exception.message ?: "Error desconocido"
            )
        }
    }
    
    /**
     * Mensajes por defecto según código HTTP
     */
    private fun getDefaultMessage(code: Int): String {
        return when (code) {
            400 -> "Datos inválidos. Verifica la información ingresada"
            401 -> "Credenciales inválidas o sesión expirada"
            403 -> "No tienes permisos para realizar esta acción"
            404 -> "Recurso no encontrado"
            409 -> "Ya existe un recurso con estos datos"
            500 -> "Error interno del servidor"
            502 -> "Servidor no disponible"
            503 -> "Servicio temporalmente no disponible"
            else -> "Error del servidor (código: $code)"
        }
    }
}
