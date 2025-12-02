package com.alpaca.knm.domain.usecase.solicitud

import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.repository.SolicitudRepository

class RejectSolicitudUseCase(
    private val repository: SolicitudRepository
) {
    suspend operator fun invoke(id: Long, comment: String): Result<Solicitud> {
        if (comment.isBlank()) {
            return Result.failure(IllegalArgumentException("El motivo del rechazo es obligatorio"))
        }
        return repository.rejectSolicitud(id, comment)
    }
}
