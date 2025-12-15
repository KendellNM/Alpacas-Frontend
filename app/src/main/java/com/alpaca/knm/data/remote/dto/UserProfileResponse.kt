package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

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
    val avatarUrl: String?,
    
    @SerializedName("birth_date")
    val birthDate: String? = null,
    
    @SerializedName("alpacas_count")
    val alpacasCount: Int? = null
)
