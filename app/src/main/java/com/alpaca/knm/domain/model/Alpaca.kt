package com.alpaca.knm.domain.model

import java.util.Date

data class Alpaca(
    val id: Long,
    val ganaderoId: Long,
    val nombre: String,
    val raza: AlpacaRaza,
    val color: String,
    val edad: Int,
    val peso: Double,
    val sexo: AlpacaSexo,
    val estado: AlpacaEstado,
    val fechaRegistro: Date,
    val observaciones: String?
)

enum class AlpacaRaza {
    HUACAYA,
    SURI
}

enum class AlpacaSexo {
    MACHO,
    HEMBRA
}

enum class AlpacaEstado {
    ACTIVO,
    VENDIDO,
    FALLECIDO
}
