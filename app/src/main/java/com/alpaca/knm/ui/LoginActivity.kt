package com.alpaca.knm.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.AlpacaApplication
import com.alpaca.knm.MainActivity
import com.alpaca.knm.R
import com.alpaca.knm.presentation.login.LoginUiState
import com.alpaca.knm.presentation.login.LoginViewModel
import com.alpaca.knm.presentation.login.LoginViewModelFactory
import com.alpaca.knm.presentation.login.NavigationEvent
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Activity de Login - Capa de Presentación
 * Responsable únicamente de la UI y delegación al ViewModel
 */
class LoginActivity : AppCompatActivity() {

    // Views
    private lateinit var tilUsername: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var progressIndicator: CircularProgressIndicator
    
    // ViewModel
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
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
        
        val factory = LoginViewModelFactory(
            appContainer.loginUseCase,
            appContainer.validateCredentialsUseCase
        )
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    /**
     * Inicializa las vistas
     */
    private fun initializeViews() {
        tilUsername = findViewById(R.id.tilUsername)
        tilPassword = findViewById(R.id.tilPassword)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        
        // Crear progress indicator programáticamente si no existe en el layout
        progressIndicator = CircularProgressIndicator(this).apply {
            visibility = View.GONE
        }
    }

    /**
     * Configura los listeners
     */
    private fun setupListeners() {
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            viewModel.onLoginClicked(username, password)
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
    private fun handleUiState(state: LoginUiState) {
        when (state) {
            is LoginUiState.Idle -> {
                showLoading(false)
                clearErrors()
            }
            is LoginUiState.Loading -> {
                showLoading(true)
                clearErrors()
            }
            is LoginUiState.Success -> {
                showLoading(false)
                showSuccessMessage(state.user.username)
            }
            is LoginUiState.Error -> {
                showLoading(false)
                showErrorMessage(state.message)
                viewModel.onErrorShown()
            }
            is LoginUiState.ValidationError -> {
                showLoading(false)
                showValidationErrors(state.usernameError, state.passwordError)
            }
        }
    }

    /**
     * Maneja los eventos de navegación
     */
    private fun handleNavigationEvent(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.NavigateToHome -> {
                navigateToHome(event.user.username)
            }
        }
    }

    /**
     * Muestra u oculta el indicador de carga
     */
    private fun showLoading(isLoading: Boolean) {
        btnLogin.isEnabled = !isLoading
        etUsername.isEnabled = !isLoading
        etPassword.isEnabled = !isLoading
        
        if (isLoading) {
            btnLogin.text = ""
            btnLogin.icon = progressIndicator.indeterminateDrawable
        } else {
            btnLogin.text = getString(R.string.login_button)
            btnLogin.icon = null
        }
    }

    /**
     * Limpia los errores de validación
     */
    private fun clearErrors() {
        tilUsername.error = null
        tilPassword.error = null
    }

    /**
     * Muestra errores de validación
     */
    private fun showValidationErrors(usernameError: String?, passwordError: String?) {
        tilUsername.error = usernameError
        tilPassword.error = passwordError
    }

    /**
     * Muestra mensaje de éxito
     */
    private fun showSuccessMessage(username: String) {
        val message = getString(R.string.login_welcome_message, username)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Muestra mensaje de error
     */
    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Navega a la pantalla principal según el rol del usuario
     */
    private fun navigateToHome(username: String) {
        val user = (application as AlpacaApplication).appContainer.authRepository.getCurrentUser()
        
        val intent = when (user?.role) {
            "ROLE_ADMIN" -> Intent(this, AdminDashboardActivity::class.java)
            "ROLE_GANADERO" -> Intent(this, DashboardActivity::class.java)
            else -> {
                showErrorMessage("Rol no reconocido")
                return
            }
        }
        
        intent.putExtra("username", username)
        startActivity(intent)
        finish()
    }
}
