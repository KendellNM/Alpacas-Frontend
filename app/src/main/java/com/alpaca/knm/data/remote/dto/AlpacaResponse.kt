package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para respuesta de alpaca
 */
data class AlpacaResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("breed")
    val breed: String,
    
    @SerializedName("age")
    val age: Int,
    
    @SerializedName("weight")
    val weight: Double,
    
    @SerializedName("color")
    val color: String,
    
    @SerializedName("ganadero_id")
    val ganaderoId: String,
    
    @SerializedName("ganadero_name")
    val ganaderoName: String,
    
    @SerializedName("created_at")
    val createdAt: String?
)

/**
 * DTO para crear/actualizar alpaca
 */
data class AlpacaRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("breed")
    val breed: String,
    
    @SerializedName("age")
    val age: Int,
    
    @SerializedName("weight")
    val weight: Double,
    
    @SerializedName("color")
    val color: String,
    
    @SerializedName("ganadero_id")
    val ganaderoId: String
)
