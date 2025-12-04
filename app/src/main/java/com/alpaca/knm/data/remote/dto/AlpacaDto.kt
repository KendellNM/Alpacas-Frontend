package com.alpaca.knm.data.remote.dto

import com.alpaca.knm.domain.model.Alpaca
import com.alpaca.knm.domain.model.AlpacaEstado
import com.alpaca.knm.domain.model.AlpacaRaza
import com.alpaca.knm.domain.model.AlpacaSexo
import com.google.gson.annotations.SerializedName
import java.util.Date

data class AlpacaDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("ganadero_id")
    val ganaderoId: Long,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("raza")
    val raza: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("edad")
    val edad: Int,
    @SerializedName("peso")
    val peso: Double,
    @SerializedName("sexo")
    val sexo: String,
    @SerializedName("estado")
    val estado: String,
    @SerializedName("fecha_registro")
    val fechaRegistro: String?,
    @SerializedName("observaciones")
    val observaciones: String?
) {
    fun toDomain(): Alpaca {
        return Alpaca(
            id = id,
            ganaderoId = ganaderoId,
            nombre = nombre,
            raza = try { AlpacaRaza.valueOf(raza.uppercase()) } catch (e: Exception) { AlpacaRaza.HUACAYA },
            color = color,
            edad = edad,
            peso = peso,
            sexo = try { AlpacaSexo.valueOf(sexo.uppercase()) } catch (e: Exception) { AlpacaSexo.MACHO },
            estado = try { AlpacaEstado.valueOf(estado.uppercase()) } catch (e: Exception) { AlpacaEstado.ACTIVO },
            fechaRegistro = Date(),
            observaciones = observaciones
        )
    }
}

data class AlpacaCreateRequest(
    @SerializedName("ganadero_id")
    val ganaderoId: Long,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("raza")
    val raza: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("edad")
    val edad: Int,
    @SerializedName("peso")
    val peso: Double,
    @SerializedName("sexo")
    val sexo: String,
    @SerializedName("estado")
    val estado: String,
    @SerializedName("observaciones")
    val observaciones: String?
)
