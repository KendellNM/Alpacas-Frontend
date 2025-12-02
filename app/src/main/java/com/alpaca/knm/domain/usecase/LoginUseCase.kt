package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.model.User
import com.alpaca.knm.domain.repository.AuthRepository

/**
 * Caso de uso: Iniciar sesión
 * Encapsula la lógica de negocio para el login
 */
class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        // Validaciones de negocio
        if (username.isBlank()) {
            return Result.failure(Exception("Username cannot be empty"))
        }
        
        if (password.isBlank()) {
            return Result.failure(Exception("Password cannot be empty"))
        }
        
        // Delegar al repositorio
        return authRepository.login(username, password)
    }
}
