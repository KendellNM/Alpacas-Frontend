package com.alpaca.knm.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alpaca.knm.R
import com.google.android.material.card.MaterialCardView

/**
 * Dashboard principal para ROLE_ADMIN (Operador de Campo)
 * Diseño basado en el prototipo HTML
 */
class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var cardGanaderos: MaterialCardView
    private lateinit var cardSolicitudes: MaterialCardView
    private lateinit var cardReportes: MaterialCardView
    
    // Navigation containers
    private var navGanaderosContainer: View? = null
    private var navSolicitudesContainer: View? = null
    private var navUsersContainer: View? = null
    private var navLogoutContainer: View? = null
    
    // Task counters
    private var layoutSolicitudesPendientes: View? = null
    private var layoutGanaderosCount: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        
        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        // Cards
        cardGanaderos = findViewById(R.id.cardGanaderos)
        cardReportes = findViewById(R.id.cardReportes)
        
        // Hidden cards for compatibility
        cardSolicitudes = findViewById(R.id.cardSolicitudes)
        
        // Try to find new navigation elements
        navGanaderosContainer = findViewById(R.id.navGanaderosContainer)
        navSolicitudesContainer = findViewById(R.id.navSolicitudesContainer)
        navUsersContainer = findViewById(R.id.navUsersContainer)
        navLogoutContainer = findViewById(R.id.navLogoutContainer)
        
        // Task counters
        layoutSolicitudesPendientes = findViewById(R.id.layoutSolicitudesPendientes)
        layoutGanaderosCount = findViewById(R.id.layoutGanaderosCount)
    }

    private fun setupListeners() {
        // Card: Registrar Ganadero
        cardGanaderos.setOnClickListener {
            navigateToGanaderoForm()
        }
        
        // Card: Registrar Cosecha (placeholder)
        cardReportes.setOnClickListener {
            showComingSoon("Registrar Cosecha")
        }
        
        // Bottom Navigation: Ganaderos
        navGanaderosContainer?.setOnClickListener {
            navigateToGanaderos()
        }
        
        // Bottom Navigation: Solicitudes/Anticipos
        navSolicitudesContainer?.setOnClickListener {
            navigateToSolicitudes()
        }
        
        // Bottom Navigation: Users
        navUsersContainer?.setOnClickListener {
            navigateToUsers()
        }
        
        // Bottom Navigation: Logout
        navLogoutContainer?.setOnClickListener {
            logout()
        }
        
        // Task counters click
        layoutSolicitudesPendientes?.setOnClickListener {
            navigateToSolicitudes()
        }
        
        layoutGanaderosCount?.setOnClickListener {
            navigateToGanaderos()
        }
    }

    private fun navigateToGanaderos() {
        val intent = Intent(this, GanaderosActivity::class.java)
        startActivity(intent)
    }
    
    private fun navigateToGanaderoForm() {
        val intent = Intent(this, GanaderoFormActivity::class.java)
        startActivity(intent)
    }
    
    private fun navigateToSolicitudes() {
        val intent = Intent(this, com.alpaca.knm.ui.solicitudes.SolicitudesActivity::class.java)
        startActivity(intent)
    }
    
    private fun navigateToAlpacas() {
        val intent = Intent(this, com.alpaca.knm.ui.alpacas.AlpacasActivity::class.java)
        startActivity(intent)
    }
    
    private fun navigateToUsers() {
        val intent = Intent(this, com.alpaca.knm.ui.users.UsersActivity::class.java)
        startActivity(intent)
    }
    
    private fun logout() {
        // Clear session and go back to login
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showComingSoon(feature: String) {
        Toast.makeText(
            this,
            "Funcionalidad: $feature - Próximamente disponible",
            Toast.LENGTH_SHORT
        ).show()
    }
}
