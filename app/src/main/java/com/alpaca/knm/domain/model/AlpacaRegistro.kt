package com.alpaca.knm.domain.model

/**
 * Modelo de dominio para registro simplificado de alpacas por raza
 */
data class AlpacaRegistro(
    val id: Int,
    val ganaderoId: Int,
    val raza: AlpacaRaza,
    val cantidad: Int,
    val adultos: Int,
    val crias: Int,
    val fechaRegistro: String?
)

// AlpacaRaza está definido en Alpaca.kt
