package com.alpaca.knm.domain.repository

import com.alpaca.knm.domain.model.DashboardStats

interface DashboardRepository {
    suspend fun getDashboardStats(token: String): Result<DashboardStats>
}
