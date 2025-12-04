package com.alpaca.knm.domain.repository

import com.alpaca.knm.domain.model.User

/**
 * Interfaz del repositorio de autenticación
 * Define el contrato para las operaciones de autenticación
 */
interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
    fun isUserLoggedIn(): Boolean
    fun getCurrentUser(): User?
    fun getToken(): String?
}
