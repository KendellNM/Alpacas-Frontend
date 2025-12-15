package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SolicitudDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("ganaderoId")
    val ganaderoId: Int,
    @SerializedName("ganaderoNombre")
    val ganaderoNombre: String,
    @SerializedName("kilograms")
    val kilograms: Double,
    @SerializedName("totalAmount")
    val totalAmount: Double,
    @SerializedName("status")
    val status: String,
    @SerializedName("requestDate")
    val requestDate: String?,
    @SerializedName("responseDate")
    val responseDate: String?,
    @SerializedName("adminComment")
    val adminComment: String?
)
