package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.model.Ganadero
import com.alpaca.knm.domain.repository.AuthRepository
import com.alpaca.knm.domain.repository.GanaderoRepository

/**
 * Caso de uso: Obtener lista de ganaderos
 */
class GetGanaderosUseCase(
    private val ganaderoRepository: GanaderoRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<List<Ganadero>> {
        val token = authRepository.getCurrentUser()?.token
            ?: return Result.failure(Exception("No token available"))
        
        return ganaderoRepository.getGanaderos(token)
    }
}
