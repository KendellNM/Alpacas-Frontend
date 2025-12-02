package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.repository.SolicitudRepository

class RejectSolicitudUseCase(
    private val solicitudRepository: SolicitudRepository
) {
    suspend operator fun invoke(id: Long, comment: String): Result<Solicitud> {
        return solicitudRepository.rejectSolicitud(id, comment)
    }
}
