package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para respuesta de solicitud
 */
data class SolicitudResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("ganadero_id")
    val ganaderoId: String,
    
    @SerializedName("ganadero_name")
    val ganaderoName: String,
    
    @SerializedName("estimated_kg")
    val estimatedKg: Double,
    
    @SerializedName("requested_amount")
    val requestedAmount: Double,
    
    @SerializedName("reference_price")
    val referencePrice: Double,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("created_at")
    val createdAt: String,
    
    @SerializedName("updated_at")
    val updatedAt: String?,
    
    @SerializedName("reviewed_by")
    val reviewedBy: String?,
    
    @SerializedName("review_notes")
    val reviewNotes: String?
)

/**
 * DTO para aprobar/rechazar solicitud
 */
data class ReviewSolicitudRequest(
    @SerializedName("notes")
    val notes: String?
)
