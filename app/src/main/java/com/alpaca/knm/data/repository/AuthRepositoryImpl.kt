package com.alpaca.knm.data.repository

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
            // ============================================
            // MODO DEMO: Login de prueba sin backend
            // Usuarios: "ganadero" o "operador" (cualquier password)
            // ============================================
            val demoUser = when (username.lowercase()) {
                "ganadero" -> User(
                    id = "1",
                    username = "ganadero",
                    email = "ganadero@alpaca.com",
                    role = "ROLE_GANADERO",
                    fullName = "Juan Dueñas"
                )
                "operador", "admin" -> User(
                    id = "2",
                    username = "operador",
                    email = "operador@alpaca.com",
                    role = "ROLE_ADMIN",
                    fullName = "Carla M."
                )
                else -> null
            }
            
            if (demoUser != null) {
                // Guardar sesión localmente
                localDataSource.saveUser(demoUser)
                return Result.success(demoUser)
            }
            
            // Si no es usuario demo, intentar login real con API
            val user = remoteDataSource.login(username, password)
            
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
                else -> e.message ?: "Error de conexión. Usa 'ganadero' u 'operador' para probar."
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
            
            // Limpiar datos locales
            localDataSource.clearUser()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun isUserLoggedIn(): Boolean {
        return localDataSource.isUserLoggedIn()
    }
    
    override fun getCurrentUser(): User? {
        return localDataSource.getCurrentUser()
    }
}
