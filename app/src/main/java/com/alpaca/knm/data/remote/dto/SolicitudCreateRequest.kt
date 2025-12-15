package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SolicitudCreateRequest(
    @SerializedName("ganadero_id")
    val ganaderoId: Long,
    @SerializedName("kilograms")
    val kilograms: Double,
    @SerializedName("total_amount")
    val totalAmount: Double
)
