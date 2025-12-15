package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("id")
    val id: Int,
    
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
