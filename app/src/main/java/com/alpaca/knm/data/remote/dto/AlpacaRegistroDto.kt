package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para registro simplificado de alpacas
 */
data class AlpacaRegistroRequest(
    @SerializedName("ganadero_id")
    val ganaderoId: Int,
    
    @SerializedName("raza")
    val raza: String,
    
    @SerializedName("cantidad")
    val cantidad: Int,
    
    @SerializedName("adultos")
    val adultos: Int
)

data class AlpacaRegistroResponse(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("ganadero_id")
    val ganaderoId: Int,
    
    @SerializedName("raza")
    val raza: String,
    
    @SerializedName("cantidad")
    val cantidad: Int,
    
    @SerializedName("adultos")
    val adultos: Int,
    
    @SerializedName("crias")
    val crias: Int,
    
    @SerializedName("fecha_registro")
    val fechaRegistro: String?
)

data class RazaInfo(
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("descripcion")
    val descripcion: String
)
