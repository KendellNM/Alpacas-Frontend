package com.alpaca.knm

import android.app.Application
import com.alpaca.knm.di.AppContainer

/**
 * Clase Application personalizada
 * Inicializa el contenedor de dependencias
 */
class AlpacaApplication : Application() {
    
    lateinit var appContainer: AppContainer
        private set
    
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
