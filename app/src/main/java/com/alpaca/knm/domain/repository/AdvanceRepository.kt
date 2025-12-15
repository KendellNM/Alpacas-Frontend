package com.alpaca.knm.domain.repository

import com.alpaca.knm.domain.model.AdvanceRequest

interface AdvanceRepository {
    suspend fun createAdvanceRequest(
        token: String,
        request: AdvanceRequest
    ): Result<String>
}
