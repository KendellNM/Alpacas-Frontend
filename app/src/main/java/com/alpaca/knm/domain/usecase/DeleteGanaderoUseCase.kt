package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.repository.AuthRepository
import com.alpaca.knm.domain.repository.GanaderoRepository

/**
 * Caso de uso: Eliminar ganadero
 */
class DeleteGanaderoUseCase(
    private val ganaderoRepository: GanaderoRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        val token = authRepository.getCurrentUser()?.token
            ?: return Result.failure(Exception("No token available"))
        
        return ganaderoRepository.deleteGanadero(token, id)
    }
}
