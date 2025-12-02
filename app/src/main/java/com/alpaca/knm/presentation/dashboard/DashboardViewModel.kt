package com.alpaca.knm.presentation.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.knm.domain.model.DashboardStats
import com.alpaca.knm.domain.usecase.GetCurrentUserUseCase
import com.alpaca.knm.domain.usecase.GetDashboardStatsUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Dashboard
 */
class DashboardViewModel(
    private val getDashboardStatsUseCase: GetDashboardStatsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    
    private val _uiState = MutableLiveData<DashboardUiState>()
    val uiState: LiveData<DashboardUiState> = _uiState
    
    private val _navigationEvent = MutableLiveData<DashboardNavigationEvent>()
    val navigationEvent: LiveData<DashboardNavigationEvent> = _navigationEvent
    
    init {
        loadDashboardData()
    }
    
    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            
            val user = getCurrentUserUseCase()
            
            getDashboardStatsUseCase()
                .onSuccess { stats ->
                    _uiState.value = DashboardUiState.Success(
                        username = user?.fullName ?: user?.username ?: "Usuario",
                        stats = stats
                    )
                }
                .onFailure { error ->
                    _uiState.value = DashboardUiState.Error(
                        error.message ?: "Error al cargar datos"
                    )
                }
        }
    }
    
    fun onNewRequestClicked() {
        _navigationEvent.value = DashboardNavigationEvent.NavigateToNewRequest
    }
    
    fun onErrorShown() {
        _uiState.value = DashboardUiState.Loading
        loadDashboardData()
    }
    
    fun onWalletClicked() {
        _navigationEvent.value = DashboardNavigationEvent.NavigateToWallet
    }
    
    fun onProfileClicked() {
        _navigationEvent.value = DashboardNavigationEvent.NavigateToProfile
    }
    
    fun onRefresh() {
        loadDashboardData()
    }
}

/**
 * Estados de la UI del Dashboard
 */
sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(
        val username: String,
        val stats: DashboardStats
    ) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

/**
 * Eventos de navegación del Dashboard
 */
sealed class DashboardNavigationEvent {
    object NavigateToNewRequest : DashboardNavigationEvent()
    object NavigateToWallet : DashboardNavigationEvent()
    object NavigateToProfile : DashboardNavigationEvent()
}
