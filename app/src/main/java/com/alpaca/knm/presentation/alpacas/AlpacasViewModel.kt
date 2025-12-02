package com.alpaca.knm.presentation.alpacas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.knm.domain.model.Alpaca
import com.alpaca.knm.domain.usecase.alpaca.DeleteAlpacaUseCase
import com.alpaca.knm.domain.usecase.alpaca.GetAllAlpacasUseCase
import com.alpaca.knm.domain.usecase.alpaca.GetAlpacasByGanaderoUseCase
import kotlinx.coroutines.launch

class AlpacasViewModel(
    private val getAllAlpacasUseCase: GetAllAlpacasUseCase,
    private val getAlpacasByGanaderoUseCase: GetAlpacasByGanaderoUseCase,
    private val deleteAlpacaUseCase: DeleteAlpacaUseCase
) : ViewModel() {
    
    private val _uiState = MutableLiveData<AlpacasUiState>()
    val uiState: LiveData<AlpacasUiState> = _uiState
    
    private val _deleteResult = MutableLiveData<DeleteResult>()
    val deleteResult: LiveData<DeleteResult> = _deleteResult
    
    private var currentGanaderoId: Long? = null
    private var allAlpacas = listOf<Alpaca>()
    
    fun loadAlpacas(ganaderoId: Long? = null) {
        currentGanaderoId = ganaderoId
        viewModelScope.launch {
            _uiState.value = AlpacasUiState.Loading
            
            val result = if (ganaderoId == null) {
                getAllAlpacasUseCase()
            } else {
                getAlpacasByGanaderoUseCase(ganaderoId)
            }
            
            result.fold(
                onSuccess = { alpacas ->
                    allAlpacas = alpacas
                    if (alpacas.isEmpty()) {
                        _uiState.value = AlpacasUiState.Empty
                    } else {
                        _uiState.value = AlpacasUiState.Success(alpacas)
                    }
                },
                onFailure = { error ->
                    _uiState.value = AlpacasUiState.Error(
                        error.message ?: "Error al cargar alpacas"
                    )
                }
            )
        }
    }
    
    fun searchAlpacas(query: String) {
        if (query.isBlank()) {
            _uiState.value = AlpacasUiState.Success(allAlpacas)
            return
        }
        
        val filtered = allAlpacas.filter { alpaca ->
            alpaca.nombre.contains(query, ignoreCase = true) ||
            alpaca.color.contains(query, ignoreCase = true) ||
            alpaca.raza.name.contains(query, ignoreCase = true)
        }
        
        if (filtered.isEmpty()) {
            _uiState.value = AlpacasUiState.Empty
        } else {
            _uiState.value = AlpacasUiState.Success(filtered)
        }
    }
    
    fun deleteAlpaca(id: Long) {
        viewModelScope.launch {
            val result = deleteAlpacaUseCase(id)
            result.fold(
                onSuccess = {
                    _deleteResult.value = DeleteResult.Success("Alpaca eliminada exitosamente")
                    loadAlpacas(currentGanaderoId)
                },
                onFailure = { error ->
                    _deleteResult.value = DeleteResult.Error(
                        error.message ?: "Error al eliminar alpaca"
                    )
                }
            )
        }
    }
    
    fun clearDeleteResult() {
        _deleteResult.value = null
    }
}

sealed class AlpacasUiState {
    object Loading : AlpacasUiState()
    object Empty : AlpacasUiState()
    data class Success(val alpacas: List<Alpaca>) : AlpacasUiState()
    data class Error(val message: String) : AlpacasUiState()
}

sealed class DeleteResult {
    data class Success(val message: String) : DeleteResult()
    data class Error(val message: String) : DeleteResult()
}
