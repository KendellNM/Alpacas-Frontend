package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.model.Alpaca
import com.alpaca.knm.domain.repository.AlpacaRepository

class UpdateAlpacaUseCase(
    private val alpacaRepository: AlpacaRepository
) {
    suspend operator fun invoke(id: Long, alpaca: Alpaca): Result<Alpaca> {
        if (alpaca.nombre.isBlank()) {
            return Result.failure(Exception("El nombre es requerido"))
        }
        
        return alpacaRepository.updateAlpaca(id, alpaca)
    }
}
