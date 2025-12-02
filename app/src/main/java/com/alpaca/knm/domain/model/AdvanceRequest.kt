package com.alpaca.knm.domain.model

/**
 * Entidad de dominio - Solicitud de Anticipo
 */
data class AdvanceRequest(
    val estimatedKg: Double,
    val requestedAmount: Double,
    val referencePrice: Double = 55.0
) {
    val calculatedAmount: Double
        get() = estimatedKg * referencePrice
}
