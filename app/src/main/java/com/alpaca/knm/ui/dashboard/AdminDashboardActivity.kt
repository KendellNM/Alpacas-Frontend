package com.alpaca.knm.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alpaca.knm.AlpacaApplication
import com.alpaca.knm.R
import com.alpaca.knm.ui.ganaderos.GanaderosActivity
import com.alpaca.knm.ui.login.LoginActivity
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var cardGanaderos: MaterialCardView
    private lateinit var cardSolicitudes: MaterialCardView
    private lateinit var cardReportes: MaterialCardView
    private var cardAlpacasRegistros: MaterialCardView? = null
    private var navGanaderosContainer: View? = null
    private var navSolicitudesContainer: View? = null
    private var navUsersContainer: View? = null
    private var navLogoutContainer: View? = null
    private var layoutSolicitudesPendientes: View? = null
    private var layoutGanaderosCount: View? = null
    private var tvPendingCount: TextView? = null
    private var tvGanaderosCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        initializeViews()
        setupListeners()
        loadDashboardData()
    }
    
    override fun onResume() {
        super.onResume()
        loadDashboardData()
    }

    private fun initializeViews() {
        cardGanaderos = findViewById(R.id.cardGanaderos)
        cardReportes = findViewById(R.id.cardReportes)
        cardAlpacasRegistros = findViewById(R.id.cardAlpacasRegistros)
        cardSolicitudes = findViewById(R.id.cardSolicitudes)
        navGanaderosContainer = findViewById(R.id.navGanaderosContainer)
        navSolicitudesContainer = findViewById(R.id.navSolicitudesContainer)
        navUsersContainer = findViewById(R.id.navUsersContainer)
        navLogoutContainer = findViewById(R.id.navLogoutContainer)
        layoutSolicitudesPendientes = findViewById(R.id.layoutSolicitudesPendientes)
        layoutGanaderosCount = findViewById(R.id.layoutGanaderosCount)
        tvPendingCount = findViewById(R.id.tvPendingCount)
        tvGanaderosCount = findViewById(R.id.tvGanaderosCount)
    }

    private fun setupListeners() {
        cardGanaderos.setOnClickListener { navigateToUsers() }
        cardReportes.setOnClickListener { navigateToSolicitudes() }
        cardAlpacasRegistros?.setOnClickListener { navigateToAlpacasRegistros() }
        navGanaderosContainer?.setOnClickListener { navigateToGanaderos() }
        navSolicitudesContainer?.setOnClickListener { navigateToSolicitudes() }
        navUsersContainer?.setOnClickListener { navigateToUsers() }
        navLogoutContainer?.setOnClickListener { logout() }
        layoutSolicitudesPendientes?.setOnClickListener { navigateToSolicitudes() }
        layoutGanaderosCount?.setOnClickListener { navigateToGanaderos() }
    }

    private fun navigateToGanaderos() {
        startActivity(Intent(this, GanaderosActivity::class.java))
    }
    
    private fun navigateToUsers() {
        startActivity(Intent(this, com.alpaca.knm.ui.users.UsersActivity::class.java))
    }
    
    private fun navigateToSolicitudes() {
        startActivity(Intent(this, com.alpaca.knm.ui.solicitudes.SolicitudesActivity::class.java))
    }
    
    private fun navigateToAlpacasRegistros() {
        startActivity(Intent(this, com.alpaca.knm.ui.alpacas.AlpacasRegistrosActivity::class.java))
    }
    
    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun loadDashboardData() {
        val token = com.alpaca.knm.data.local.SessionManager.getToken(this)
        if (token.isNullOrEmpty()) return
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = (application as AlpacaApplication).appContainer.dashboardApiService
                val response = apiService.getStats("Bearer $token")
                if (response.isSuccessful) {
                    val stats = response.body()
                    withContext(Dispatchers.Main) {
                        tvPendingCount?.text = stats?.pendingRequests?.toString() ?: "0"
                        tvGanaderosCount?.text = stats?.ganaderosCount?.toString() ?: "0"
                        findViewById<TextView>(R.id.tvBadge)?.text = stats?.pendingRequests?.toString() ?: "0"
                    }
                }
            } catch (_: Exception) { }
        }
    }
}
