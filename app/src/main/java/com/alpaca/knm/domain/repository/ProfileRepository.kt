package com.alpaca.knm.domain.repository

import com.alpaca.knm.domain.model.UserProfile

/**
 * Interfaz del repositorio de perfil de usuario
 */
interface ProfileRepository {
    suspend fun getUserProfile(token: String): Result<UserProfile>
}
