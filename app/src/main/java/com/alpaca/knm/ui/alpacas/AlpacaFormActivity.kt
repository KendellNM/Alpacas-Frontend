package com.alpaca.knm.ui.alpacas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.AlpacaApplication
import com.alpaca.knm.R
import com.alpaca.knm.domain.model.AlpacaEstado
import com.alpaca.knm.domain.model.AlpacaRaza
import com.alpaca.knm.domain.model.AlpacaSexo
import com.alpaca.knm.presentation.alpacas.AlpacaFormViewModel
import com.alpaca.knm.presentation.alpacas.AlpacaFormViewModelFactory
import com.alpaca.knm.presentation.alpacas.SaveResult
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AlpacaFormActivity : AppCompatActivity() {
    
    private lateinit var viewModel: AlpacaFormViewModel
    
    private lateinit var toolbar: MaterialToolbar
    private lateinit var tilNombre: TextInputLayout
    private lateinit var etNombre: TextInputEditText
    private lateinit var actvRaza: AutoCompleteTextView
    private lateinit var tilColor: TextInputLayout
    private lateinit var etColor: TextInputEditText
    private lateinit var tilEdad: TextInputLayout
    private lateinit var etEdad: TextInputEditText
    private lateinit var tilPeso: TextInputLayout
    private lateinit var etPeso: TextInputEditText
    private lateinit var actvSexo: AutoCompleteTextView
    private lateinit var actvEstado: AutoCompleteTextView
    private lateinit var etObservaciones: TextInputEditText
    private lateinit var btnSave: Button
    
    private var alpacaId: Long? = null
    private var ganaderoId: Long = 1 // Default, debería venir del usuario actual
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alpaca_form)
        
        setupViewModel()
        setupViews()
        setupDropdowns()
        loadData()
        setupObservers()
    }
    
    private fun setupViewModel() {
        val appContainer = (application as AlpacaApplication).appContainer
        val factory = AlpacaFormViewModelFactory(
            appContainer.createAlpacaUseCase,
            appContainer.updateAlpacaUseCase
        )
        viewModel = ViewModelProvider(this, factory)[AlpacaFormViewModel::class.java]
    }
    
    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar)
        tilNombre = findViewById(R.id.tilNombre)
        etNombre = findViewById(R.id.etNombre)
        actvRaza = findViewById(R.id.actvRaza)
        tilColor = findViewById(R.id.tilColor)
        etColor = findViewById(R.id.etColor)
        tilEdad = findViewById(R.id.tilEdad)
        etEdad = findViewById(R.id.etEdad)
        tilPeso = findViewById(R.id.tilPeso)
        etPeso = findViewById(R.id.etPeso)
        actvSexo = findViewById(R.id.actvSexo)
        actvEstado = findViewById(R.id.actvEstado)
        etObservaciones = findViewById(R.id.etObservaciones)
        btnSave = findViewById(R.id.btnSave)
        
        toolbar.setNavigationOnClickListener { finish() }
        btnSave.setOnClickListener { saveAlpaca() }
    }
    
    private fun setupDropdowns() {
        // Raza
        val razas = listOf(
            getString(R.string.alpaca_raza_huacaya),
            getString(R.string.alpaca_raza_suri)
        )
        actvRaza.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, razas))
        
        // Sexo
        val sexos = listOf(
            getString(R.string.alpaca_sexo_macho),
            getString(R.string.alpaca_sexo_hembra)
        )
        actvSexo.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sexos))
        
        // Estado
        val estados = listOf(
            getString(R.string.alpaca_estado_activo),
            getString(R.string.alpaca_estado_vendido),
            getString(R.string.alpaca_estado_fallecido)
        )
        actvEstado.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, estados))
        
        // Defaults
        actvRaza.setText(razas[0], false)
        actvSexo.setText(sexos[0], false)
        actvEstado.setText(estados[0], false)
    }
    
    private fun loadData() {
        intent?.let {
            alpacaId = if (it.hasExtra("ALPACA_ID")) it.getLongExtra("ALPACA_ID", 0) else null
            ganaderoId = it.getLongExtra("GANADERO_ID", 1)
            
            alpacaId?.let { _ ->
                toolbar.title = getString(R.string.alpaca_form_title_edit)
                etNombre.setText(it.getStringExtra("NOMBRE"))
                actvRaza.setText(it.getStringExtra("RAZA"), false)
                etColor.setText(it.getStringExtra("COLOR"))
                etEdad.setText(it.getIntExtra("EDAD", 0).toString())
                etPeso.setText(it.getDoubleExtra("PESO", 0.0).toString())
                actvSexo.setText(it.getStringExtra("SEXO"), false)
                actvEstado.setText(it.getStringExtra("ESTADO"), false)
                etObservaciones.setText(it.getStringExtra("OBSERVACIONES"))
            }
        }
    }
    
    private fun setupObservers() {
        viewModel.saveResult.observe(this) { result ->
            result?.let {
                when (it) {
                    is SaveResult.Success -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is SaveResult.Error -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                }
                viewModel.clearSaveResult()
            }
        }
    }
    
    private fun saveAlpaca() {
        // Clear errors
        tilNombre.error = null
        tilColor.error = null
        tilEdad.error = null
        tilPeso.error = null
        
        // Get values
        val nombre = etNombre.text.toString().trim()
        val razaStr = actvRaza.text.toString()
        val color = etColor.text.toString().trim()
        val edadStr = etEdad.text.toString()
        val pesoStr = etPeso.text.toString()
        val sexoStr = actvSexo.text.toString()
        val estadoStr = actvEstado.text.toString()
        val observaciones = etObservaciones.text.toString().trim().ifEmpty { null }
        
        // Validate
        var isValid = true
        
        if (nombre.isEmpty()) {
            tilNombre.error = "El nombre es obligatorio"
            isValid = false
        }
        
        if (color.isEmpty()) {
            tilColor.error = "El color es obligatorio"
            isValid = false
        }
        
        if (edadStr.isEmpty()) {
            tilEdad.error = "La edad es obligatoria"
            isValid = false
        }
        
        if (pesoStr.isEmpty()) {
            tilPeso.error = "El peso es obligatorio"
            isValid = false
        }
        
        if (!isValid) return
        
        val edad = edadStr.toIntOrNull() ?: 0
        val peso = pesoStr.toDoubleOrNull() ?: 0.0
        
        val raza = when (razaStr) {
            getString(R.string.alpaca_raza_suri) -> AlpacaRaza.SURI
            else -> AlpacaRaza.HUACAYA
        }
        
        val sexo = when (sexoStr) {
            getString(R.string.alpaca_sexo_hembra) -> AlpacaSexo.HEMBRA
            else -> AlpacaSexo.MACHO
        }
        
        val estado = when (estadoStr) {
            getString(R.string.alpaca_estado_vendido) -> AlpacaEstado.VENDIDO
            getString(R.string.alpaca_estado_fallecido) -> AlpacaEstado.FALLECIDO
            else -> AlpacaEstado.ACTIVO
        }
        
        viewModel.saveAlpaca(
            id = alpacaId,
            ganaderoId = ganaderoId,
            nombre = nombre,
            raza = raza,
            color = color,
            edad = edad,
            peso = peso,
            sexo = sexo,
            estado = estado,
            observaciones = observaciones
        )
    }
}
