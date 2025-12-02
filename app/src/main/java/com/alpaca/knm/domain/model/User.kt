package com.alpaca.knm.domain.model

/**
 * Entidad de dominio - Usuario
 */
data class User(
    val id: String = "",
    val username: String,
    val email: String = "",
    val token: String? = null,
    val role: String? = null,
    val fullName: String? = null
)
