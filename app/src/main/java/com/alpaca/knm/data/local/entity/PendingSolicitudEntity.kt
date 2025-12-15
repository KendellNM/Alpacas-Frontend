package com.alpaca.knm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_solicitudes")
data class PendingSolicitudEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val ganaderoId: Long,
    val kilograms: Double,
    val totalAmount: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val retryCount: Int = 0,
    val lastError: String? = null
)
