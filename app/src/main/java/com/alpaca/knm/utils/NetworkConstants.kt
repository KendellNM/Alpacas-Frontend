package com.alpaca.knm.utils

/**
 * Constantes de red y endpoints
 */
object NetworkConstants {
    
    // URL base del backend
    // Para emulador Android: 10.0.2.2 mapea a localhost de tu PC
    // Para dispositivo físico: reemplaza con la IP de tu PC
    const val BASE_URL = "http://192.168.1.5:8000/api/"
    
    // Endpoints
    object Endpoints {
        const val LOGIN = "auth/login"
        const val LOGOUT = "auth/logout"
        const val DASHBOARD_STATS = "dashboard/stats"
        const val PROFILE = "profile"
        const val ADVANCE_REQUEST = "advances/request"
    }
    
    // Timeouts
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
}
