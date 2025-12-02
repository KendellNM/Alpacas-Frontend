package com.alpaca.knm.domain.usecase.alpaca

import com.alpaca.knm.domain.model.Alpaca
import com.alpaca.knm.domain.repository.AlpacaRepository

class UpdateAlpacaUseCase(
    private val repository: AlpacaRepository
) {
    suspend operator fun invoke(id: Long, alpaca: Alpaca): Result<Alpaca> {
        // Validaciones
        if (alpaca.nombre.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre es obligatorio"))
        }
        if (alpaca.color.isBlank()) {
            return Result.failure(IllegalArgumentException("El color es obligatorio"))
        }
        if (alpaca.edad < 0 || alpaca.edad > 30) {
            return Result.failure(IllegalArgumentException("La edad debe estar entre 0 y 30 años"))
        }
        if (alpaca.peso <= 0 || alpaca.peso > 200) {
            return Result.failure(IllegalArgumentException("El peso debe estar entre 0 y 200 kg"))
        }
        
        return repository.updateAlpaca(id, alpaca)
    }
}
