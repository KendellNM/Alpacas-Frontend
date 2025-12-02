package com.alpaca.knm.domain.usecase.alpaca

import com.alpaca.knm.domain.model.Alpaca
import com.alpaca.knm.domain.repository.AlpacaRepository

class GetAllAlpacasUseCase(
    private val repository: AlpacaRepository
) {
    suspend operator fun invoke(): Result<List<Alpaca>> {
        return repository.getAllAlpacas()
    }
}
