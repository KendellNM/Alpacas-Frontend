package com.alpaca.knm.ui.alpacas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.AlpacaApplication
import com.alpaca.knm.R
import com.alpaca.knm.domain.model.Alpaca
import com.alpaca.knm.presentation.alpacas.AlpacasAdapter
import com.alpaca.knm.presentation.alpacas.AlpacasUiState
import com.alpaca.knm.presentation.alpacas.AlpacasViewModel
import com.alpaca.knm.presentation.alpacas.AlpacasViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class AlpacasActivity : AppCompatActivity() {
    
    private lateinit var viewModel: AlpacasViewModel
    private lateinit var adapter: AlpacasAdapter
    
    private lateinit var toolbar: MaterialToolbar
    private lateinit var etSearch: TextInputEditText
    private lateinit var rvAlpacas: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyState: View
    private lateinit var fabAdd: FloatingActionButton
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alpacas)
        
        setupViewModel()
        setupViews()
        setupRecyclerView()
        setupObservers()
        setupSearch()
        
        viewModel.loadAlpacas()
    }
    
    private fun setupViewModel() {
        val appContainer = (application as AlpacaApplication).appContainer
        val factory = AlpacasViewModelFactory(
            appContainer.getAllAlpacasUseCase,
            appContainer.getAlpacasByGanaderoUseCase,
            appContainer.deleteAlpacaUseCase
        )
        viewModel = ViewModelProvider(this, factory)[AlpacasViewModel::class.java]
    }
    
    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar)
        etSearch = findViewById(R.id.etSearch)
        rvAlpacas = findViewById(R.id.rvAlpacas)
        progressBar = findViewById(R.id.progressBar)
        emptyState = findViewById(R.id.emptyState)
        fabAdd = findViewById(R.id.fabAdd)
        
        toolbar.setNavigationOnClickListener { finish() }
        fabAdd.setOnClickListener { navigateToForm() }
    }
    
    private fun setupRecyclerView() {
        adapter = AlpacasAdapter(
            onEdit = { alpaca -> navigateToForm(alpaca) },
            onDelete = { alpaca -> showDeleteDialog(alpaca) }
        )
        rvAlpacas.adapter = adapter
    }
    
    private fun setupObservers() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is AlpacasUiState.Loading -> showLoading()
                is AlpacasUiState.Empty -> showEmpty()
                is AlpacasUiState.Success -> showAlpacas(state.alpacas)
                is AlpacasUiState.Error -> showError(state.message)
            }
        }
        
        viewModel.deleteResult.observe(this) { result ->
            result?.let {
                when (it) {
                    is com.alpaca.knm.presentation.alpacas.DeleteResult.Success -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is com.alpaca.knm.presentation.alpacas.DeleteResult.Error -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                }
                viewModel.clearDeleteResult()
            }
        }
    }
    
    private fun setupSearch() {
        etSearch.addTextChangedListener { text ->
            viewModel.searchAlpacas(text.toString())
        }
    }
    
    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        rvAlpacas.visibility = View.GONE
        emptyState.visibility = View.GONE
    }
    
    private fun showEmpty() {
        progressBar.visibility = View.GONE
        rvAlpacas.visibility = View.GONE
        emptyState.visibility = View.VISIBLE
    }
    
    private fun showAlpacas(alpacas: List<Alpaca>) {
        progressBar.visibility = View.GONE
        rvAlpacas.visibility = View.VISIBLE
        emptyState.visibility = View.GONE
        adapter.submitList(alpacas)
    }
    
    private fun showError(message: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    private fun navigateToForm(alpaca: Alpaca? = null) {
        val intent = Intent(this, AlpacaFormActivity::class.java)
        alpaca?.let {
            intent.putExtra("ALPACA_ID", it.id)
            intent.putExtra("GANADERO_ID", it.ganaderoId)
            intent.putExtra("NOMBRE", it.nombre as String)
            intent.putExtra("RAZA", it.raza.name as String)
            intent.putExtra("COLOR", it.color as String)
            intent.putExtra("EDAD", it.edad as Int)
            intent.putExtra("PESO", it.peso as Double)
            intent.putExtra("SEXO", it.sexo.name as String)
            intent.putExtra("ESTADO", it.estado.name as String)
            intent.putExtra("OBSERVACIONES", it.observaciones ?: "")
        }
        startActivity(intent)
    }
    
    private fun showDeleteDialog(alpaca: Alpaca) {
        AlertDialog.Builder(this)
            .setTitle(R.string.alpacas_delete_confirm)
            .setMessage(getString(R.string.alpacas_delete_message))
            .setPositiveButton(R.string.alpacas_delete) { _, _ ->
                viewModel.deleteAlpaca(alpaca.id)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.loadAlpacas()
    }
}
