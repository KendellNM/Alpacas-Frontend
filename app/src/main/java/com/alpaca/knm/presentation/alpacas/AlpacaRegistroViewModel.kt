package com.alpaca.knm.presentation.alpacas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.knm.domain.model.AlpacaRegistro
import com.alpaca.knm.data.remote.dto.RazaInfo
import com.alpaca.knm.domain.repository.AlpacaRegistroRepository
import kotlinx.coroutines.launch

class AlpacaRegistroViewModel(
    private val repository: AlpacaRegistroRepository
) : ViewModel() {
    
    private val _uiState = MutableLiveData<AlpacaRegistroUiState>(AlpacaRegistroUiState.Loading)
    val uiState: LiveData<AlpacaRegistroUiState> = _uiState
    
    private val _razas = MutableLiveData<List<RazaInfo>>()
    val razas: LiveData<List<RazaInfo>> = _razas
    
    private val _registros = MutableLiveData<List<AlpacaRegistro>>()
    val registros: LiveData<List<AlpacaRegistro>> = _registros
    
    init {
        cargarRazas()
        cargarRegistros()
    }
    
    fun cargarRazas() {
        viewModelScope.launch {
            repository.getRazas()
                .onSuccess { _razas.value = it }
                .onFailure { }
        }
    }
    
    fun cargarRegistros() {
        _uiState.value = AlpacaRegistroUiState.Loading
        viewModelScope.launch {
            repository.getRegistros()
                .onSuccess { 
                    _registros.value = it
                    _uiState.value = if (it.isEmpty()) {
                        AlpacaRegistroUiState.Empty
                    } else {
                        AlpacaRegistroUiState.Success(it)
                    }
                }
                .onFailure { 
                    _uiState.value = AlpacaRegistroUiState.Error(it.message ?: "Error desconocido")
                }
        }
    }

    fun crearRegistro(ganaderoId: Int, raza: String, cantidad: Int, adultos: Int) {
        _uiState.value = AlpacaRegistroUiState.Loading
        viewModelScope.launch {
            repository.crearRegistro(ganaderoId, raza, cantidad, adultos)
                .onSuccess { 
                    _uiState.value = AlpacaRegistroUiState.Created(it)
                    cargarRegistros()
                }
                .onFailure { 
                    _uiState.value = AlpacaRegistroUiState.Error(it.message ?: "Error al crear")
                }
        }
    }
    
    fun actualizarRegistro(id: Int, ganaderoId: Int, raza: String, cantidad: Int, adultos: Int) {
        _uiState.value = AlpacaRegistroUiState.Loading
        viewModelScope.launch {
            repository.actualizarRegistro(id, ganaderoId, raza, cantidad, adultos)
                .onSuccess { 
                    _uiState.value = AlpacaRegistroUiState.Updated(it)
                    cargarRegistros()
                }
                .onFailure { 
                    _uiState.value = AlpacaRegistroUiState.Error(it.message ?: "Error al actualizar")
                }
        }
    }
    
    fun eliminarRegistro(id: Int) {
        _uiState.value = AlpacaRegistroUiState.Loading
        viewModelScope.launch {
            repository.eliminarRegistro(id)
                .onSuccess { 
                    _uiState.value = AlpacaRegistroUiState.Deleted
                    cargarRegistros()
                }
                .onFailure { 
                    _uiState.value = AlpacaRegistroUiState.Error(it.message ?: "Error al eliminar")
                }
        }
    }
}

sealed class AlpacaRegistroUiState {
    object Loading : AlpacaRegistroUiState()
    object Empty : AlpacaRegistroUiState()
    data class Success(val registros: List<AlpacaRegistro>) : AlpacaRegistroUiState()
    data class Created(val registro: AlpacaRegistro) : AlpacaRegistroUiState()
    data class Updated(val registro: AlpacaRegistro) : AlpacaRegistroUiState()
    object Deleted : AlpacaRegistroUiState()
    data class Error(val message: String) : AlpacaRegistroUiState()
}
