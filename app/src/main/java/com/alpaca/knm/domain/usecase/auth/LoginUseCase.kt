package com.alpaca.knm.domain.usecase.auth

import com.alpaca.knm.domain.model.User
import com.alpaca.knm.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        if (username.isBlank()) {
            return Result.failure(Exception("Username cannot be empty"))
        }
        if (password.isBlank()) {
            return Result.failure(Exception("Password cannot be empty"))
        }
        return authRepository.login(username, password)
    }
}
