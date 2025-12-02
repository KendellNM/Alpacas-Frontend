package com.alpaca.knm.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

/**
 * Activity de Solicitud de Anticipo
 * Diseño basado en el prototipo HTML
 */
class RequestActivity : AppCompatActivity() {

    // Views
    private var etKg: TextInputEditText? = null
    private var etMonto: TextInputEditText? = null
    private lateinit var tvCalcAmount: TextView
    private lateinit var tvReferencePrice: TextView
    private var labelMonto: TextView? = null
    private lateinit var btnSend: MaterialButton
    
    // Navigation
    private var btnBack: ImageButton? = null
    private var navHome: ImageView? = null
    private var navHomeContainer: View? = null
    private var navWalletContainer: View? = null
    private var navUserContainer: View? = null
    
    // ViewModel
    private lateinit var viewModel: RequestViewModel
    
    // Constants
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
        val appContainer = (application as AlpacaApplication).appContainer
        
        val factory = RequestViewModelFactory(
            appContainer.createAdvanceRequestUseCase
        )
        viewModel = ViewModelProvider(this, factory)[RequestViewModel::class.java]
    }

    private fun initializeViews() {
        // Try to find TextInputEditText first, fallback to EditText
        etKg = findViewById(R.id.etKg)
        etMonto = findViewById(R.id.etMonto)
        
        tvCalcAmount = findViewById(R.id.tvCalcAmount)
        tvReferencePrice = findViewById(R.id.tvReferencePrice)
        labelMonto = findViewById(R.id.labelMonto)
        btnSend = findViewById(R.id.btnSend)
        
        // Navigation
        btnBack = findViewById(R.id.btnBack)
        navHome = findViewById(R.id.navHome)
        navHomeContainer = findViewById(R.id.navHomeContainer)
        navWalletContainer = findViewById(R.id.navWalletContainer)
        navUserContainer = findViewById(R.id.navUserContainer)
        
        // Mostrar precio de referencia
        tvReferencePrice.text = "Precio Ref. Alpaca: S/ ${String.format("%.2f", PRECIO_REF_KG)} / Kg"
    }

    private fun setupListeners() {
        // Cálculo automático al escribir Kg
        etKg?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateAnticipo()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        
        btnSend.setOnClickListener {
            val kg = etKg?.text.toString()
            val amount = etMonto?.text.toString()
            
            if (kg.isEmpty() || amount.isEmpty()) {
                Toast.makeText(this, "Debe ingresar valores válidos para Kg y Monto.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            
            val kgValue = kg.toDoubleOrNull() ?: 0.0
            val amountValue = amount.toDoubleOrNull() ?: 0.0
            
            if (kgValue <= 0 || amountValue <= 0) {
                Toast.makeText(this, "Debe ingresar valores válidos para Kg y Monto.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            
            viewModel.onSendClicked(kg, amount)
        }
        
        // Back button
        btnBack?.setOnClickListener {
            finish()
        }
        
        // Navigation
        navHome?.setOnClickListener {
            navigateToDashboard()
        }
        
        navHomeContainer?.setOnClickListener {
            navigateToDashboard()
        }
        
        navWalletContainer?.setOnClickListener {
            // Already on request screen
        }
        
        navUserContainer?.setOnClickListener {
            navigateToProfile()
        }
    }
    
    private fun calculateAnticipo() {
        val kgText = etKg?.text.toString()
        val kg = kgText.toDoubleOrNull() ?: 0.0
        
        val montoSugerido = kg * PRECIO_REF_KG * PORCENTAJE_ANTICIPO
        
        tvCalcAmount.text = String.format(Locale.getDefault(), "S/ %.2f", montoSugerido)
        labelMonto?.text = "Monto Solicitado (Máx: S/ ${String.format("%.2f", montoSugerido)})"
        
        // Auto-fill monto
        etMonto?.setText(String.format("%.2f", montoSugerido))
        
        // Notify ViewModel
        viewModel.onKgChanged(kg)
    }
    
    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
    
    private fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            handleUiState(state)
        }
        
        viewModel.calculatedAmount.observe(this) { amount ->
            // Already handled in calculateAnticipo
        }
        
        viewModel.navigationEvent.observe(this) { event ->
            handleNavigationEvent(event)
        }
    }

    private fun handleUiState(state: RequestUiState) {
        when (state) {
            is RequestUiState.Idle -> {
                showLoading(false)
            }
            is RequestUiState.Loading -> {
                showLoading(true)
            }
            is RequestUiState.Success -> {
                showLoading(false)
                Toast.makeText(
                    this,
                    "Tu solicitud de S/ ${etMonto?.text} ha sido enviada con éxito y está PENDIENTE de revisión.",
                    Toast.LENGTH_LONG
                ).show()
                // Navigate back after success
                finish()
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
        when (event) {
            is RequestNavigationEvent.NavigateBack -> {
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        btnSend.isEnabled = !isLoading
        etKg?.isEnabled = !isLoading
        etMonto?.isEnabled = !isLoading
        
        if (isLoading) {
            btnSend.text = "Enviando..."
        } else {
            btnSend.text = "Enviar Solicitud"
        }
    }
}
