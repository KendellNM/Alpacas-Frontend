package com.alpaca.knm.domain.model

/**
 * Entidad de dominio - Estadísticas del Dashboard
 */
data class DashboardStats(
    val alpacasCount: Int = 0,
    val pendingRequests: Int = 0,
    val totalAdvances: Double = 0.0
)
