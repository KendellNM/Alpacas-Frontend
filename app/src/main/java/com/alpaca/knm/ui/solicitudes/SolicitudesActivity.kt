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
    private var chipPending: Chip? = null
    private var chipApproved: Chip? = null
    private var chipDisbursed: Chip? = null
    private var chipRejected: Chip? = null
    private var btnBack: ImageButton? = null
    private var tvTitle: TextView? = null
    private var allSolicitudes: List<Solicitud> = emptyList()
    
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
        chipPending = findViewById(R.id.chipPending)
        chipApproved = findViewById(R.id.chipApproved)
        chipDisbursed = findViewById(R.id.chipDisbursed)
        chipRejected = findViewById(R.id.chipRejected)
        btnBack = findViewById(R.id.btnBack)
        tvTitle = findViewById(R.id.tvTitle)
        toolbar?.setNavigationOnClickListener { finish() }
        btnBack?.setOnClickListener { finish() }
    }
    
    private fun setupRecyclerView() {
        adapter = SolicitudesAdapter(
            onApprove = { showApproveDialog(it) },
            onReject = { showRejectDialog(it) }
        )
        rvSolicitudes.adapter = adapter
    }
    
    private fun setupObservers() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is SolicitudesUiState.Loading -> showLoading()
                is SolicitudesUiState.Empty -> {
                    allSolicitudes = emptyList()
                    updateChipCounts()
                    filterSolicitudes(SolicitudStatus.PENDIENTE)
                }
                is SolicitudesUiState.Success -> {
                    allSolicitudes = state.solicitudes
                    updateChipCounts()
                    val currentChipId = chipGroupFilter.checkedChipId
                    when (currentChipId) {
                        R.id.chipPending -> filterSolicitudes(SolicitudStatus.PENDIENTE)
                        R.id.chipApproved -> filterSolicitudes(SolicitudStatus.APROBADA)
                        R.id.chipRejected -> filterSolicitudes(SolicitudStatus.RECHAZADA)
                        R.id.chipDisbursed -> filterSolicitudes(SolicitudStatus.DESEMBOLSADA)
                        else -> filterSolicitudes(SolicitudStatus.PENDIENTE)
                    }
                }
                is SolicitudesUiState.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        viewModel.actionResult.observe(this) { result ->
            result?.let {
                when (it) {
                    is ActionResult.Success -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        viewModel.loadSolicitudes(null)
                    }
                    is ActionResult.Error -> Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                viewModel.clearActionResult()
            }
        }
    }
    
    private fun setupFilters() {
        viewModel.loadSolicitudes(null)
        chipPending?.isChecked = true
        chipGroupFilter.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipAll -> filterSolicitudes(null)
                R.id.chipPending -> filterSolicitudes(SolicitudStatus.PENDIENTE)
                R.id.chipApproved -> filterSolicitudes(SolicitudStatus.APROBADA)
                R.id.chipRejected -> filterSolicitudes(SolicitudStatus.RECHAZADA)
                R.id.chipDisbursed -> filterSolicitudes(SolicitudStatus.DESEMBOLSADA)
            }
        }
    }
    
    private fun filterSolicitudes(status: SolicitudStatus?) {
        val filtered = if (status == null) allSolicitudes else allSolicitudes.filter { it.status == status }
        if (filtered.isEmpty()) showEmpty() else showSolicitudes(filtered)
    }
    
    private fun updateChipCounts() {
        chipPending?.text = "Pendiente (${allSolicitudes.count { it.status == SolicitudStatus.PENDIENTE }})"
        chipApproved?.text = "Aprobada (${allSolicitudes.count { it.status == SolicitudStatus.APROBADA }})"
        chipDisbursed?.text = "Desembolsada (${allSolicitudes.count { it.status == SolicitudStatus.DESEMBOLSADA }})"
        chipRejected?.text = "Rechazada (${allSolicitudes.count { it.status == SolicitudStatus.RECHAZADA }})"
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
    
    private fun showApproveDialog(solicitud: Solicitud) {
        AlertDialog.Builder(this)
            .setTitle("Aprobar Solicitud")
            .setMessage("¿Aprobar la solicitud de ${solicitud.ganaderoNombre} por S/ ${String.format("%.2f", solicitud.totalAmount)}?")
            .setPositiveButton("Aprobar") { _, _ -> viewModel.approveSolicitud(solicitud.id) }
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
                    Toast.makeText(this, "Las observaciones son obligatorias.", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
