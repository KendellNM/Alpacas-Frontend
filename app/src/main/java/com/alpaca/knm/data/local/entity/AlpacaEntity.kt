package com.alpaca.knm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alpaca.knm.domain.model.Alpaca
import com.alpaca.knm.domain.model.AlpacaEstado
import com.alpaca.knm.domain.model.AlpacaRaza
import com.alpaca.knm.domain.model.AlpacaSexo
import java.util.Date

/**
 * Entidad Room para Alpaca
 */
@Entity(tableName = "alpacas")
data class AlpacaEntity(
    @PrimaryKey
    val id: Long,
    val ganaderoId: Long,
    val nombre: String,
    val raza: String,
    val color: String,
    val edad: Int,
    val peso: Double,
    val sexo: String,
    val estado: String,
    val fechaRegistro: Long,
    val observaciones: String?,
    val syncedAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Alpaca {
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
            fechaRegistro = Date(fechaRegistro),
            observaciones = observaciones
        )
    }
    
    companion object {
        fun fromDomain(alpaca: Alpaca): AlpacaEntity {
            return AlpacaEntity(
                id = alpaca.id,
                ganaderoId = alpaca.ganaderoId,
                nombre = alpaca.nombre,
                raza = alpaca.raza.name,
                color = alpaca.color,
                edad = alpaca.edad,
                peso = alpaca.peso,
                sexo = alpaca.sexo.name,
                estado = alpaca.estado.name,
                fechaRegistro = alpaca.fechaRegistro.time,
                observaciones = alpaca.observaciones
            )
        }
    }
}
