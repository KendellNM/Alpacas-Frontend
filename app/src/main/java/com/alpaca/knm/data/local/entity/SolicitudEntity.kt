package com.alpaca.knm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.model.SolicitudStatus
import java.util.Date

@Entity(tableName = "solicitudes")
data class SolicitudEntity(
    @PrimaryKey
    val id: Long,
    val ganaderoId: Long,
    val ganaderoNombre: String,
    val kilograms: Double,
    val totalAmount: Double,
    val status: String,
    val requestDate: Long,
    val responseDate: Long?,
    val adminComment: String?,
    val syncedAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Solicitud {
        return Solicitud(
            id = id,
            ganaderoId = ganaderoId,
            ganaderoNombre = ganaderoNombre,
            kilograms = kilograms,
            totalAmount = totalAmount,
            status = when (status.uppercase()) {
                "PENDIENTE", "PENDING" -> SolicitudStatus.PENDIENTE
                "APROBADA", "APPROVED" -> SolicitudStatus.APROBADA
                "RECHAZADA", "REJECTED" -> SolicitudStatus.RECHAZADA
                "DESEMBOLSADA", "DISBURSED" -> SolicitudStatus.DESEMBOLSADA
                else -> SolicitudStatus.PENDIENTE
            },
            requestDate = Date(requestDate),
            responseDate = responseDate?.let { Date(it) },
            adminComment = adminComment
        )
    }
    
    companion object {
        fun fromDomain(solicitud: Solicitud): SolicitudEntity {
            return SolicitudEntity(
                id = solicitud.id,
                ganaderoId = solicitud.ganaderoId,
                ganaderoNombre = solicitud.ganaderoNombre,
                kilograms = solicitud.kilograms,
                totalAmount = solicitud.totalAmount,
                status = solicitud.status.name,
                requestDate = solicitud.requestDate.time,
                responseDate = solicitud.responseDate?.time,
                adminComment = solicitud.adminComment
            )
        }
    }
}
