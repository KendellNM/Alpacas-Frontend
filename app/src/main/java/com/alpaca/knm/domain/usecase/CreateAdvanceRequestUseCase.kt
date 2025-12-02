package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.model.AdvanceRequest
import com.alpaca.knm.domain.repository.AdvanceRepository
import com.alpaca.knm.domain.repository.AuthRepository

/**
 * Caso de uso: Crear solicitud de anticipo
 */
class CreateAdvanceRequestUseCase(
    private val advanceRepository: AdvanceRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        estimatedKg: Double,
        requestedAmount: Double
    ): Result<String> {
        // Validaciones
        if (estimatedKg <= 0) {
            return Result.failure(Exception("Los kilogramos deben ser mayor a 0"))
        }
        
        if (requestedAmount <= 0) {
            return Result.failure(Exception("El monto debe ser mayor a 0"))
        }
        
        // Obtener token
        val token = authRepository.getCurrentUser()?.token
            ?: return Result.failure(Exception("No token available"))
        
        // Crear solicitud
        val request = AdvanceRequest(
            estimatedKg = estimatedKg,
            requestedAmount = requestedAmount
        )
        
        return advanceRepository.createAdvanceRequest(token, request)
    }
}
