package com.alpaca.knm.domain.model

/**
 * Excepciones personalizadas para errores de API
 */
sealed class ApiException(message: String) : Exception(message) {
    
    /**
     * Error de red (sin conexión, timeout)
     */
    class NetworkException(message: String = "Error de conexión. Verifica tu internet") : ApiException(message)
    
    /**
     * Error de autenticación (401)
     */
    class UnauthorizedException(message: String = "Sesión expirada. Por favor inicia sesión nuevamente") : ApiException(message)
    
    /**
     * Error de permisos (403)
     */
    class ForbiddenException(message: String = "No tienes permisos para realizar esta acción") : ApiException(message)
    
    /**
     * Recurso no encontrado (404)
     */
    class NotFoundException(message: String = "Recurso no encontrado") : ApiException(message)
    
    /**
     * Conflicto (409)
     */
    class ConflictException(message: String = "Ya existe un recurso con estos datos") : ApiException(message)
    
    /**
     * Datos inválidos (400)
     */
    class BadRequestException(
        message: String = "Datos inválidos",
        val fieldErrors: Map<String, String>? = null
    ) : ApiException(message)
    
    /**
     * Error del servidor (500)
     */
    class ServerException(message: String = "Error interno del servidor. Intenta más tarde") : ApiException(message)
    
    /**
     * Error desconocido
     */
    class UnknownException(message: String = "Error desconocido. Intenta nuevamente") : ApiException(message)
}
