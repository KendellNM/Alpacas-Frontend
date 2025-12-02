package com.alpaca.knm.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.knm.domain.model.User
import com.alpaca.knm.domain.usecase.LoginUseCase
import com.alpaca.knm.domain.usecase.ValidateCredentialsUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Login
 * Maneja la lógica de presentación y estado de la UI
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val validateCredentialsUseCase: ValidateCredentialsUseCase
) : ViewModel() {
    
    private val _uiState = MutableLiveData<LoginUiState>()
    val uiState: LiveData<LoginUiState> = _uiState
    
    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent> = _navigationEvent
    
    fun onLoginClicked(username: String, password: String) {
        // Validar credenciales
        val validation = validateCredentialsUseCase(username, password)
        
        if (!validation.isValid) {
            _uiState.value = LoginUiState.ValidationError(
                usernameError = validation.usernameError,
                passwordError = validation.passwordError
            )
            return
        }
        
        // Iniciar login
        performLogin(username, password)
    }
    
    private fun performLogin(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            
            loginUseCase(username, password)
                .onSuccess { user ->
                    _uiState.value = LoginUiState.Success(user)
                    _navigationEvent.value = NavigationEvent.NavigateToHome(user)
                }
                .onFailure { error ->
                    _uiState.value = LoginUiState.Error(
                        error.message ?: "Error desconocido"
                    )
                }
        }
    }
    
    fun onErrorShown() {
        _uiState.value = LoginUiState.Idle
    }
}

/**
 * Estados de la UI de Login
 */
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val user: User) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    data class ValidationError(
        val usernameError: String?,
        val passwordError: String?
    ) : LoginUiState()
}

/**
 * Eventos de navegación
 */
sealed class NavigationEvent {
    data class NavigateToHome(val user: User) : NavigationEvent()
}
