package com.alpaca.knm.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.AlpacaApplication
import com.alpaca.knm.R
import com.alpaca.knm.presentation.dashboard.DashboardNavigationEvent
import com.alpaca.knm.presentation.dashboard.DashboardUiState
import com.alpaca.knm.presentation.dashboard.DashboardViewModel
import com.alpaca.knm.presentation.dashboard.DashboardViewModelFactory
import com.google.android.material.button.MaterialButton

/**
 * Activity de Dashboard - Pantalla principal para ROLE_GANADERO
 * Diseño basado en el prototipo HTML
 */
class DashboardActivity : AppCompatActivity() {

    // Views
    private lateinit var tvWelcomeUser: TextView
    private lateinit var tvAlpacaCount: TextView
    private lateinit var btnNewRequest: MaterialButton
    
    // Navigation containers
    private var navHomeContainer: View? = null
    private var navAlpacasContainer: View? = null
    private var navWalletContainer: View? = null
    private var navUserContainer: View? = null
    
    // Legacy navigation (for compatibility)
    private var navHome: ImageView? = null
    private var navWallet: ImageView? = null
    private var navUser: ImageView? = null
    
    // ViewModel
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        
        initializeViewModel()
        initializeViews()
        setupListeners()
        observeViewModel()
    }

    /**
     * Inicializa el ViewModel con sus dependencias desde el AppContainer
     */
    private fun initializeViewModel() {
        val appContainer = (application as AlpacaApplication).appContainer
        
        val factory = DashboardViewModelFactory(
            appContainer.getDashboardStatsUseCase,
            appContainer.getCurrentUserUseCase
        )
        viewModel = ViewModelProvider(this, factory)[DashboardViewModel::class.java]
    }

    /**
     * Inicializa las vistas
     */
    private fun initializeViews() {
        tvWelcomeUser = findViewById(R.id.tvWelcomeUser)
        tvAlpacaCount = findViewById(R.id.tvAlpacaCount)
        btnNewRequest = findViewById(R.id.btnNewRequest)
        
        // Try new navigation containers first
        navHomeContainer = findViewById(R.id.navHomeContainer)
        navAlpacasContainer = findViewById(R.id.navAlpacasContainer)
        navWalletContainer = findViewById(R.id.navWalletContainer)
        navUserContainer = findViewById(R.id.navUserContainer)
        
        // Fallback to legacy navigation
        navHome = findViewById(R.id.navHome)
        navWallet = findViewById(R.id.navWallet)
        navUser = findViewById(R.id.navUser)
    }

    /**
     * Configura los listeners
     */
    private fun setupListeners() {
        btnNewRequest.setOnClickListener {
            viewModel.onNewRequestClicked()
        }
        
        // New navigation containers
        navHomeContainer?.setOnClickListener {
            // Ya estamos en home
        }
        
        navAlpacasContainer?.setOnClickListener {
            navigateToAlpacaRegistro()
        }
        
        navWalletContainer?.setOnClickListener {
            viewModel.onWalletClicked()
        }
        
        navUserContainer?.setOnClickListener {
            viewModel.onProfileClicked()
        }
        
        // Legacy navigation (fallback)
        navHome?.setOnClickListener {
            // Ya estamos en home
        }
        
        navWallet?.setOnClickListener {
            viewModel.onWalletClicked()
        }
        
        navUser?.setOnClickListener {
            viewModel.onProfileClicked()
        }
    }

    /**
     * Observa los cambios del ViewModel
     */
    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            handleUiState(state)
        }
        
        viewModel.navigationEvent.observe(this) { event ->
            handleNavigationEvent(event)
        }
    }

    /**
     * Maneja los diferentes estados de la UI
     */
    private fun handleUiState(state: DashboardUiState) {
        when (state) {
            is DashboardUiState.Loading -> {
                showLoading(true)
            }
            is DashboardUiState.Success -> {
                showLoading(false)
                updateUI(state.username, state.stats.alpacasCount)
            }
            is DashboardUiState.Error -> {
                showLoading(false)
                showError(state.message)
            }
        }
    }

    /**
     * Maneja los eventos de navegación
     */
    private fun handleNavigationEvent(event: DashboardNavigationEvent) {
        when (event) {
            is DashboardNavigationEvent.NavigateToNewRequest -> {
                navigateToRequest()
            }
            is DashboardNavigationEvent.NavigateToWallet -> {
                // Navigate to request screen (same as wallet in this context)
                navigateToRequest()
            }
            is DashboardNavigationEvent.NavigateToProfile -> {
                navigateToProfile()
            }
        }
    }
    
    private fun navigateToRequest() {
        val intent = Intent(this, RequestActivity::class.java)
        startActivity(intent)
    }
    
    private fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
    
    private fun navigateToAlpacaRegistro() {
        val intent = Intent(this, com.alpaca.knm.ui.alpacas.MisAlpacasActivity::class.java)
        startActivity(intent)
    }

    /**
     * Actualiza la UI con los datos
     */
    private fun updateUI(username: String, alpacasCount: Int) {
        tvWelcomeUser.text = "Hola, $username"
        tvAlpacaCount.text = alpacasCount.toString()
    }

    /**
     * Muestra u oculta el indicador de carga
     */
    private fun showLoading(isLoading: Boolean) {
        btnNewRequest.isEnabled = !isLoading
    }

    /**
     * Muestra un mensaje de error
     */
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Muestra un mensaje informativo
     */
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
