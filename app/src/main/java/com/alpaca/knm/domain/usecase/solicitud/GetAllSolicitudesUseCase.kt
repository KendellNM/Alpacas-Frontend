package com.alpaca.knm.domain.usecase.solicitud

import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.repository.SolicitudRepository

class GetAllSolicitudesUseCase(
    private val repository: SolicitudRepository
) {
    suspend operator fun invoke(): Result<List<Solicitud>> {
        return repository.getAllSolicitudes()
    }
}
