package com.alpaca.knm.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.AlpacaApplication
import com.alpaca.knm.R
import com.alpaca.knm.presentation.profile.ProfileNavigationEvent
import com.alpaca.knm.presentation.profile.ProfileUiState
import com.alpaca.knm.presentation.profile.ProfileViewModel
import com.alpaca.knm.presentation.profile.ProfileViewModelFactory
import com.alpaca.knm.ui.alpacas.MisAlpacasActivity

/**
 * Activity de Perfil
 * Muestra información del usuario (solo lectura)
 */
class ProfileActivity : AppCompatActivity() {

    // Views
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etSexo: EditText
    private lateinit var etUbicacion: EditText
    private lateinit var btnLogout: com.google.android.material.button.MaterialButton
    
    // Navigation
    private var navHomeContainer: android.view.View? = null
    private var navAlpacasContainer: android.view.View? = null
    private var navWalletContainer: android.view.View? = null
    private var navUserContainer: android.view.View? = null
    
    // ViewModel
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        
        initializeViewModel()
        initializeViews()
        setupListeners()
        observeViewModel()
        disableEditing()
    }

    private fun initializeViewModel() {
        val appContainer = (application as AlpacaApplication).appContainer
        
        val factory = ProfileViewModelFactory(
            appContainer.getUserProfileUseCase,
            appContainer.logoutUseCase
        )
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }

    private fun initializeViews() {
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etSexo = findViewById(R.id.etSexo)
        etUbicacion = findViewById(R.id.etUbicacion)
        btnLogout = findViewById(R.id.btnLogout)
        
        // Navigation
        navHomeContainer = findViewById(R.id.navHomeContainer)
        navAlpacasContainer = findViewById(R.id.navAlpacasContainer)
        navWalletContainer = findViewById(R.id.navWalletContainer)
        navUserContainer = findViewById(R.id.navUserContainer)
    }

    private fun setupListeners() {
        navHomeContainer?.setOnClickListener {
            viewModel.onHomeClicked()
        }
        
        navAlpacasContainer?.setOnClickListener {
            navigateToAlpacaRegistro()
        }
        
        navWalletContainer?.setOnClickListener {
            viewModel.onWalletClicked()
        }
        
        navUserContainer?.setOnClickListener {
            // Already on profile screen
        }
        
        btnLogout.setOnClickListener {
            viewModel.onLogoutClicked()
        }
    }
    
    private fun navigateToAlpacaRegistro() {
        val intent = Intent(this, MisAlpacasActivity::class.java)
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            handleUiState(state)
        }
        
        viewModel.navigationEvent.observe(this) { event ->
            handleNavigationEvent(event)
        }
    }

    private fun handleUiState(state: ProfileUiState) {
        when (state) {
            is ProfileUiState.Loading -> {
                // Mostrar loading si es necesario
            }
            is ProfileUiState.Success -> {
                updateUI(state.profile)
            }
            is ProfileUiState.Error -> {
                showError(state.message)
            }
        }
    }

    private fun handleNavigationEvent(event: ProfileNavigationEvent) {
        when (event) {
            is ProfileNavigationEvent.NavigateToHome -> {
                finish()
            }
            is ProfileNavigationEvent.NavigateToWallet -> {
                showMessage(getString(R.string.profile_go_to_wallet))
            }
            is ProfileNavigationEvent.NavigateToLogin -> {
                navigateToLogin()
            }
        }
    }
    
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun updateUI(profile: com.alpaca.knm.domain.model.UserProfile) {
        etNombre.setText(profile.firstName)
        etApellido.setText(profile.lastName)
        etSexo.setText(profile.gender)
        etUbicacion.setText(profile.location)
    }

    /**
     * Deshabilita la edición de los campos (solo lectura)
     */
    private fun disableEditing() {
        val editTexts = arrayOf(etNombre, etApellido, etSexo, etUbicacion)
        
        for (et in editTexts) {
            et.isFocusable = false
            et.isClickable = false
            et.isCursorVisible = false
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
