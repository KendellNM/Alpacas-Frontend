package com.alpaca.knm.domain.repository

import com.alpaca.knm.domain.model.Ganadero

/**
 * Interfaz del repositorio de Ganaderos
 */
interface GanaderoRepository {
    suspend fun getGanaderos(token: String): Result<List<Ganadero>>
    suspend fun getGanaderoById(token: String, id: String): Result<Ganadero>
    suspend fun createGanadero(token: String, ganadero: Ganadero): Result<Ganadero>
    suspend fun updateGanadero(token: String, id: String, ganadero: Ganadero): Result<Ganadero>
    suspend fun deleteGanadero(token: String, id: String): Result<Unit>
}
