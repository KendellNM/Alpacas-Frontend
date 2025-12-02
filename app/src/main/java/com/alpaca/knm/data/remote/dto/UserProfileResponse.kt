package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta del perfil de usuario
 */
data class UserProfileResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("first_name")
    val firstName: String,
    
    @SerializedName("last_name")
    val lastName: String,
    
    @SerializedName("gender")
    val gender: String,
    
    @SerializedName("location")
    val location: String,
    
    @SerializedName("avatar_url")
    val avatarUrl: String?
)
