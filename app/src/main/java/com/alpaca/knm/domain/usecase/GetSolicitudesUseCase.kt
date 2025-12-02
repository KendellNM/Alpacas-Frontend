package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.repository.SolicitudRepository

class GetSolicitudesUseCase(
    private val solicitudRepository: SolicitudRepository
) {
    suspend operator fun invoke(): Result<List<Solicitud>> {
        return solicitudRepository.getAllSolicitudes()
    }
}
