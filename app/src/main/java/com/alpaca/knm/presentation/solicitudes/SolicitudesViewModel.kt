package com.alpaca.knm.presentation.solicitudes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.model.SolicitudStatus
import com.alpaca.knm.domain.usecase.solicitud.ApproveSolicitudUseCase
import com.alpaca.knm.domain.usecase.solicitud.GetAllSolicitudesUseCase
import com.alpaca.knm.domain.usecase.solicitud.GetSolicitudesByStatusUseCase
import com.alpaca.knm.domain.usecase.solicitud.RejectSolicitudUseCase
import kotlinx.coroutines.launch

class SolicitudesViewModel(
    private val getAllSolicitudesUseCase: GetAllSolicitudesUseCase,
    private val getSolicitudesByStatusUseCase: GetSolicitudesByStatusUseCase,
    private val approveSolicitudUseCase: ApproveSolicitudUseCase,
    private val rejectSolicitudUseCase: RejectSolicitudUseCase
) : ViewModel() {
    
    private val _uiState = MutableLiveData<SolicitudesUiState>()
    val uiState: LiveData<SolicitudesUiState> = _uiState
    
    private val _actionResult = MutableLiveData<ActionResult>()
    val actionResult: LiveData<ActionResult> = _actionResult
    
    private var currentFilter: SolicitudStatus? = null
    
    init {
        loadSolicitudes()
    }
    
    fun loadSolicitudes(status: SolicitudStatus? = null) {
        currentFilter = status
        viewModelScope.launch {
            _uiState.value = SolicitudesUiState.Loading
            
            val result = if (status == null) {
                getAllSolicitudesUseCase()
            } else {
                getSolicitudesByStatusUseCase(status)
            }
            
            result.fold(
                onSuccess = { solicitudes ->
                    if (solicitudes.isEmpty()) {
                        _uiState.value = SolicitudesUiState.Empty
                    } else {
                        _uiState.value = SolicitudesUiState.Success(solicitudes)
                    }
                },
                onFailure = { error ->
                    _uiState.value = SolicitudesUiState.Error(
                        error.message ?: "Error al cargar solicitudes"
                    )
                }
            )
        }
    }
    
    fun approveSolicitud(id: Long, comment: String? = null) {
        viewModelScope.launch {
            val result = approveSolicitudUseCase(id, comment)
            result.fold(
                onSuccess = {
                    _actionResult.value = ActionResult.Success("Solicitud aprobada exitosamente")
                    loadSolicitudes(currentFilter)
                },
                onFailure = { error ->
                    _actionResult.value = ActionResult.Error(
                        error.message ?: "Error al aprobar solicitud"
                    )
                }
            )
        }
    }
    
    fun rejectSolicitud(id: Long, comment: String) {
        viewModelScope.launch {
            val result = rejectSolicitudUseCase(id, comment)
            result.fold(
                onSuccess = {
                    _actionResult.value = ActionResult.Success("Solicitud rechazada")
                    loadSolicitudes(currentFilter)
                },
                onFailure = { error ->
                    _actionResult.value = ActionResult.Error(
                        error.message ?: "Error al rechazar solicitud"
                    )
                }
            )
        }
    }
    
    fun clearActionResult() {
        _actionResult.value = null
    }
}

sealed class SolicitudesUiState {
    object Loading : SolicitudesUiState()
    object Empty : SolicitudesUiState()
    data class Success(val solicitudes: List<Solicitud>) : SolicitudesUiState()
    data class Error(val message: String) : SolicitudesUiState()
}

sealed class ActionResult {
    data class Success(val message: String) : ActionResult()
    data class Error(val message: String) : ActionResult()
}
