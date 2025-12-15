package com.alpaca.knm.domain.usecase.ganadero

import com.alpaca.knm.domain.model.Ganadero
import com.alpaca.knm.domain.repository.AuthRepository
import com.alpaca.knm.domain.repository.GanaderoRepository

class CreateGanaderoUseCase(
    private val ganaderoRepository: GanaderoRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(ganadero: Ganadero): Result<Ganadero> {
        if (ganadero.firstName.isBlank()) {
            return Result.failure(Exception("El nombre es requerido"))
        }
        if (ganadero.lastName.isBlank()) {
            return Result.failure(Exception("El apellido es requerido"))
        }
        if (ganadero.dni.isBlank() || ganadero.dni.length != 8) {
            return Result.failure(Exception("DNI inválido (debe tener 8 dígitos)"))
        }
        
        val token = authRepository.getCurrentUser()?.token
            ?: return Result.failure(Exception("No token available"))
        
        return ganaderoRepository.createGanadero(token, ganadero)
    }
}
