package com.alpaca.knm.domain.repository

import com.alpaca.knm.domain.model.DashboardStats

/**
 * Interfaz del repositorio de Dashboard
 */
interface DashboardRepository {
    suspend fun getDashboardStats(token: String): Result<DashboardStats>
}
