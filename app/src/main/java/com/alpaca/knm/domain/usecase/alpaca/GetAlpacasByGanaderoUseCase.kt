package com.alpaca.knm.domain.usecase.alpaca

import com.alpaca.knm.domain.model.Alpaca
import com.alpaca.knm.domain.repository.AlpacaRepository

class GetAlpacasByGanaderoUseCase(
    private val repository: AlpacaRepository
) {
    suspend operator fun invoke(ganaderoId: Long): Result<List<Alpaca>> {
        return repository.getAlpacasByGanadero(ganaderoId)
    }
}
