package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.model.User
import com.alpaca.knm.domain.repository.AuthRepository

/**
 * Caso de uso: Obtener usuario actual
 */
class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): User? {
        return authRepository.getCurrentUser()
    }
}
