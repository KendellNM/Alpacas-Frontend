package com.alpaca.knm.data.remote.dto

import com.alpaca.knm.domain.model.Alpaca
import com.alpaca.knm.domain.model.AlpacaEstado
import com.alpaca.knm.domain.model.AlpacaRaza
import com.alpaca.knm.domain.model.AlpacaSexo
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AlpacaDto(
    @SerializedName("id") val id: Long,
    @SerializedName("ganadero_id") val ganaderoId: Long,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("raza") val raza: String,
    @SerializedName("color") val color: String,
    @SerializedName("edad") val edad: Int,
    @SerializedName("peso") val peso: Double,
    @SerializedName("sexo") val sexo: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("fecha_registro") val fechaRegistro: String,
    @SerializedName("observaciones") val observaciones: String?
) {
    fun toDomain(): Alpaca {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return Alpaca(
            id = id,
            ganaderoId = ganaderoId,
            nombre = nombre,
            raza = when (raza.uppercase()) {
                "HUACAYA" -> AlpacaRaza.HUACAYA
                "SURI" -> AlpacaRaza.SURI
                else -> AlpacaRaza.HUACAYA
            },
            color = color,
            edad = edad,
            peso = peso,
            sexo = when (sexo.uppercase()) {
                "MACHO" -> AlpacaSexo.MACHO
                "HEMBRA" -> AlpacaSexo.HEMBRA
                else -> AlpacaSexo.MACHO
            },
            estado = when (estado.uppercase()) {
                "ACTIVO" -> AlpacaEstado.ACTIVO
                "VENDIDO" -> AlpacaEstado.VENDIDO
                "FALLECIDO" -> AlpacaEstado.FALLECIDO
                else -> AlpacaEstado.ACTIVO
            },
            fechaRegistro = try { dateFormat.parse(fechaRegistro) ?: Date() } catch (e: Exception) { Date() },
            observaciones = observaciones
        )
    }
}

data class AlpacaCreateRequest(
    @SerializedName("ganadero_id") val ganaderoId: Long,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("raza") val raza: String,
    @SerializedName("color") val color: String,
    @SerializedName("edad") val edad: Int,
    @SerializedName("peso") val peso: Double,
    @SerializedName("sexo") val sexo: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("observaciones") val observaciones: String?
)
