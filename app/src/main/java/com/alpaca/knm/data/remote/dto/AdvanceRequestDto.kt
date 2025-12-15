package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AdvanceRequestDto(
    @SerializedName("estimated_kg")
    val estimatedKg: Double,
    
    @SerializedName("requested_amount")
    val requestedAmount: Double,
    
    @SerializedName("reference_price")
    val referencePrice: Double
)

data class AdvanceRequestResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("message")
    val message: String
)
