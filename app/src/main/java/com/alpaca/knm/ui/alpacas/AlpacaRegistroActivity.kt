package com.alpaca.knm.ui.alpacas

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.R
import com.alpaca.knm.data.local.SessionManager
import com.alpaca.knm.data.repository.AlpacaRegistroRepositoryImpl
import com.alpaca.knm.domain.model.AlpacaRaza
import com.alpaca.knm.presentation.alpacas.AlpacaRegistroUiState
import com.alpaca.knm.presentation.alpacas.AlpacaRegistroViewModel
import com.alpaca.knm.presentation.alpacas.AlpacaRegistroViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AlpacaRegistroActivity : AppCompatActivity() {
    
    private lateinit var viewModel: AlpacaRegistroViewModel
    
    private lateinit var toolbar: MaterialToolbar
    private lateinit var actvRaza: AutoCompleteTextView
    private lateinit var etAdultos: TextInputEditText
    private lateinit var etCrias: TextInputEditText
    private lateinit var tvResumenAdultos: TextView
    private lateinit var tvResumenCrias: TextView
    private lateinit var tvResumenTotal: TextView
    private lateinit var btnGuardar: MaterialButton
    private lateinit var progressBar: ProgressBar
    
    private var ganaderoId: Int = 0
    private var registroId: Int? = null // null = crear, valor = editar
    private var isEditMode = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alpaca_registro)
        
        ganaderoId = SessionManager.getUserId(this)
        
        registroId = intent.getIntExtra("REGISTRO_ID", -1).takeIf { it != -1 }
        isEditMode = registroId != null
        
        setupViewModel()
        setupViews()
        setupListeners()
        observeViewModel()
        
        if (isEditMode) {
            cargarDatosEdicion()
        }
    }
    
    private fun cargarDatosEdicion() {
        toolbar.title = "Editar Registro"
        btnGuardar.text = "Actualizar"
        
        val raza = intent.getStringExtra("RAZA") ?: ""
        val adultos = intent.getIntExtra("ADULTOS", 0)
        val crias = intent.getIntExtra("CRIAS", 0)
        
        actvRaza.setText(raza, false)
        etAdultos.setText(adultos.toString())
        etCrias.setText(crias.toString())
        actualizarResumen()
    }
    
    private fun setupViewModel() {
        val repository = AlpacaRegistroRepositoryImpl()
        val factory = AlpacaRegistroViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AlpacaRegistroViewModel::class.java]
    }

    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar)
        actvRaza = findViewById(R.id.actvRaza)
        etAdultos = findViewById(R.id.etAdultos)
        etCrias = findViewById(R.id.etCrias)
        tvResumenAdultos = findViewById(R.id.tvResumenAdultos)
        tvResumenCrias = findViewById(R.id.tvResumenCrias)
        tvResumenTotal = findViewById(R.id.tvResumenTotal)
        btnGuardar = findViewById(R.id.btnGuardar)
        progressBar = findViewById(R.id.progressBar)
        
        val razas = AlpacaRaza.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, razas)
        actvRaza.setAdapter(adapter)
    }
    
    private fun setupListeners() {
        toolbar.setNavigationOnClickListener { finish() }
        
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { actualizarResumen() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        etAdultos.addTextChangedListener(textWatcher)
        etCrias.addTextChangedListener(textWatcher)
        
        btnGuardar.setOnClickListener { guardarRegistro() }
    }
    
    private fun actualizarResumen() {
        val adultos = etAdultos.text.toString().toIntOrNull() ?: 0
        val crias = etCrias.text.toString().toIntOrNull() ?: 0
        val total = adultos + crias
        
        tvResumenAdultos.text = adultos.toString()
        tvResumenCrias.text = crias.toString()
        tvResumenTotal.text = total.toString()
    }
    
    private fun guardarRegistro() {
        val raza = actvRaza.text.toString()
        val adultos = etAdultos.text.toString().toIntOrNull()
        val crias = etCrias.text.toString().toIntOrNull()
        
        val razaValida = AlpacaRaza.values().any { it.name == raza }
        if (raza.isEmpty() || !razaValida) {
            Toast.makeText(this, "Seleccione una raza válida", Toast.LENGTH_SHORT).show()
            return
        }
        if (adultos == null || adultos < 0) {
            Toast.makeText(this, "Ingrese cantidad de adultos válida", Toast.LENGTH_SHORT).show()
            return
        }
        if (crias == null || crias < 0) {
            Toast.makeText(this, "Ingrese cantidad de crías válida", Toast.LENGTH_SHORT).show()
            return
        }
        
        val total = adultos + crias
        if (total <= 0) {
            Toast.makeText(this, "Debe registrar al menos una alpaca", Toast.LENGTH_SHORT).show()
            return
        }
        
        val razaFormateada = raza.lowercase().replaceFirstChar { it.uppercase() }
        
        if (isEditMode && registroId != null) {
            viewModel.actualizarRegistro(registroId!!, ganaderoId, razaFormateada, total, adultos)
        } else {
            viewModel.crearRegistro(ganaderoId, razaFormateada, total, adultos)
        }
    }
    
    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is AlpacaRegistroUiState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    btnGuardar.isEnabled = false
                }
                is AlpacaRegistroUiState.Created -> {
                    progressBar.visibility = View.GONE
                    btnGuardar.isEnabled = true
                    val msg = if (isEditMode) "Registro actualizado" else "Registro creado exitosamente"
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is AlpacaRegistroUiState.Updated -> {
                    progressBar.visibility = View.GONE
                    btnGuardar.isEnabled = true
                    Toast.makeText(this, "Registro actualizado exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is AlpacaRegistroUiState.Error -> {
                    progressBar.visibility = View.GONE
                    btnGuardar.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                else -> {
                    progressBar.visibility = View.GONE
                    btnGuardar.isEnabled = true
                }
            }
        }
    }
}
