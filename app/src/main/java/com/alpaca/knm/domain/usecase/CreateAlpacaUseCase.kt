package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.model.Alpaca
import com.alpaca.knm.domain.repository.AlpacaRepository

class CreateAlpacaUseCase(
    private val alpacaRepository: AlpacaRepository
) {
    suspend operator fun invoke(alpaca: Alpaca): Result<Alpaca> {
        if (alpaca.nombre.isBlank()) {
            return Result.failure(Exception("El nombre es requerido"))
        }
        if (alpaca.edad < 0) {
            return Result.failure(Exception("La edad debe ser positiva"))
        }
        if (alpaca.peso <= 0) {
            return Result.failure(Exception("El peso debe ser mayor a 0"))
        }
        
        return alpacaRepository.createAlpaca(alpaca)
    }
}
