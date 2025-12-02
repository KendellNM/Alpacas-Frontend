package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.repository.AlpacaRepository

class DeleteAlpacaUseCase(
    private val alpacaRepository: AlpacaRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return alpacaRepository.deleteAlpaca(id)
    }
}
