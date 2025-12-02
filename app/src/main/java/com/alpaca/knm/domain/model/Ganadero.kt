package com.alpaca.knm.domain.model

/**
 * Entidad de dominio - Ganadero
 * Basado en el prototipo HTML
 */
data class Ganadero(
    val id: String = "",
    val firstName: String,
    val lastName: String,
    val dni: String,
    val phone: String,
    val email: String = "",
    val address: String = "",
    val district: String, // Comunidad
    val province: String = "",
    val department: String = "",
    val alpacasCount: Int = 0,
    val createdAt: String? = null,
    // Nuevos campos del prototipo
    val scoring: Int? = 50, // Scoring crediticio
    val status: String? = "activo", // activo, inactivo, suspendido
    val gpsLatitud: Double? = null,
    val gpsLongitud: Double? = null,
    val foto: String? = null,
    // Estadísticas
    val totalAnticiposRecibidos: Int? = 0,
    val montoTotalAnticipos: Double? = 0.0,
    val tasaCumplimiento: Int? = 100
) {
    val fullName: String
        get() = if (lastName.isNotEmpty()) "$firstName $lastName" else firstName
    
    val comunidad: String
        get() = district
}
