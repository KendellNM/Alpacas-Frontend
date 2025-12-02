package com.alpaca.knm.domain.usecase.alpaca

import com.alpaca.knm.domain.repository.AlpacaRepository

class DeleteAlpacaUseCase(
    private val repository: AlpacaRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return repository.deleteAlpaca(id)
    }
}
