package com.alpaca.knm.data.remote.dto

import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.model.SolicitudStatus
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SolicitudDto(
    @SerializedName("id") val id: Long,
    @SerializedName("ganaderoId") val ganaderoId: Long,
    @SerializedName("ganaderoNombre") val ganaderoNombre: String,
    @SerializedName("kilograms") val kilograms: Double,
    @SerializedName("totalAmount") val totalAmount: Double,
    @SerializedName("status") val status: String,
    @SerializedName("requestDate") val requestDate: String,
    @SerializedName("responseDate") val responseDate: String? = null,
    @SerializedName("adminComment") val adminComment: String? = null
) {
    fun toDomain(): Solicitud {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
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
            requestDate = try { dateFormat.parse(requestDate) ?: Date() } catch (e: Exception) { Date() },
            responseDate = responseDate?.let { 
                try { dateFormat.parse(it) } catch (e: Exception) { null }
            },
            adminComment = adminComment
        )
    }
}

data class SolicitudActionRequest(
    @SerializedName("comment") val comment: String? = null
)
