package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.repository.SolicitudRepository

class GetMySolicitudesUseCase(
    private val solicitudRepository: SolicitudRepository
) {
    suspend operator fun invoke(): Result<List<Solicitud>> {
        // For now, return all solicitudes - in production this would filter by current user
        return solicitudRepository.getAllSolicitudes()
    }
}
