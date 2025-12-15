package com.alpaca.knm.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.knm.domain.model.UserProfile
import com.alpaca.knm.domain.usecase.auth.LogoutUseCase
import com.alpaca.knm.domain.usecase.profile.GetUserProfileUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    
    private val _uiState = MutableLiveData<ProfileUiState>()
    val uiState: LiveData<ProfileUiState> = _uiState
    
    private val _navigationEvent = MutableLiveData<ProfileNavigationEvent>()
    val navigationEvent: LiveData<ProfileNavigationEvent> = _navigationEvent
    
    init {
        loadProfile()
    }
    
    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            
            getUserProfileUseCase()
                .onSuccess { profile ->
                    _uiState.value = ProfileUiState.Success(profile)
                }
                .onFailure { error ->
                    _uiState.value = ProfileUiState.Error(
                        error.message ?: "Error al cargar perfil"
                    )
                }
        }
    }
    
    fun onHomeClicked() {
        _navigationEvent.value = ProfileNavigationEvent.NavigateToHome
    }
    
    fun onWalletClicked() {
        _navigationEvent.value = ProfileNavigationEvent.NavigateToWallet
    }
    
    fun onLogoutClicked() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            
            logoutUseCase()
                .onSuccess {
                    _navigationEvent.value = ProfileNavigationEvent.NavigateToLogin
                }
                .onFailure { error ->
                    _uiState.value = ProfileUiState.Error(
                        error.message ?: "Error al cerrar sesión"
                    )
                }
        }
    }
}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val profile: UserProfile) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

sealed class ProfileNavigationEvent {
    object NavigateToHome : ProfileNavigationEvent()
    object NavigateToWallet : ProfileNavigationEvent()
    object NavigateToLogin : ProfileNavigationEvent()
}
