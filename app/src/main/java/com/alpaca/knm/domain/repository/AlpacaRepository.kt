package com.alpaca.knm.domain.repository

import com.alpaca.knm.domain.model.Alpaca

interface AlpacaRepository {
    suspend fun getAllAlpacas(): Result<List<Alpaca>>
    suspend fun getAlpacasByGanadero(ganaderoId: Long): Result<List<Alpaca>>
    suspend fun getAlpacaById(id: Long): Result<Alpaca>
    suspend fun createAlpaca(alpaca: Alpaca): Result<Alpaca>
    suspend fun updateAlpaca(id: Long, alpaca: Alpaca): Result<Alpaca>
    suspend fun deleteAlpaca(id: Long): Result<Unit>
}
