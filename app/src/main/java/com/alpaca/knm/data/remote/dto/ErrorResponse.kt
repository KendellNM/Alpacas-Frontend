package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para respuestas de error del backend
 * Estructura estándar de errores
 */
data class ErrorResponse(
    @SerializedName("timestamp")
    val timestamp: String? = null,
    
    @SerializedName("status")
    val status: Int,
    
    @SerializedName("error")
    val error: String,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("path")
    val path: String? = null,
    
    @SerializedName("errors")
    val errors: List<FieldError>? = null
)

/**
 * Errores de validación de campos
 */
data class FieldError(
    @SerializedName("field")
    val field: String,
    
    @SerializedName("message")
    val message: String
)
