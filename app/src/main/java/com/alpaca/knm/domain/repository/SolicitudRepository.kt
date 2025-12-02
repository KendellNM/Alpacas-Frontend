package com.alpaca.knm.domain.repository

import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.model.SolicitudStatus

interface SolicitudRepository {
    suspend fun getAllSolicitudes(): Result<List<Solicitud>>
    suspend fun getSolicitudesByStatus(status: SolicitudStatus): Result<List<Solicitud>>
    suspend fun getSolicitudById(id: Long): Result<Solicitud>
    suspend fun approveSolicitud(id: Long, comment: String?): Result<Solicitud>
    suspend fun rejectSolicitud(id: Long, comment: String): Result<Solicitud>
}
