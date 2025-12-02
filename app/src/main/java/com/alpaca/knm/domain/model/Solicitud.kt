package com.alpaca.knm.domain.model

import java.util.Date

data class Solicitud(
    val id: Long,
    val ganaderoId: Long,
    val ganaderoNombre: String,
    val kilograms: Double,
    val totalAmount: Double,
    val status: SolicitudStatus,
    val requestDate: Date,
    val responseDate: Date? = null,
    val adminComment: String? = null,
    val scoring: Int? = null,
    val recomendacion: Recomendacion? = null
)

data class Recomendacion(
    val accion: String, // "aprobar", "revisar", "rechazar"
    val confianza: Int,
    val motivos: List<String>
)

enum class SolicitudStatus {
    PENDIENTE,
    APROBADA,
    RECHAZADA,
    DESEMBOLSADA
}
