package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.repository.AuthRepository

/**
 * Caso de uso: Verificar si el usuario está logueado
 */
class IsUserLoggedInUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}
