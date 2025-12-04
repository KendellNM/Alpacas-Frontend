package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta de login
 */
data class LoginResponse(
    @SerializedName("username")
    val username: String,
    
    @SerializedName("token")
    val token: String,
    
    @SerializedName("role")
    val role: String,
    
    @SerializedName("token_type")
    val tokenType: String?,
    
    @SerializedName("expires_in")
    val expiresIn: Int?
)
