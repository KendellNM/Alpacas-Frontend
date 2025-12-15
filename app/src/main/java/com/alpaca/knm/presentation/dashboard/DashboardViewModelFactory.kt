package com.alpaca.knm.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.domain.usecase.auth.GetCurrentUserUseCase
import com.alpaca.knm.domain.usecase.dashboard.GetDashboardStatsUseCase

class DashboardViewModelFactory(
    private val getDashboardStatsUseCase: GetDashboardStatsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(getDashboardStatsUseCase, getCurrentUserUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
