package com.alpaca.knm.domain.usecase.solicitud

import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.repository.SolicitudRepository

class ApproveSolicitudUseCase(
    private val repository: SolicitudRepository
) {
    suspend operator fun invoke(id: Long, comment: String? = null): Result<Solicitud> {
        return repository.approveSolicitud(id, comment)
    }
}
