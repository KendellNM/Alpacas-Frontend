package com.alpaca.knm.domain.repository

import com.alpaca.knm.domain.model.AdvanceRequest

/**
 * Interfaz del repositorio de solicitudes de anticipo
 */
interface AdvanceRepository {
    suspend fun createAdvanceRequest(
        token: String,
        request: AdvanceRequest
    ): Result<String>
}
