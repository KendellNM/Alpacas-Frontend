package com.alpaca.knm.ui.ganaderos

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.AlpacaApplication
import com.alpaca.knm.R
import com.alpaca.knm.presentation.ganaderos.GanaderosNavigationEvent
import com.alpaca.knm.presentation.ganaderos.GanaderosUiState
import com.alpaca.knm.presentation.ganaderos.GanaderosViewModel
import com.alpaca.knm.presentation.ganaderos.GanaderosViewModelFactory
import com.alpaca.knm.ui.adapters.GanaderosAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class GanaderosActivity : AppCompatActivity() {

    private lateinit var viewModel: GanaderosViewModel
    private lateinit var adapter: GanaderosAdapter
    private lateinit var rvGanaderos: RecyclerView
    private lateinit var etSearch: TextInputEditText
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var emptyState: View
    private lateinit var progressBar: View
    private var btnBack: ImageButton? = null
    private var tvTitle: TextView? = null
    private var chipActivos: com.google.android.material.chip.Chip? = null
    private var chipInactivos: com.google.android.material.chip.Chip? = null
    private var showingActivos = true
    private var allGanaderos: List<com.alpaca.knm.domain.model.Ganadero> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganaderos)
        initializeViewModel()
        initializeViews()
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun initializeViewModel() {
        val appContainer = (application as AlpacaApplication).appContainer
        val factory = GanaderosViewModelFactory(appContainer.getGanaderosUseCase, appContainer.deleteGanaderoUseCase)
        viewModel = ViewModelProvider(this, factory)[GanaderosViewModel::class.java]
    }

    private fun initializeViews() {
        rvGanaderos = findViewById(R.id.rvGanaderos)
        etSearch = findViewById(R.id.etSearch)
        fabAdd = findViewById(R.id.fabAdd)
        emptyState = findViewById(R.id.emptyState)
        progressBar = findViewById(R.id.progressBar)
        btnBack = findViewById(R.id.btnBack)
        tvTitle = findViewById(R.id.tvTitle)
        chipActivos = findViewById(R.id.chipActivos)
        chipInactivos = findViewById(R.id.chipInactivos)
        try {
            val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            if (toolbar != null && toolbar.visibility == View.VISIBLE) {
                setSupportActionBar(toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        } catch (_: Exception) { }
    }

    private fun setupRecyclerView() {
        adapter = GanaderosAdapter(onItemClick = { }, onDeleteClick = { })
        rvGanaderos.adapter = adapter
    }

    private fun setupListeners() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { viewModel.onSearchQueryChanged(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        fabAdd.visibility = View.GONE
        btnBack?.setOnClickListener { finish() }
        chipActivos?.setOnClickListener {
            showingActivos = true
            chipActivos?.isChecked = true
            chipInactivos?.isChecked = false
            filterGanaderos()
        }
        chipInactivos?.setOnClickListener {
            showingActivos = false
            chipActivos?.isChecked = false
            chipInactivos?.isChecked = true
            filterGanaderos()
        }
    }
    
    private fun filterGanaderos() {
        val filtered = if (showingActivos) {
            allGanaderos.filter { it.status?.uppercase() == "ACTIVO" || it.status == null }
        } else {
            allGanaderos.filter { it.status?.uppercase() == "INACTIVO" }
        }
        if (filtered.isEmpty()) {
            rvGanaderos.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
        } else {
            rvGanaderos.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
            adapter.submitList(filtered)
        }
    }
    
    private fun updateChipCounts() {
        val activos = allGanaderos.count { it.status?.uppercase() == "ACTIVO" || it.status == null }
        val inactivos = allGanaderos.count { it.status?.uppercase() == "INACTIVO" }
        chipActivos?.text = "Activos ($activos)"
        chipInactivos?.text = "Inactivos ($inactivos)"
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { handleUiState(it) }
        viewModel.navigationEvent.observe(this) { handleNavigationEvent(it) }
    }

    private fun handleUiState(state: GanaderosUiState) {
        when (state) {
            is GanaderosUiState.Loading -> {
                progressBar.visibility = View.VISIBLE
                rvGanaderos.visibility = View.GONE
                emptyState.visibility = View.GONE
            }
            is GanaderosUiState.Success -> {
                progressBar.visibility = View.GONE
                allGanaderos = state.ganaderos
                updateChipCounts()
                filterGanaderos()
                tvTitle?.text = "Ganaderos (${state.ganaderos.size})"
            }
            is GanaderosUiState.Empty -> {
                progressBar.visibility = View.GONE
                rvGanaderos.visibility = View.GONE
                emptyState.visibility = View.VISIBLE
                tvTitle?.text = "Ganaderos (0)"
            }
            is GanaderosUiState.Error -> {
                progressBar.visibility = View.GONE
                Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleNavigationEvent(event: GanaderosNavigationEvent) {
        when (event) {
            is GanaderosNavigationEvent.NavigateToDetail -> navigateToForm(event.id)
            is GanaderosNavigationEvent.NavigateToCreate -> navigateToForm(null)
        }
    }

    private fun navigateToForm(ganaderoId: String?) {
        val intent = Intent(this, GanaderoFormActivity::class.java)
        ganaderoId?.let { intent.putExtra("ganadero_id", it) }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadGanaderos()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
