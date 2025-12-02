package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para respuesta de ganadero
 */
data class GanaderoResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("first_name")
    val firstName: String,
    
    @SerializedName("last_name")
    val lastName: String,
    
    @SerializedName("dni")
    val dni: String,
    
    @SerializedName("phone")
    val phone: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("address")
    val address: String,
    
    @SerializedName("district")
    val district: String,
    
    @SerializedName("province")
    val province: String,
    
    @SerializedName("department")
    val department: String,
    
    @SerializedName("alpacas_count")
    val alpacasCount: Int = 0,
    
    @SerializedName("created_at")
    val createdAt: String?
)

/**
 * DTO para crear/actualizar ganadero
 */
data class GanaderoRequest(
    @SerializedName("first_name")
    val firstName: String,
    
    @SerializedName("last_name")
    val lastName: String,
    
    @SerializedName("dni")
    val dni: String,
    
    @SerializedName("phone")
    val phone: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("address")
    val address: String,
    
    @SerializedName("district")
    val district: String,
    
    @SerializedName("province")
    val province: String,
    
    @SerializedName("department")
    val department: String
)
