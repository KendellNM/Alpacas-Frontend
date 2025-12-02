package com.alpaca.knm.domain.usecase

import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.repository.SolicitudRepository

class ApproveSolicitudUseCase(
    private val solicitudRepository: SolicitudRepository
) {
    suspend operator fun invoke(id: Long, comment: String?): Result<Solicitud> {
        return solicitudRepository.approveSolicitud(id, comment)
    }
}
