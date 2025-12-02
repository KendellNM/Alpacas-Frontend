package com.alpaca.knm.domain.model

/**
 * Entidad de dominio - Perfil de Usuario
 */
data class UserProfile(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "",
    val location: String = "",
    val avatarUrl: String? = null
)
