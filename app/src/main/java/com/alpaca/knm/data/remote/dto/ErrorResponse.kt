package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("timestamp")
    val timestamp: String? = null,
    
    @SerializedName("status")
    val status: Int,
    
    @SerializedName("error")
    val error: String,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("path")
    val path: String? = null,
    
    @SerializedName("errors")
    val errors: List<FieldError>? = null
)

data class FieldError(
    @SerializedName("field")
    val field: String,
    
    @SerializedName("message")
    val message: String
)
