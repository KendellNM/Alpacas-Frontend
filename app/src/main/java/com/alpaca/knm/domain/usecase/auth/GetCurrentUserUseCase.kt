package com.alpaca.knm.domain.usecase.auth

import com.alpaca.knm.domain.model.User
import com.alpaca.knm.domain.repository.AuthRepository

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): User? {
        return authRepository.getCurrentUser()
    }
}
