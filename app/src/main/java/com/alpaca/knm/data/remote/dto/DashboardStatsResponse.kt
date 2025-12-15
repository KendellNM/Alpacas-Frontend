package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DashboardStatsResponse(
    @SerializedName("alpacas_count")
    val alpacasCount: Int,
    
    @SerializedName("pending_requests")
    val pendingRequests: Int,
    
    @SerializedName("total_advances")
    val totalAdvances: Double,
    
    @SerializedName("ganaderos_count")
    val ganaderosCount: Int = 0
)
