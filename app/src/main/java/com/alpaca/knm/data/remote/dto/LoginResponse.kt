package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta de login
 */
data class LoginResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("username")
    val username: String,
    
    @SerializedName("email")
    val email: String?,
    
    @SerializedName("token")
    val token: String?,
    
    @SerializedName("role")
    val role: String?
)
