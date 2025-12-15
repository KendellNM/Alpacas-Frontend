package com.alpaca.knm.data.repository

import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.source.local.AuthLocalDataSource
import com.alpaca.knm.data.remote.datasource.AuthRemoteDataSource
import com.alpaca.knm.domain.model.User
import com.alpaca.knm.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {
    
    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            val user = remoteDataSource.login(username, password)
            RetrofitClient.setAuthToken(user.token)
            localDataSource.saveUser(user)
            Result.success(user)
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("401") == true -> "Credenciales inválidas"
                e.message?.contains("404") == true -> "Usuario no encontrado"
                e.message?.contains("500") == true -> "Error del servidor"
                e.message?.contains("timeout") == true -> "Tiempo de espera agotado"
                else -> e.message ?: "Error de conexión"
            }
            Result.failure(Exception(errorMessage))
        }
    }
    
    override suspend fun logout(): Result<Unit> {
        return try {
            try {
                val token = localDataSource.getToken()
                if (token != null) remoteDataSource.logout(token)
            } catch (_: Exception) { }
            RetrofitClient.setAuthToken(null)
            localDataSource.clearUser()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun isUserLoggedIn(): Boolean {
        val isLoggedIn = localDataSource.isUserLoggedIn()
        if (isLoggedIn) RetrofitClient.setAuthToken(localDataSource.getToken())
        return isLoggedIn
    }
    
    override fun getCurrentUser(): User? {
        val user = localDataSource.getCurrentUser()
        if (user?.token != null) RetrofitClient.setAuthToken(user.token)
        return user
    }
    
    override fun getToken(): String? = localDataSource.getToken()
}
