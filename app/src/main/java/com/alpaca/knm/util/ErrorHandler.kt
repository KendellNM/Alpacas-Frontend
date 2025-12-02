package com.alpaca.knm.util

import retrofit2.Response
import java.io.IOException

/**
 * Manejador centralizado de errores
 */
object ErrorHandler {
    
    fun <T> handleError(response: Response<T>): Exception {
        return when (response.code()) {
            400 -> Exception("Solicitud inválida")
            401 -> Exception("No autorizado. Por favor, inicie sesión nuevamente")
            403 -> Exception("Acceso denegado")
            404 -> Exception("Recurso no encontrado")
            500 -> Exception("Error del servidor. Intente nuevamente más tarde")
            else -> Exception("Error: ${response.message()}")
        }
    }
    
    fun handleException(e: Exception): Exception {
        return when (e) {
            is IOException -> Exception("Error de conexión. Verifique su conexión a internet")
            else -> Exception(e.message ?: "Error desconocido")
        }
    }
}
