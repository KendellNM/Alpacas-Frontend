package com.alpaca.knm.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
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
import com.alpaca.knm.ui.login.LoginActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etSexo: EditText
    private lateinit var etNumeroAlpacas: EditText
    private lateinit var btnLogout: com.google.android.material.button.MaterialButton
    private var navHomeContainer: android.view.View? = null
    private var navAlpacasContainer: android.view.View? = null
    private var navWalletContainer: android.view.View? = null
    private var navUserContainer: android.view.View? = null
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
        val factory = ProfileViewModelFactory(appContainer.getUserProfileUseCase, appContainer.logoutUseCase)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }

    private fun initializeViews() {
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etSexo = findViewById(R.id.etSexo)
        etNumeroAlpacas = findViewById(R.id.etNumeroAlpacas)
        btnLogout = findViewById(R.id.btnLogout)
        navHomeContainer = findViewById(R.id.navHomeContainer)
        navAlpacasContainer = findViewById(R.id.navAlpacasContainer)
        navWalletContainer = findViewById(R.id.navWalletContainer)
        navUserContainer = findViewById(R.id.navUserContainer)
    }

    private fun setupListeners() {
        navHomeContainer?.setOnClickListener { viewModel.onHomeClicked() }
        navAlpacasContainer?.setOnClickListener { startActivity(Intent(this, MisAlpacasActivity::class.java)) }
        navWalletContainer?.setOnClickListener { viewModel.onWalletClicked() }
        navUserContainer?.setOnClickListener { }
        btnLogout.setOnClickListener { viewModel.onLogoutClicked() }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { handleUiState(it) }
        viewModel.navigationEvent.observe(this) { handleNavigationEvent(it) }
    }

    private fun handleUiState(state: ProfileUiState) {
        when (state) {
            is ProfileUiState.Loading -> { }
            is ProfileUiState.Success -> updateUI(state.profile)
            is ProfileUiState.Error -> Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun handleNavigationEvent(event: ProfileNavigationEvent) {
        when (event) {
            is ProfileNavigationEvent.NavigateToHome -> finish()
            is ProfileNavigationEvent.NavigateToWallet -> Toast.makeText(this, getString(R.string.profile_go_to_wallet), Toast.LENGTH_SHORT).show()
            is ProfileNavigationEvent.NavigateToLogin -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun updateUI(profile: com.alpaca.knm.domain.model.UserProfile) {
        etNombre.setText(profile.firstName)
        etApellido.setText(profile.lastName)
        etSexo.setText(profile.gender)
        etNumeroAlpacas.setText(profile.alpacasCount?.toString() ?: "0")
    }

    private fun disableEditing() {
        arrayOf(etNombre, etApellido, etSexo, etNumeroAlpacas).forEach {
            it.isFocusable = false
            it.isClickable = false
            it.isCursorVisible = false
        }
    }
}
