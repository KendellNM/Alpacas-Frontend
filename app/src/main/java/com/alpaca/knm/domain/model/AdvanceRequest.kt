package com.alpaca.knm.domain.model

data class AdvanceRequest(
    val estimatedKg: Double,
    val requestedAmount: Double,
    val referencePrice: Double = 55.0
) {
    val calculatedAmount: Double
        get() = estimatedKg * referencePrice
}
