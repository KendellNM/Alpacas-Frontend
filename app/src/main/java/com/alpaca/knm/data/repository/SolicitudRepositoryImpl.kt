package com.alpaca.knm.data.repository

import com.alpaca.knm.data.remote.datasource.SolicitudRemoteDataSource
import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.model.SolicitudStatus
import com.alpaca.knm.domain.repository.SolicitudRepository

class SolicitudRepositoryImpl(
    private val remoteDataSource: SolicitudRemoteDataSource
) : SolicitudRepository {
    
    override suspend fun getAllSolicitudes(): Result<List<Solicitud>> {
        return remoteDataSource.getAllSolicitudes()
            .map { dtos -> dtos.map { it.toDomain() } }
    }
    
    override suspend fun getSolicitudesByStatus(status: SolicitudStatus): Result<List<Solicitud>> {
        val statusString = when (status) {
            SolicitudStatus.PENDIENTE -> "PENDIENTE"
            SolicitudStatus.APROBADA -> "APROBADA"
            SolicitudStatus.RECHAZADA -> "RECHAZADA"
            SolicitudStatus.DESEMBOLSADA -> "DESEMBOLSADA"
        }
        return remoteDataSource.getSolicitudesByStatus(statusString)
            .map { dtos -> dtos.map { it.toDomain() } }
    }
    
    override suspend fun getSolicitudById(id: Long): Result<Solicitud> {
        return remoteDataSource.getSolicitudById(id)
            .map { it.toDomain() }
    }
    
    override suspend fun approveSolicitud(id: Long, comment: String?): Result<Solicitud> {
        return remoteDataSource.approveSolicitud(id, comment)
            .map { it.toDomain() }
    }
    
    override suspend fun rejectSolicitud(id: Long, comment: String): Result<Solicitud> {
        return remoteDataSource.rejectSolicitud(id, comment)
            .map { it.toDomain() }
    }
}
