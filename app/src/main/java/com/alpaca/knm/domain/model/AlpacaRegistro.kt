package com.alpaca.knm.domain.model

data class AlpacaRegistro(
    val id: Int,
    val ganaderoId: Int,
    val raza: AlpacaRaza,
    val cantidad: Int,
    val adultos: Int,
    val crias: Int,
    val fechaRegistro: String?
)
