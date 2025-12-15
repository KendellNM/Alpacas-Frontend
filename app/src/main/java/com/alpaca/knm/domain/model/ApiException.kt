package com.alpaca.knm.domain.model

sealed class ApiException(message: String) : Exception(message) {
    
    class NetworkException(message: String = "Error de conexión. Verifica tu internet") : ApiException(message)
    
    class UnauthorizedException(message: String = "Sesión expirada. Por favor inicia sesión nuevamente") : ApiException(message)
    
    class ForbiddenException(message: String = "No tienes permisos para realizar esta acción") : ApiException(message)
    
    class NotFoundException(message: String = "Recurso no encontrado") : ApiException(message)
    
    class ConflictException(message: String = "Ya existe un recurso con estos datos") : ApiException(message)
    
    class BadRequestException(
        message: String = "Datos inválidos",
        val fieldErrors: Map<String, String>? = null
    ) : ApiException(message)
    
    class ServerException(message: String = "Error interno del servidor. Intenta más tarde") : ApiException(message)
    
    class UnknownException(message: String = "Error desconocido. Intenta nuevamente") : ApiException(message)
}
