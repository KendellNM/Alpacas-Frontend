package com.alpaca.knm.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.AlpacaApplication
import com.alpaca.knm.R
import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.SolicitudApiService
import com.alpaca.knm.presentation.dashboard.DashboardNavigationEvent
import com.alpaca.knm.presentation.dashboard.DashboardUiState
import com.alpaca.knm.presentation.dashboard.DashboardViewModel
import com.alpaca.knm.presentation.dashboard.DashboardViewModelFactory
import com.alpaca.knm.ui.adapters.SolicitudAdapter
import com.alpaca.knm.ui.profile.ProfileActivity
import com.alpaca.knm.ui.request.RequestActivity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private lateinit var tvWelcomeUser: TextView
    private lateinit var tvAlpacaCount: TextView
    private lateinit var btnNewRequest: MaterialButton
    private lateinit var rvSolicitudes: RecyclerView
    private lateinit var tvEmptySolicitudes: TextView
    private lateinit var solicitudAdapter: SolicitudAdapter
    private var navHomeContainer: View? = null
    private var navAlpacasContainer: View? = null
    private var navWalletContainer: View? = null
    private var navUserContainer: View? = null
    private var navHome: ImageView? = null
    private var navWallet: ImageView? = null
    private var navUser: ImageView? = null
    private lateinit var viewModel: DashboardViewModel
    
    private val solicitudApiService: SolicitudApiService by lazy {
        RetrofitClient.createService(SolicitudApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        initializeViewModel()
        initializeViews()
        setupListeners()
        observeViewModel()
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.onRefresh()
        loadMisSolicitudes()
    }

    private fun initializeViewModel() {
        val appContainer = (application as AlpacaApplication).appContainer
        val factory = DashboardViewModelFactory(
            appContainer.getDashboardStatsUseCase,
            appContainer.getCurrentUserUseCase
        )
        viewModel = ViewModelProvider(this, factory)[DashboardViewModel::class.java]
    }

    private fun initializeViews() {
        tvWelcomeUser = findViewById(R.id.tvWelcomeUser)
        tvAlpacaCount = findViewById(R.id.tvAlpacaCount)
        btnNewRequest = findViewById(R.id.btnNewRequest)
        rvSolicitudes = findViewById(R.id.rvSolicitudes)
        tvEmptySolicitudes = findViewById(R.id.tvEmptySolicitudes)
        solicitudAdapter = SolicitudAdapter()
        rvSolicitudes.layoutManager = LinearLayoutManager(this)
        rvSolicitudes.adapter = solicitudAdapter
        navHomeContainer = findViewById(R.id.navHomeContainer)
        navAlpacasContainer = findViewById(R.id.navAlpacasContainer)
        navWalletContainer = findViewById(R.id.navWalletContainer)
        navUserContainer = findViewById(R.id.navUserContainer)
        navHome = findViewById(R.id.navHome)
        navWallet = findViewById(R.id.navWallet)
        navUser = findViewById(R.id.navUser)
    }

    private fun setupListeners() {
        btnNewRequest.setOnClickListener { viewModel.onNewRequestClicked() }
        navHomeContainer?.setOnClickListener { }
        navAlpacasContainer?.setOnClickListener { navigateToAlpacaRegistro() }
        navWalletContainer?.setOnClickListener { viewModel.onWalletClicked() }
        navUserContainer?.setOnClickListener { viewModel.onProfileClicked() }
        navHome?.setOnClickListener { }
        navWallet?.setOnClickListener { viewModel.onWalletClicked() }
        navUser?.setOnClickListener { viewModel.onProfileClicked() }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { handleUiState(it) }
        viewModel.navigationEvent.observe(this) { handleNavigationEvent(it) }
    }

    private fun handleUiState(state: DashboardUiState) {
        when (state) {
            is DashboardUiState.Loading -> showLoading(true)
            is DashboardUiState.Success -> {
                showLoading(false)
                updateUI(state.username, state.stats.alpacasCount)
                loadMisSolicitudes()
            }
            is DashboardUiState.Error -> {
                showLoading(false)
                showError(state.message)
            }
        }
    }
    
    private fun loadMisSolicitudes() {
        lifecycleScope.launch {
            try {
                val response = solicitudApiService.getMisSolicitudes()
                if (response.isSuccessful) {
                    val solicitudes = response.body() ?: emptyList()
                    if (solicitudes.isEmpty()) {
                        tvEmptySolicitudes.visibility = View.VISIBLE
                        rvSolicitudes.visibility = View.GONE
                    } else {
                        tvEmptySolicitudes.visibility = View.GONE
                        rvSolicitudes.visibility = View.VISIBLE
                        solicitudAdapter.submitList(solicitudes)
                    }
                }
            } catch (_: Exception) {
                tvEmptySolicitudes.visibility = View.VISIBLE
                rvSolicitudes.visibility = View.GONE
            }
        }
    }

    private fun handleNavigationEvent(event: DashboardNavigationEvent) {
        when (event) {
            is DashboardNavigationEvent.NavigateToNewRequest -> navigateToRequest()
            is DashboardNavigationEvent.NavigateToWallet -> navigateToRequest()
            is DashboardNavigationEvent.NavigateToProfile -> navigateToProfile()
        }
    }
    
    private fun navigateToRequest() {
        startActivity(Intent(this, RequestActivity::class.java))
    }
    
    private fun navigateToProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }
    
    private fun navigateToAlpacaRegistro() {
        startActivity(Intent(this, com.alpaca.knm.ui.alpacas.MisAlpacasActivity::class.java))
    }

    private fun updateUI(username: String, alpacasCount: Int) {
        tvWelcomeUser.text = "Hola, $username"
        tvAlpacaCount.text = alpacasCount.toString()
    }

    private fun showLoading(isLoading: Boolean) {
        btnNewRequest.isEnabled = !isLoading
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
