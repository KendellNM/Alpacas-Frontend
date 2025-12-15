package com.alpaca.knm.ui.request

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.AlpacaApplication
import com.alpaca.knm.R
import com.alpaca.knm.presentation.request.RequestNavigationEvent
import com.alpaca.knm.presentation.request.RequestUiState
import com.alpaca.knm.presentation.request.RequestViewModel
import com.alpaca.knm.presentation.request.RequestViewModelFactory
import com.alpaca.knm.ui.alpacas.MisAlpacasActivity
import com.alpaca.knm.ui.dashboard.DashboardActivity
import com.alpaca.knm.ui.profile.ProfileActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class RequestActivity : AppCompatActivity() {

    private var etKg: TextInputEditText? = null
    private var etMonto: TextInputEditText? = null
    private lateinit var tvCalcAmount: TextView
    private lateinit var tvReferencePrice: TextView
    private var labelMonto: TextView? = null
    private lateinit var btnSend: MaterialButton
    private var btnBack: ImageButton? = null
    private var navHome: ImageView? = null
    private var navHomeContainer: View? = null
    private var navAlpacasContainer: View? = null
    private var navWalletContainer: View? = null
    private var navUserContainer: View? = null
    private lateinit var viewModel: RequestViewModel
    
    companion object {
        private const val PRECIO_REF_KG = 60.00
        private const val PORCENTAJE_ANTICIPO = 0.60
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)
        initializeViewModel()
        initializeViews()
        setupListeners()
        observeViewModel()
    }

    private fun initializeViewModel() {
        try {
            val appContainer = (application as AlpacaApplication).appContainer
            val currentUser = appContainer.getCurrentUserUseCase()
            val ganaderoId = try { currentUser?.id?.toLongOrNull() ?: 1L } catch (_: Exception) { 1L }
            val factory = RequestViewModelFactory(application, appContainer.createAdvanceRequestUseCase, ganaderoId)
            viewModel = ViewModelProvider(this, factory)[RequestViewModel::class.java]
        } catch (e: Exception) {
            Toast.makeText(this, "Error al inicializar: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun initializeViews() {
        etKg = findViewById(R.id.etKg)
        etMonto = findViewById(R.id.etMonto)
        tvCalcAmount = findViewById(R.id.tvCalcAmount)
        tvReferencePrice = findViewById(R.id.tvReferencePrice)
        labelMonto = findViewById(R.id.labelMonto)
        btnSend = findViewById(R.id.btnSend)
        btnBack = findViewById(R.id.btnBack)
        navHome = findViewById(R.id.navHome)
        navHomeContainer = findViewById(R.id.navHomeContainer)
        navAlpacasContainer = findViewById(R.id.navAlpacasContainer)
        navWalletContainer = findViewById(R.id.navWalletContainer)
        navUserContainer = findViewById(R.id.navUserContainer)
        tvReferencePrice.text = "Precio Ref. Alpaca: S/ ${String.format("%.2f", PRECIO_REF_KG)} / Kg"
    }

    private fun setupListeners() {
        etKg?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { updateMontoReference() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        etKg?.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) autoFillMonto() }
        btnSend.setOnClickListener {
            val kg = etKg?.text.toString().trim()
            val amount = etMonto?.text.toString().trim()
            if (kg.isEmpty() || amount.isEmpty()) {
                Toast.makeText(this, "Ingrese Kg y Monto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val kgNormalized = kg.replace(",", ".")
            val amountNormalized = amount.replace(",", ".")
            val kgValue = kgNormalized.toDoubleOrNull() ?: 0.0
            val amountValue = amountNormalized.toDoubleOrNull() ?: 0.0
            if (kgValue <= 0 || amountValue <= 0) {
                Toast.makeText(this, "Ingrese valores mayores a 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.onSendClicked(kgNormalized, amountNormalized)
        }
        btnBack?.setOnClickListener { finish() }
        navHome?.setOnClickListener { navigateToDashboard() }
        navHomeContainer?.setOnClickListener { navigateToDashboard() }
        navAlpacasContainer?.setOnClickListener { startActivity(Intent(this, MisAlpacasActivity::class.java)) }
        navWalletContainer?.setOnClickListener { }
        navUserContainer?.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }
    }
    
    private fun updateMontoReference() {
        val kg = etKg?.text.toString().toDoubleOrNull() ?: 0.0
        val montoSugerido = kg * PRECIO_REF_KG * PORCENTAJE_ANTICIPO
        tvCalcAmount.text = String.format(Locale.getDefault(), "S/ %.2f", montoSugerido)
        labelMonto?.text = "Monto Solicitado (Max: S/ ${String.format("%.2f", montoSugerido)})"
        viewModel.onKgChanged(kg)
    }
    
    private fun autoFillMonto() {
        val kg = etKg?.text.toString().toDoubleOrNull() ?: 0.0
        if (kg > 0 && etMonto?.text.isNullOrEmpty()) {
            val montoSugerido = kg * PRECIO_REF_KG * PORCENTAJE_ANTICIPO
            etMonto?.setText(String.format(Locale.US, "%.2f", montoSugerido))
        }
    }
    
    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { handleUiState(it) }
        viewModel.calculatedAmount.observe(this) { }
        viewModel.navigationEvent.observe(this) { handleNavigationEvent(it) }
    }

    private fun handleUiState(state: RequestUiState) {
        when (state) {
            is RequestUiState.Idle -> showLoading(false)
            is RequestUiState.Loading -> showLoading(true)
            is RequestUiState.Success -> {
                showLoading(false)
                Toast.makeText(this, "Tu solicitud de S/ ${etMonto?.text} ha sido enviada con exito y esta PENDIENTE de revision.", Toast.LENGTH_LONG).show()
                finish()
            }
            is RequestUiState.SavedOffline -> {
                showLoading(false)
                Toast.makeText(this, "Sin conexion. Solicitud guardada localmente.", Toast.LENGTH_LONG).show()
                finish()
            }
            is RequestUiState.SyncComplete -> {
                showLoading(false)
                Toast.makeText(this, "${state.count} solicitud(es) sincronizada(s)", Toast.LENGTH_SHORT).show()
            }
            is RequestUiState.Error -> {
                showLoading(false)
                Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
            }
            is RequestUiState.ValidationError -> {
                showLoading(false)
                state.kgError?.let { etKg?.error = it }
                state.amountError?.let { etMonto?.error = it }
            }
        }
    }

    private fun handleNavigationEvent(event: RequestNavigationEvent) {
        when (event) { is RequestNavigationEvent.NavigateBack -> finish() }
    }

    private fun showLoading(isLoading: Boolean) {
        btnSend.isEnabled = !isLoading
        etKg?.isEnabled = !isLoading
        etMonto?.isEnabled = !isLoading
        btnSend.text = if (isLoading) "Enviando..." else "Enviar Solicitud"
    }
}
