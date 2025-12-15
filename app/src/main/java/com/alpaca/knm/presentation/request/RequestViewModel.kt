package com.alpaca.knm.presentation.request

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alpaca.knm.data.sync.SolicitudSyncManager
import com.alpaca.knm.domain.usecase.advance.CreateAdvanceRequestUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RequestViewModel(
    application: Application,
    private val createAdvanceRequestUseCase: CreateAdvanceRequestUseCase,
    private val ganaderoId: Long
) : AndroidViewModel(application) {
    
    companion object {
        const val REFERENCE_PRICE = 55.0
    }
    
    private val syncManager: SolicitudSyncManager? = try {
        SolicitudSyncManager.getInstance(application)
    } catch (e: Exception) {
        null
    }
    
    private val _uiState = MutableLiveData<RequestUiState>(RequestUiState.Idle)
    val uiState: LiveData<RequestUiState> = _uiState
    
    private val _calculatedAmount = MutableLiveData<Double>(0.0)
    val calculatedAmount: LiveData<Double> = _calculatedAmount
    
    private val _navigationEvent = MutableLiveData<RequestNavigationEvent>()
    val navigationEvent: LiveData<RequestNavigationEvent> = _navigationEvent
    
    private val _isOnline = MutableLiveData<Boolean>(true)
    val isOnline: LiveData<Boolean> = _isOnline
    
    private val _pendingCount = MutableLiveData<Int>(0)
    val pendingCount: LiveData<Int> = _pendingCount
    
    init {
        if (syncManager != null) {
            observeNetwork()
            observePendingCount()
        }
    }
    
    private fun observeNetwork() {
        viewModelScope.launch {
            syncManager?.observeNetwork()?.collectLatest { online ->
                _isOnline.postValue(online)
            }
        }
    }
    
    private fun observePendingCount() {
        viewModelScope.launch {
            syncManager?.getPendingCount()?.collectLatest { count ->
                _pendingCount.postValue(count)
            }
        }
    }
    
    fun onKgChanged(kg: Double) {
        val calculated = kg * REFERENCE_PRICE
        _calculatedAmount.value = calculated
    }
    
    fun onSendClicked(kgText: String, amountText: String) {
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
                amountError = "Ingrese un monto valido"
            )
            return
        }
        
        val kg = kgText.toDoubleOrNull()
        val amount = amountText.toDoubleOrNull()
        
        if (kg == null || kg <= 0) {
            _uiState.value = RequestUiState.ValidationError(
                kgError = "Ingrese un valor valido",
                amountError = null
            )
            return
        }
        
        if (amount == null || amount <= 0) {
            _uiState.value = RequestUiState.ValidationError(
                kgError = null,
                amountError = "Ingrese un monto valido"
            )
            return
        }
        
        sendRequest(kg, amount)
    }
    
    private fun sendRequest(kg: Double, amount: Double) {
        viewModelScope.launch {
            _uiState.value = RequestUiState.Loading
            
            val isOnline = syncManager?.isOnline() ?: true
            
            if (isOnline) {
                createAdvanceRequestUseCase(kg, amount)
                    .onSuccess { message ->
                        _uiState.value = RequestUiState.Success(message)
                        _navigationEvent.value = RequestNavigationEvent.NavigateBack
                    }
                    .onFailure { error ->
                        if (syncManager != null) {
                            saveOffline(kg, amount)
                        } else {
                            _uiState.value = RequestUiState.Error(error.message ?: "Error al enviar")
                        }
                    }
            } else {
                saveOffline(kg, amount)
            }
        }
    }
    
    private suspend fun saveOffline(kg: Double, amount: Double) {
        try {
            syncManager?.savePendingSolicitud(ganaderoId, kg, amount)
            _uiState.value = RequestUiState.SavedOffline
            _navigationEvent.value = RequestNavigationEvent.NavigateBack
        } catch (e: Exception) {
            _uiState.value = RequestUiState.Error(
                e.message ?: "Error al guardar solicitud"
            )
        }
    }
    
    fun syncPending() {
        viewModelScope.launch {
            if (syncManager?.isOnline() == true) {
                val result = syncManager.syncPendingSolicitudes()
                if (result.synced > 0) {
                    _uiState.value = RequestUiState.SyncComplete(result.synced)
                }
            }
        }
    }
    
    fun onHomeClicked() {
        _navigationEvent.value = RequestNavigationEvent.NavigateBack
    }
}

sealed class RequestUiState {
    object Idle : RequestUiState()
    object Loading : RequestUiState()
    data class Success(val message: String) : RequestUiState()
    data class Error(val message: String) : RequestUiState()
    data class ValidationError(
        val kgError: String?,
        val amountError: String?
    ) : RequestUiState()
    object SavedOffline : RequestUiState()
    data class SyncComplete(val count: Int) : RequestUiState()
}

sealed class RequestNavigationEvent {
    object NavigateBack : RequestNavigationEvent()
}
