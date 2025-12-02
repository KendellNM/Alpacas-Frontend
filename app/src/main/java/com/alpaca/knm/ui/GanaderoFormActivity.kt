package com.alpaca.knm.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.AlpacaApplication
import com.alpaca.knm.R
import com.alpaca.knm.domain.model.Ganadero
import com.alpaca.knm.presentation.ganaderos.GanaderoFormNavigationEvent
import com.alpaca.knm.presentation.ganaderos.GanaderoFormUiState
import com.alpaca.knm.presentation.ganaderos.GanaderoFormViewModel
import com.alpaca.knm.presentation.ganaderos.GanaderoFormViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class GanaderoFormActivity : AppCompatActivity() {

    private lateinit var viewModel: GanaderoFormViewModel
    
    private lateinit var tvTitle: TextView
    private lateinit var tilFirstName: TextInputLayout
    private var tilLastName: TextInputLayout? = null
    private lateinit var tilDni: TextInputLayout
    private lateinit var tilPhone: TextInputLayout
    private var tilEmail: TextInputLayout? = null
    private var tilAddress: TextInputLayout? = null
    private lateinit var tilDistrict: TextInputLayout // Comunidad
    private var tilProvince: TextInputLayout? = null
    private var tilDepartment: TextInputLayout? = null
    private var tilAlpacas: TextInputLayout? = null
    
    private lateinit var etFirstName: TextInputEditText
    private var etLastName: TextInputEditText? = null
    private lateinit var etDni: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private var etEmail: TextInputEditText? = null
    private var etAddress: TextInputEditText? = null
    private lateinit var etDistrict: TextInputEditText // Comunidad
    private var etProvince: TextInputEditText? = null
    private var etDepartment: TextInputEditText? = null
    private var etAlpacas: TextInputEditText? = null
    
    private var tvGpsStatus: TextView? = null
    private var btnCaptureGps: MaterialButton? = null
    private var btnBack: ImageButton? = null
    
    private lateinit var btnSave: MaterialButton
    
    private var editingGanaderoId: String? = null
    private var capturedLat: Double? = null
    private var capturedLon: Double? = null
    
    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganadero_form)
        
        editingGanaderoId = intent.getStringExtra("ganadero_id")
        
        initializeViewModel()
        initializeViews()
        setupListeners()
        observeViewModel()
        
        updateTitle()
    }

    private fun initializeViewModel() {
        val appContainer = (application as AlpacaApplication).appContainer
        val factory = GanaderoFormViewModelFactory(
            appContainer.createGanaderoUseCase,
            appContainer.updateGanaderoUseCase
        )
        viewModel = ViewModelProvider(this, factory)[GanaderoFormViewModel::class.java]
    }

    private fun initializeViews() {
        tvTitle = findViewById(R.id.tvTitle)
        tilFirstName = findViewById(R.id.tilFirstName)
        tilLastName = findViewById(R.id.tilLastName)
        tilDni = findViewById(R.id.tilDni)
        tilPhone = findViewById(R.id.tilPhone)
        tilEmail = findViewById(R.id.tilEmail)
        tilAddress = findViewById(R.id.tilAddress)
        tilDistrict = findViewById(R.id.tilDistrict)
        tilProvince = findViewById(R.id.tilProvince)
        tilDepartment = findViewById(R.id.tilDepartment)
        tilAlpacas = findViewById(R.id.tilAlpacas)
        
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etDni = findViewById(R.id.etDni)
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmail)
        etAddress = findViewById(R.id.etAddress)
        etDistrict = findViewById(R.id.etDistrict)
        etProvince = findViewById(R.id.etProvince)
        etDepartment = findViewById(R.id.etDepartment)
        etAlpacas = findViewById(R.id.etAlpacas)
        
        // New views
        tvGpsStatus = findViewById(R.id.tvGpsStatus)
        btnCaptureGps = findViewById(R.id.btnCaptureGps)
        btnBack = findViewById(R.id.btnBack)
        
        btnSave = findViewById(R.id.btnSave)
    }

    private fun setupListeners() {
        btnSave.setOnClickListener {
            saveGanadero()
        }
        
        btnBack?.setOnClickListener {
            finish()
        }
        
        btnCaptureGps?.setOnClickListener {
            captureGps()
        }
    }
    
    private fun captureGps() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
            return
        }
        
        tvGpsStatus?.text = "Buscando ubicación..."
        
        // Simulate GPS capture (in real app, use FusedLocationProviderClient)
        android.os.Handler(mainLooper).postDelayed({
            // Simulated coordinates
            capturedLat = -13.15 + (Math.random() * 0.1)
            capturedLon = -74.22 + (Math.random() * 0.1)
            
            tvGpsStatus?.text = "Lat: ${String.format("%.5f", capturedLat)}, Lon: ${String.format("%.5f", capturedLon)}"
            Toast.makeText(this, "Ubicación capturada", Toast.LENGTH_SHORT).show()
        }, 1500)
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureGps()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            handleUiState(state)
        }
        
        viewModel.navigationEvent.observe(this) { event ->
            handleNavigationEvent(event)
        }
    }

    private fun handleUiState(state: GanaderoFormUiState) {
        when (state) {
            is GanaderoFormUiState.Idle -> {
                btnSave.isEnabled = true
                clearErrors()
            }
            is GanaderoFormUiState.Loading -> {
                btnSave.isEnabled = false
                clearErrors()
            }
            is GanaderoFormUiState.Success -> {
                btnSave.isEnabled = true
                showSuccess(state.message)
            }
            is GanaderoFormUiState.Error -> {
                btnSave.isEnabled = true
                showError(state.message)
            }
            is GanaderoFormUiState.ValidationError -> {
                btnSave.isEnabled = true
                showValidationErrors(state.errors)
            }
        }
    }

    private fun handleNavigationEvent(event: GanaderoFormNavigationEvent) {
        when (event) {
            is GanaderoFormNavigationEvent.NavigateBack -> {
                finish()
            }
        }
    }

    private fun saveGanadero() {
        val firstName = etFirstName.text.toString().trim()
        val dni = etDni.text.toString().trim()
        val comunidad = etDistrict.text.toString().trim()
        
        // Validación básica
        if (firstName.isEmpty() || dni.isEmpty() || comunidad.isEmpty()) {
            Toast.makeText(this, "Los campos Nombre, DNI y Comunidad son obligatorios.", Toast.LENGTH_LONG).show()
            return
        }
        
        val alpacasCount = etAlpacas?.text.toString().toIntOrNull() ?: 0
        
        val ganadero = Ganadero(
            id = editingGanaderoId ?: "",
            firstName = firstName,
            lastName = etLastName?.text.toString().trim(),
            dni = dni,
            phone = etPhone.text.toString().trim(),
            email = etEmail?.text.toString().trim(),
            address = etAddress?.text.toString().trim(),
            district = comunidad,
            province = etProvince?.text.toString().trim(),
            department = etDepartment?.text.toString().trim(),
            alpacasCount = alpacasCount,
            gpsLatitud = capturedLat,
            gpsLongitud = capturedLon,
            status = "activo",
            scoring = 50 // Default scoring
        )
        
        viewModel.onSaveClicked(ganadero)
    }

    private fun updateTitle() {
        tvTitle.text = if (editingGanaderoId != null) {
            getString(R.string.ganadero_form_title_edit)
        } else {
            "Registrar Ganadero"
        }
    }

    private fun clearErrors() {
        tilFirstName.error = null
        tilLastName?.error = null
        tilDni.error = null
        tilPhone.error = null
        tilEmail?.error = null
        tilAddress?.error = null
        tilDistrict.error = null
        tilProvince?.error = null
        tilDepartment?.error = null
        tilAlpacas?.error = null
    }

    private fun showValidationErrors(errors: Map<String, String>) {
        errors.forEach { (field, message) ->
            when (field) {
                "firstName" -> tilFirstName.error = message
                "lastName" -> tilLastName?.error = message
                "dni" -> tilDni.error = message
                "phone" -> tilPhone.error = message
                "email" -> tilEmail?.error = message
                "address" -> tilAddress?.error = message
                "district" -> tilDistrict.error = message
                "province" -> tilProvince?.error = message
                "department" -> tilDepartment?.error = message
            }
        }
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
