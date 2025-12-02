package com.alpaca.knm.ui.solicitudes

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.AlpacaApplication
import com.alpaca.knm.R
import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.model.SolicitudStatus
import com.alpaca.knm.presentation.solicitudes.ActionResult
import com.alpaca.knm.presentation.solicitudes.SolicitudesAdapter
import com.alpaca.knm.presentation.solicitudes.SolicitudesUiState
import com.alpaca.knm.presentation.solicitudes.SolicitudesViewModel
import com.alpaca.knm.presentation.solicitudes.SolicitudesViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SolicitudesActivity : AppCompatActivity() {
    
    private lateinit var viewModel: SolicitudesViewModel
    private lateinit var adapter: SolicitudesAdapter
    
    private var toolbar: MaterialToolbar? = null
    private lateinit var chipGroupFilter: ChipGroup
    private lateinit var rvSolicitudes: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyState: View
    
    // New views
    private var btnBack: ImageButton? = null
    private var tvTitle: TextView? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitudes)
        
        setupViewModel()
        setupViews()
        setupRecyclerView()
        setupObservers()
        setupFilters()
    }
    
    private fun setupViewModel() {
        val appContainer = (application as AlpacaApplication).appContainer
        val factory = SolicitudesViewModelFactory(
            appContainer.getAllSolicitudesUseCase,
            appContainer.getSolicitudesByStatusUseCase,
            appContainer.approveSolicitudUseCase,
            appContainer.rejectSolicitudUseCase
        )
        viewModel = ViewModelProvider(this, factory)[SolicitudesViewModel::class.java]
    }
    
    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar)
        chipGroupFilter = findViewById(R.id.chipGroupFilter)
        rvSolicitudes = findViewById(R.id.rvSolicitudes)
        progressBar = findViewById(R.id.progressBar)
        emptyState = findViewById(R.id.emptyState)
        
        // New navigation
        btnBack = findViewById(R.id.btnBack)
        tvTitle = findViewById(R.id.tvTitle)
        
        // Setup navigation
        toolbar?.setNavigationOnClickListener { finish() }
        btnBack?.setOnClickListener { finish() }
    }
    
    private fun setupRecyclerView() {
        adapter = SolicitudesAdapter(
            onApprove = { solicitud -> showApproveDialog(solicitud) },
            onReject = { solicitud -> showRejectDialog(solicitud) }
        )
        rvSolicitudes.adapter = adapter
    }
    
    private fun setupObservers() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is SolicitudesUiState.Loading -> showLoading()
                is SolicitudesUiState.Empty -> showEmpty()
                is SolicitudesUiState.Success -> showSolicitudes(state.solicitudes)
                is SolicitudesUiState.Error -> showError(state.message)
            }
        }
        
        viewModel.actionResult.observe(this) { result ->
            result?.let {
                when (it) {
                    is ActionResult.Success -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is ActionResult.Error -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                }
                viewModel.clearActionResult()
            }
        }
    }
    
    private fun setupFilters() {
        // Load pending by default
        viewModel.loadSolicitudes(SolicitudStatus.PENDIENTE)
        
        chipGroupFilter.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipAll -> viewModel.loadSolicitudes(null)
                R.id.chipPending -> viewModel.loadSolicitudes(SolicitudStatus.PENDIENTE)
                R.id.chipApproved -> viewModel.loadSolicitudes(SolicitudStatus.APROBADA)
                R.id.chipRejected -> viewModel.loadSolicitudes(SolicitudStatus.RECHAZADA)
                R.id.chipDisbursed -> viewModel.loadSolicitudes(SolicitudStatus.DESEMBOLSADA)
            }
        }
    }
    
    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        rvSolicitudes.visibility = View.GONE
        emptyState.visibility = View.GONE
    }
    
    private fun showEmpty() {
        progressBar.visibility = View.GONE
        rvSolicitudes.visibility = View.GONE
        emptyState.visibility = View.VISIBLE
    }
    
    private fun showSolicitudes(solicitudes: List<Solicitud>) {
        progressBar.visibility = View.GONE
        rvSolicitudes.visibility = View.VISIBLE
        emptyState.visibility = View.GONE
        adapter.submitList(solicitudes)
    }
    
    private fun showError(message: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    private fun showApproveDialog(solicitud: Solicitud) {
        AlertDialog.Builder(this)
            .setTitle("Aprobar Solicitud")
            .setMessage("¿Aprobar la solicitud de ${solicitud.ganaderoNombre} por S/ ${String.format("%.2f", solicitud.totalAmount)}?")
            .setPositiveButton("Aprobar") { _, _ ->
                viewModel.approveSolicitud(solicitud.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun showRejectDialog(solicitud: Solicitud) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_reject_reason, null)
        val inputLayout = dialogView.findViewById<TextInputLayout>(R.id.tilRejectReason)
        val inputText = dialogView.findViewById<TextInputEditText>(R.id.etRejectReason)
        
        AlertDialog.Builder(this)
            .setTitle("Rechazar Solicitud")
            .setMessage("Las observaciones son obligatorias para RECHAZAR una solicitud.")
            .setView(dialogView)
            .setPositiveButton("Rechazar") { _, _ ->
                val reason = inputText.text.toString().trim()
                if (reason.isNotEmpty()) {
                    viewModel.rejectSolicitud(solicitud.id, reason)
                } else {
                    inputLayout.error = "El motivo es obligatorio"
                    Toast.makeText(this, "Las observaciones son obligatorias para RECHAZAR una solicitud.", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
