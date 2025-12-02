package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la petición de login
 */
data class LoginRequest(
    @SerializedName("username")
    val username: String,
    
    @SerializedName("password")
    val password: String
)
