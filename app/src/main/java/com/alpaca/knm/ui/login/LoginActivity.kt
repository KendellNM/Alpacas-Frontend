package com.alpaca.knm.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.AlpacaApplication
import com.alpaca.knm.R
import com.alpaca.knm.data.local.SessionManager
import com.alpaca.knm.data.remote.RenderWakeUpInterceptor
import com.alpaca.knm.presentation.login.LoginUiState
import com.alpaca.knm.presentation.login.LoginViewModel
import com.alpaca.knm.presentation.login.LoginViewModelFactory
import com.alpaca.knm.presentation.login.NavigationEvent
import com.alpaca.knm.ui.dashboard.AdminDashboardActivity
import com.alpaca.knm.ui.dashboard.DashboardActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var tilUsername: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initializeViewModel()
        initializeViews()
        setupListeners()
        observeViewModel()
    }

    private fun initializeViewModel() {
        val appContainer = (application as AlpacaApplication).appContainer
        val factory = LoginViewModelFactory(
            appContainer.loginUseCase,
            appContainer.validateCredentialsUseCase
        )
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun initializeViews() {
        tilUsername = findViewById(R.id.tilUsername)
        tilPassword = findViewById(R.id.tilPassword)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressIndicator = CircularProgressIndicator(this).apply { visibility = View.GONE }
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            RenderWakeUpInterceptor.reset()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            viewModel.onLoginClicked(username, password)
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { handleUiState(it) }
        viewModel.navigationEvent.observe(this) { handleNavigationEvent(it) }
    }

    private fun handleUiState(state: LoginUiState) {
        when (state) {
            is LoginUiState.Idle -> { showLoading(false); clearErrors() }
            is LoginUiState.Loading -> { showLoading(true); clearErrors() }
            is LoginUiState.Success -> { showLoading(false); showSuccessMessage(state.user.username) }
            is LoginUiState.Error -> { showLoading(false); showErrorMessage(state.message); viewModel.onErrorShown() }
            is LoginUiState.ValidationError -> { showLoading(false); showValidationErrors(state.usernameError, state.passwordError) }
        }
    }

    private fun handleNavigationEvent(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.NavigateToHome -> navigateToHome(event.user.username)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        btnLogin.isEnabled = !isLoading
        etUsername.isEnabled = !isLoading
        etPassword.isEnabled = !isLoading
        if (isLoading) {
            if (RenderWakeUpInterceptor.isRenderWakingUp) {
                btnLogin.text = "Levantando servidor..."
                Toast.makeText(this, "Levantando servidor en Render... Esto puede tomar unos minutos.", Toast.LENGTH_LONG).show()
            } else {
                btnLogin.text = "Iniciando sesion..."
            }
            btnLogin.icon = progressIndicator.indeterminateDrawable
        } else {
            btnLogin.text = getString(R.string.login_button)
            btnLogin.icon = null
        }
    }

    private fun clearErrors() {
        tilUsername.error = null
        tilPassword.error = null
    }

    private fun showValidationErrors(usernameError: String?, passwordError: String?) {
        tilUsername.error = usernameError
        tilPassword.error = passwordError
    }

    private fun showSuccessMessage(username: String) {
        Toast.makeText(this, getString(R.string.login_welcome_message, username), Toast.LENGTH_SHORT).show()
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun navigateToHome(username: String) {
        val appContainer = (application as AlpacaApplication).appContainer
        val user = appContainer.authRepository.getCurrentUser()
        val token = appContainer.authRepository.getToken()
        if (user != null && token != null) {
            SessionManager.saveSession(this, token, user.id.toIntOrNull() ?: 0, user.username, user.role ?: "ROLE_GANADERO")
        }
        val intent = when (user?.role) {
            "ROLE_ADMIN" -> Intent(this, AdminDashboardActivity::class.java)
            "ROLE_GANADERO" -> Intent(this, DashboardActivity::class.java)
            else -> { showErrorMessage("Rol no reconocido"); return }
        }
        intent.putExtra("username", username)
        startActivity(intent)
        finish()
    }
}
