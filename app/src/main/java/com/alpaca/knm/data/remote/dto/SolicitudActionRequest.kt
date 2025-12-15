package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SolicitudActionRequest(
    @SerializedName("comment")
    val comment: String?
)
