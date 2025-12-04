package com.alpaca.knm.domain.repository

import com.alpaca.knm.domain.model.AlpacaRegistro
import com.alpaca.knm.data.remote.dto.RazaInfo

/**
 * Interfaz del repositorio de registro de alpacas
 */
interface AlpacaRegistroRepository {
    suspend fun getRazas(): Result<List<RazaInfo>>
    suspend fun getRegistros(): Result<List<AlpacaRegistro>>
    suspend fun getMisRegistros(token: String): Result<List<AlpacaRegistro>>
    suspend fun getRegistroById(id: Int): Result<AlpacaRegistro>
    suspend fun crearRegistro(ganaderoId: Int, raza: String, cantidad: Int, adultos: Int): Result<AlpacaRegistro>
    suspend fun actualizarRegistro(id: Int, ganaderoId: Int, raza: String, cantidad: Int, adultos: Int): Result<AlpacaRegistro>
    suspend fun eliminarRegistro(id: Int): Result<Boolean>
}
