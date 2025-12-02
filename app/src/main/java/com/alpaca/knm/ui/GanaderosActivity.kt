package com.alpaca.knm.ui

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
    
    // New views
    private var btnBack: ImageButton? = null
    private var tvTitle: TextView? = null

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
        val factory = GanaderosViewModelFactory(
            appContainer.getGanaderosUseCase,
            appContainer.deleteGanaderoUseCase
        )
        viewModel = ViewModelProvider(this, factory)[GanaderosViewModel::class.java]
    }

    private fun initializeViews() {
        rvGanaderos = findViewById(R.id.rvGanaderos)
        etSearch = findViewById(R.id.etSearch)
        fabAdd = findViewById(R.id.fabAdd)
        emptyState = findViewById(R.id.emptyState)
        progressBar = findViewById(R.id.progressBar)
        
        // New navigation
        btnBack = findViewById(R.id.btnBack)
        tvTitle = findViewById(R.id.tvTitle)
        
        // Try to setup toolbar if exists (for compatibility)
        try {
            val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            if (toolbar != null && toolbar.visibility == View.VISIBLE) {
                setSupportActionBar(toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        } catch (e: Exception) {
            // Toolbar not found or hidden, use new navigation
        }
    }

    private fun setupRecyclerView() {
        adapter = GanaderosAdapter(
            onItemClick = { ganadero ->
                viewModel.onGanaderoClicked(ganadero)
            },
            onDeleteClick = { ganadero ->
                showDeleteConfirmation(ganadero.id, ganadero.fullName)
            }
        )
        rvGanaderos.adapter = adapter
    }

    private fun setupListeners() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onSearchQueryChanged(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        
        fabAdd.setOnClickListener {
            viewModel.onAddGanaderoClicked()
        }
        
        // New back button
        btnBack?.setOnClickListener {
            finish()
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

    private fun handleUiState(state: GanaderosUiState) {
        when (state) {
            is GanaderosUiState.Loading -> {
                progressBar.visibility = View.VISIBLE
                rvGanaderos.visibility = View.GONE
                emptyState.visibility = View.GONE
            }
            is GanaderosUiState.Success -> {
                progressBar.visibility = View.GONE
                rvGanaderos.visibility = View.VISIBLE
                emptyState.visibility = View.GONE
                adapter.submitList(state.ganaderos)
                // Update title with count
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
                showError(state.message)
            }
        }
    }

    private fun handleNavigationEvent(event: GanaderosNavigationEvent) {
        when (event) {
            is GanaderosNavigationEvent.NavigateToDetail -> {
                navigateToForm(event.id)
            }
            is GanaderosNavigationEvent.NavigateToCreate -> {
                navigateToForm(null)
            }
        }
    }

    private fun navigateToForm(ganaderoId: String?) {
        val intent = Intent(this, GanaderoFormActivity::class.java)
        ganaderoId?.let { intent.putExtra("ganadero_id", it) }
        startActivity(intent)
    }

    private fun showDeleteConfirmation(id: String, name: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.ganadero_delete_confirm))
            .setMessage(getString(R.string.ganadero_delete_message))
            .setPositiveButton(getString(R.string.ganadero_delete)) { _, _ ->
                viewModel.onDeleteGanadero(id)
                Toast.makeText(this, getString(R.string.ganadero_deleted), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
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
