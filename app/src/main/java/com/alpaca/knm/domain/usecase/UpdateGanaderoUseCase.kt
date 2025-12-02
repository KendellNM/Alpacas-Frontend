package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.model.Ganadero
import com.alpaca.knm.domain.repository.AuthRepository
import com.alpaca.knm.domain.repository.GanaderoRepository

/**
 * Caso de uso: Actualizar ganadero
 */
class UpdateGanaderoUseCase(
    private val ganaderoRepository: GanaderoRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(id: String, ganadero: Ganadero): Result<Ganadero> {
        // Validaciones
        if (ganadero.firstName.isBlank()) {
            return Result.failure(Exception("El nombre es requerido"))
        }
        if (ganadero.lastName.isBlank()) {
            return Result.failure(Exception("El apellido es requerido"))
        }
        
        val token = authRepository.getCurrentUser()?.token
            ?: return Result.failure(Exception("No token available"))
        
        return ganaderoRepository.updateGanadero(token, id, ganadero)
    }
}
