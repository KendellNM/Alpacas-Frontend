package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.repository.AuthRepository

/**
 * Caso de uso: Cerrar sesión
 * Encapsula la lógica de negocio para el logout
 */
class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}
