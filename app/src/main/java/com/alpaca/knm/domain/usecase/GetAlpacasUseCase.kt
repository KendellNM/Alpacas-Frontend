package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.model.Alpaca
import com.alpaca.knm.domain.repository.AlpacaRepository

class GetAlpacasUseCase(
    private val alpacaRepository: AlpacaRepository
) {
    suspend operator fun invoke(): Result<List<Alpaca>> {
        return alpacaRepository.getAllAlpacas()
    }
}
