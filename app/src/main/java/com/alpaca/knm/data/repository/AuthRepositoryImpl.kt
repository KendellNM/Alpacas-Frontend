package com.alpaca.knm.data.repository

import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.source.local.AuthLocalDataSource
import com.alpaca.knm.data.source.remote.AuthRemoteDataSource
import com.alpaca.knm.domain.model.User
import com.alpaca.knm.domain.repository.AuthRepository

/**
 * Implementación del repositorio de autenticación
 * Coordina entre fuentes de datos locales y remotas
 */
class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {
    
    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            // Login real con API del backend
            val user = remoteDataSource.login(username, password)
            
            // Guardar token en RetrofitClient para peticiones autenticadas
            RetrofitClient.setAuthToken(user.token)
            
            // Guardar sesión localmente (token, role, etc.)
            localDataSource.saveUser(user)
            
            Result.success(user)
        } catch (e: Exception) {
            // Manejar errores específicos de la API
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
            // Intentar cerrar sesión en el servidor
            try {
                val token = localDataSource.getToken()
                if (token != null) {
                    remoteDataSource.logout(token)
                }
            } catch (e: Exception) {
                // Continuar aunque falle el logout en servidor
            }
            
            // Limpiar token de RetrofitClient
            RetrofitClient.setAuthToken(null)
            
            // Limpiar datos locales
            localDataSource.clearUser()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun isUserLoggedIn(): Boolean {
        val isLoggedIn = localDataSource.isUserLoggedIn()
        // Restaurar token en RetrofitClient si hay sesión activa
        if (isLoggedIn) {
            val token = localDataSource.getToken()
            RetrofitClient.setAuthToken(token)
        }
        return isLoggedIn
    }
    
    override fun getCurrentUser(): User? {
        val user = localDataSource.getCurrentUser()
        // Restaurar token en RetrofitClient
        if (user?.token != null) {
            RetrofitClient.setAuthToken(user.token)
        }
        return user
    }
    
    override fun getToken(): String? {
        return localDataSource.getToken()
    }
}
