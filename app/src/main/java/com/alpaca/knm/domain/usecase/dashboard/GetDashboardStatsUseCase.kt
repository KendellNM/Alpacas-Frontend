package com.alpaca.knm.domain.usecase.dashboard

import com.alpaca.knm.domain.model.DashboardStats
import com.alpaca.knm.domain.repository.AuthRepository
import com.alpaca.knm.domain.repository.DashboardRepository

class GetDashboardStatsUseCase(
    private val dashboardRepository: DashboardRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<DashboardStats> {
        val token = authRepository.getCurrentUser()?.token
            ?: return Result.failure(Exception("No token available"))
        
        return dashboardRepository.getDashboardStats(token)
    }
}
