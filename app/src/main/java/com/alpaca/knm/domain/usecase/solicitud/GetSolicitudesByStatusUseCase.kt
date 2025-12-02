package com.alpaca.knm.domain.usecase.solicitud

import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.model.SolicitudStatus
import com.alpaca.knm.domain.repository.SolicitudRepository

class GetSolicitudesByStatusUseCase(
    private val repository: SolicitudRepository
) {
    suspend operator fun invoke(status: SolicitudStatus): Result<List<Solicitud>> {
        return repository.getSolicitudesByStatus(status)
    }
}
