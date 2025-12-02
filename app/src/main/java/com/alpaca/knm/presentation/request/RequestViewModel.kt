package com.alpaca.knm.presentation.request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.knm.domain.usecase.CreateAdvanceRequestUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de solicitud de anticipo
 */
class RequestViewModel(
    private val createAdvanceRequestUseCase: CreateAdvanceRequestUseCase
) : ViewModel() {
    
    companion object {
        const val REFERENCE_PRICE = 55.0
    }
    
    private val _uiState = MutableLiveData<RequestUiState>(RequestUiState.Idle)
    val uiState: LiveData<RequestUiState> = _uiState
    
    private val _calculatedAmount = MutableLiveData<Double>(0.0)
    val calculatedAmount: LiveData<Double> = _calculatedAmount
    
    private val _navigationEvent = MutableLiveData<RequestNavigationEvent>()
    val navigationEvent: LiveData<RequestNavigationEvent> = _navigationEvent
    
    fun onKgChanged(kg: Double) {
        val calculated = kg * REFERENCE_PRICE
        _calculatedAmount.value = calculated
    }
    
    fun onSendClicked(kgText: String, amountText: String) {
        // Validar inputs
        if (kgText.isBlank()) {
            _uiState.value = RequestUiState.ValidationError(
                kgError = "Ingrese los kilogramos estimados",
                amountError = null
            )
            return
        }
        
        if (amountText.isBlank()) {
            _uiState.value = RequestUiState.ValidationError(
                kgError = null,
                amountError = "Ingrese un monto válido"
            )
            return
        }
        
        val kg = kgText.toDoubleOrNull()
        val amount = amountText.toDoubleOrNull()
        
        if (kg == null || kg <= 0) {
            _uiState.value = RequestUiState.ValidationError(
                kgError = "Ingrese un valor válido",
                amountError = null
            )
            return
        }
        
        if (amount == null || amount <= 0) {
            _uiState.value = RequestUiState.ValidationError(
                kgError = null,
                amountError = "Ingrese un monto válido"
            )
            return
        }
        
        sendRequest(kg, amount)
    }
    
    private fun sendRequest(kg: Double, amount: Double) {
        viewModelScope.launch {
            _uiState.value = RequestUiState.Loading
            
            createAdvanceRequestUseCase(kg, amount)
                .onSuccess { message ->
                    _uiState.value = RequestUiState.Success(message)
                    _navigationEvent.value = RequestNavigationEvent.NavigateBack
                }
                .onFailure { error ->
                    _uiState.value = RequestUiState.Error(
                        error.message ?: "Error al enviar solicitud"
                    )
                }
        }
    }
    
    fun onHomeClicked() {
        _navigationEvent.value = RequestNavigationEvent.NavigateBack
    }
}

/**
 * Estados de la UI de Request
 */
sealed class RequestUiState {
    object Idle : RequestUiState()
    object Loading : RequestUiState()
    data class Success(val message: String) : RequestUiState()
    data class Error(val message: String) : RequestUiState()
    data class ValidationError(
        val kgError: String?,
        val amountError: String?
    ) : RequestUiState()
}

/**
 * Eventos de navegación
 */
sealed class RequestNavigationEvent {
    object NavigateBack : RequestNavigationEvent()
}
