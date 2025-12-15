package com.alpaca.knm.domain.repository

import com.alpaca.knm.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
    fun isUserLoggedIn(): Boolean
    fun getCurrentUser(): User?
    fun getToken(): String?
}
