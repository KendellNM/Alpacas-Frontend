package com.alpaca.knm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alpaca.knm.domain.model.Ganadero

/**
 * Entidad Room para Ganadero
 */
@Entity(tableName = "ganaderos")
data class GanaderoEntity(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
    val dni: String,
    val phone: String,
    val email: String,
    val address: String,
    val district: String,
    val province: String,
    val department: String,
    val alpacasCount: Int,
    val createdAt: String?,
    val syncedAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Ganadero {
        return Ganadero(
            id = id,
            firstName = firstName,
            lastName = lastName,
            dni = dni,
            phone = phone,
            email = email,
            address = address,
            district = district,
            province = province,
            department = department,
            alpacasCount = alpacasCount,
            createdAt = createdAt
        )
    }
    
    companion object {
        fun fromDomain(ganadero: Ganadero): GanaderoEntity {
            return GanaderoEntity(
                id = ganadero.id,
                firstName = ganadero.firstName,
                lastName = ganadero.lastName,
                dni = ganadero.dni,
                phone = ganadero.phone,
                email = ganadero.email,
                address = ganadero.address,
                district = ganadero.district,
                province = ganadero.province,
                department = ganadero.department,
                alpacasCount = ganadero.alpacasCount,
                createdAt = ganadero.createdAt
            )
        }
    }
}
