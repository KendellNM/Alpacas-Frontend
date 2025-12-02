package com.alpaca.knm.presentation.solicitudes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.domain.usecase.solicitud.ApproveSolicitudUseCase
import com.alpaca.knm.domain.usecase.solicitud.GetAllSolicitudesUseCase
import com.alpaca.knm.domain.usecase.solicitud.GetSolicitudesByStatusUseCase
import com.alpaca.knm.domain.usecase.solicitud.RejectSolicitudUseCase

class SolicitudesViewModelFactory(
    private val getAllSolicitudesUseCase: GetAllSolicitudesUseCase,
    private val getSolicitudesByStatusUseCase: GetSolicitudesByStatusUseCase,
    private val approveSolicitudUseCase: ApproveSolicitudUseCase,
    private val rejectSolicitudUseCase: RejectSolicitudUseCase
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SolicitudesViewModel::class.java)) {
            return SolicitudesViewModel(
                getAllSolicitudesUseCase,
                getSolicitudesByStatusUseCase,
                approveSolicitudUseCase,
                rejectSolicitudUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
