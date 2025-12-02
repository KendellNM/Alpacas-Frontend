package com.alpaca.knm.presentation.ganaderos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.knm.domain.model.Ganadero
import com.alpaca.knm.domain.usecase.DeleteGanaderoUseCase
import com.alpaca.knm.domain.usecase.GetGanaderosUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel para la lista de Ganaderos
 */
class GanaderosViewModel(
    private val getGanaderosUseCase: GetGanaderosUseCase,
    private val deleteGanaderoUseCase: DeleteGanaderoUseCase
) : ViewModel() {
    
    private val _uiState = MutableLiveData<GanaderosUiState>()
    val uiState: LiveData<GanaderosUiState> = _uiState
    
    private val _navigationEvent = MutableLiveData<GanaderosNavigationEvent>()
    val navigationEvent: LiveData<GanaderosNavigationEvent> = _navigationEvent
    
    private var allGanaderos: List<Ganadero> = emptyList()
    
    init {
        loadGanaderos()
    }
    
    fun loadGanaderos() {
        viewModelScope.launch {
            _uiState.value = GanaderosUiState.Loading
            
            getGanaderosUseCase()
                .onSuccess { ganaderos ->
                    allGanaderos = ganaderos
                    if (ganaderos.isEmpty()) {
                        _uiState.value = GanaderosUiState.Empty
                    } else {
                        _uiState.value = GanaderosUiState.Success(ganaderos)
                    }
                }
                .onFailure { error ->
                    _uiState.value = GanaderosUiState.Error(
                        error.message ?: "Error al cargar ganaderos"
                    )
                }
        }
    }
    
    fun onSearchQueryChanged(query: String) {
        if (query.isBlank()) {
            _uiState.value = GanaderosUiState.Success(allGanaderos)
            return
        }
        
        val filtered = allGanaderos.filter { ganadero ->
            ganadero.fullName.contains(query, ignoreCase = true) ||
            ganadero.dni.contains(query)
        }
        
        _uiState.value = if (filtered.isEmpty()) {
            GanaderosUiState.Empty
        } else {
            GanaderosUiState.Success(filtered)
        }
    }
    
    fun onGanaderoClicked(ganadero: Ganadero) {
        _navigationEvent.value = GanaderosNavigationEvent.NavigateToDetail(ganadero.id)
    }
    
    fun onAddGanaderoClicked() {
        _navigationEvent.value = GanaderosNavigationEvent.NavigateToCreate
    }
    
    fun onDeleteGanadero(id: String) {
        viewModelScope.launch {
            _uiState.value = GanaderosUiState.Loading
            
            deleteGanaderoUseCase(id)
                .onSuccess {
                    loadGanaderos()
                }
                .onFailure { error ->
                    _uiState.value = GanaderosUiState.Error(
                        error.message ?: "Error al eliminar ganadero"
                    )
                }
        }
    }
}

sealed class GanaderosUiState {
    object Loading : GanaderosUiState()
    object Empty : GanaderosUiState()
    data class Success(val ganaderos: List<Ganadero>) : GanaderosUiState()
    data class Error(val message: String) : GanaderosUiState()
}

sealed class GanaderosNavigationEvent {
    data class NavigateToDetail(val id: String) : GanaderosNavigationEvent()
    object NavigateToCreate : GanaderosNavigationEvent()
}
