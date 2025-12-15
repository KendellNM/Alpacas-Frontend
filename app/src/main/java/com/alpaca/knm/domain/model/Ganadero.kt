package com.alpaca.knm.domain.model

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
    val birthDate: String? = null, // Fecha de nacimiento YYYY-MM-DD
    val sexo: String? = null,
    val createdAt: String? = null,
    val scoring: Int? = 50,
    val status: String? = "activo",
    val gpsLatitud: Double? = null,
    val gpsLongitud: Double? = null,
    val foto: String? = null,
    val totalAnticiposRecibidos: Int? = 0,
    val montoTotalAnticipos: Double? = 0.0,
    val tasaCumplimiento: Int? = 100
) {
    val fullName: String
        get() = if (lastName.isNotEmpty()) "$firstName $lastName" else firstName
    
    val comunidad: String
        get() = district
}
