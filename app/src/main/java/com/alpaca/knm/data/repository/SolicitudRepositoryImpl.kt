package com.alpaca.knm.data.repository

import com.alpaca.knm.data.remote.datasource.SolicitudRemoteDataSource
import com.alpaca.knm.data.remote.dto.SolicitudDto
import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.model.SolicitudStatus
import com.alpaca.knm.domain.repository.SolicitudRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun SolicitudDto.toDomain(): Solicitud {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return Solicitud(
        id = id.toLong(),
        ganaderoId = ganaderoId.toLong(),
        ganaderoNombre = ganaderoNombre,
        kilograms = kilograms,
        totalAmount = totalAmount,
        status = when (status) {
            "APROBADA" -> SolicitudStatus.APROBADA
            "RECHAZADA" -> SolicitudStatus.RECHAZADA
            "DESEMBOLSADA" -> SolicitudStatus.DESEMBOLSADA
            else -> SolicitudStatus.PENDIENTE
        },
        requestDate = try { dateFormat.parse(requestDate ?: "") ?: Date() } catch (_: Exception) { Date() },
        responseDate = try { responseDate?.let { dateFormat.parse(it) } } catch (_: Exception) { null },
        adminComment = adminComment,
        scoring = null
    )
}

class SolicitudRepositoryImpl(
    private val remoteDataSource: SolicitudRemoteDataSource
) : SolicitudRepository {
    
    override suspend fun getAllSolicitudes(): Result<List<Solicitud>> =
        remoteDataSource.getAllSolicitudes().map { dtos -> dtos.map { it.toDomain() } }
    
    override suspend fun getSolicitudesByStatus(status: SolicitudStatus): Result<List<Solicitud>> {
        val statusString = when (status) {
            SolicitudStatus.PENDIENTE -> "PENDIENTE"
            SolicitudStatus.APROBADA -> "APROBADA"
            SolicitudStatus.RECHAZADA -> "RECHAZADA"
            SolicitudStatus.DESEMBOLSADA -> "DESEMBOLSADA"
        }
        return remoteDataSource.getSolicitudesByStatus(statusString).map { dtos -> dtos.map { it.toDomain() } }
    }
    
    override suspend fun getSolicitudById(id: Long): Result<Solicitud> =
        remoteDataSource.getSolicitudById(id).map { it.toDomain() }
    
    override suspend fun approveSolicitud(id: Long, comment: String?): Result<Solicitud> =
        remoteDataSource.approveSolicitud(id, comment).map { it.toDomain() }
    
    override suspend fun rejectSolicitud(id: Long, comment: String): Result<Solicitud> =
        remoteDataSource.rejectSolicitud(id, comment).map { it.toDomain() }
}
