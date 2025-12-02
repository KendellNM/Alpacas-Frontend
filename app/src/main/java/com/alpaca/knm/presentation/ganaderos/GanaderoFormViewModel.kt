package com.alpaca.knm.presentation.ganaderos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.knm.domain.model.Ganadero
import com.alpaca.knm.domain.usecase.CreateGanaderoUseCase
import com.alpaca.knm.domain.usecase.UpdateGanaderoUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel para el formulario de Ganadero (Crear/Editar)
 */
class GanaderoFormViewModel(
    private val createGanaderoUseCase: CreateGanaderoUseCase,
    private val updateGanaderoUseCase: UpdateGanaderoUseCase
) : ViewModel() {
    
    private val _uiState = MutableLiveData<GanaderoFormUiState>(GanaderoFormUiState.Idle)
    val uiState: LiveData<GanaderoFormUiState> = _uiState
    
    private val _navigationEvent = MutableLiveData<GanaderoFormNavigationEvent>()
    val navigationEvent: LiveData<GanaderoFormNavigationEvent> = _navigationEvent
    
    private var editingId: String? = null
    
    fun setEditMode(ganadero: Ganadero) {
        editingId = ganadero.id
    }
    
    fun onSaveClicked(ganadero: Ganadero) {
        // Validar campos
        val validation = validateGanadero(ganadero)
        if (!validation.isValid) {
            _uiState.value = GanaderoFormUiState.ValidationError(validation.errors)
            return
        }
        
        if (editingId != null) {
            updateGanadero(editingId!!, ganadero)
        } else {
            createGanadero(ganadero)
        }
    }
    
    private fun createGanadero(ganadero: Ganadero) {
        viewModelScope.launch {
            _uiState.value = GanaderoFormUiState.Loading
            
            createGanaderoUseCase(ganadero)
                .onSuccess {
                    _uiState.value = GanaderoFormUiState.Success("Ganadero creado exitosamente")
                    _navigationEvent.value = GanaderoFormNavigationEvent.NavigateBack
                }
                .onFailure { error ->
                    _uiState.value = GanaderoFormUiState.Error(
                        error.message ?: "Error al crear ganadero"
                    )
                }
        }
    }
    
    private fun updateGanadero(id: String, ganadero: Ganadero) {
        viewModelScope.launch {
            _uiState.value = GanaderoFormUiState.Loading
            
            updateGanaderoUseCase(id, ganadero)
                .onSuccess {
                    _uiState.value = GanaderoFormUiState.Success("Ganadero actualizado exitosamente")
                    _navigationEvent.value = GanaderoFormNavigationEvent.NavigateBack
                }
                .onFailure { error ->
                    _uiState.value = GanaderoFormUiState.Error(
                        error.message ?: "Error al actualizar ganadero"
                    )
                }
        }
    }
    
    private fun validateGanadero(ganadero: Ganadero): ValidationResult {
        val errors = mutableMapOf<String, String>()
        
        if (ganadero.firstName.isBlank()) {
            errors["firstName"] = "El nombre es requerido"
        }
        if (ganadero.lastName.isBlank()) {
            errors["lastName"] = "El apellido es requerido"
        }
        if (ganadero.dni.isBlank()) {
            errors["dni"] = "El DNI es requerido"
        } else if (ganadero.dni.length != 8) {
            errors["dni"] = "El DNI debe tener 8 dígitos"
        }
        if (ganadero.phone.isBlank()) {
            errors["phone"] = "El teléfono es requerido"
        }
        
        return ValidationResult(errors.isEmpty(), errors)
    }
    
    data class ValidationResult(
        val isValid: Boolean,
        val errors: Map<String, String>
    )
}

sealed class GanaderoFormUiState {
    object Idle : GanaderoFormUiState()
    object Loading : GanaderoFormUiState()
    data class Success(val message: String) : GanaderoFormUiState()
    data class Error(val message: String) : GanaderoFormUiState()
    data class ValidationError(val errors: Map<String, String>) : GanaderoFormUiState()
}

sealed class GanaderoFormNavigationEvent {
    object NavigateBack : GanaderoFormNavigationEvent()
}
