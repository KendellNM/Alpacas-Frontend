package com.alpaca.knm.ui.alpacas

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.R
import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.AlpacaRegistroApiService
import com.alpaca.knm.data.remote.dto.AlpacaRegistroResponse
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlpacasRegistrosActivity : AppCompatActivity() {
    
    private lateinit var toolbar: MaterialToolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyState: View
    
    private lateinit var tvTotalAlpacas: TextView
    private lateinit var tvTotalGanaderos: TextView
    private lateinit var tvTotalRegistros: TextView
    
    private val apiService: AlpacaRegistroApiService by lazy {
        RetrofitClient.createService(AlpacaRegistroApiService::class.java)
    }
    
    private lateinit var adapter: AlpacasRegistrosAgrupadorAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alpacas_registros)
        
        setupViews()
        setupRecyclerView()
        loadRegistros()
    }
    
    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.rvRegistros)
        progressBar = findViewById(R.id.progressBar)
        emptyState = findViewById(R.id.emptyState)
        
        tvTotalAlpacas = findViewById(R.id.tvTotalAlpacas)
        tvTotalGanaderos = findViewById(R.id.tvTotalGanaderos)
        tvTotalRegistros = findViewById(R.id.tvTotalRegistros)
        
        toolbar.setNavigationOnClickListener { finish() }
    }
    
    private fun setupRecyclerView() {
        adapter = AlpacasRegistrosAgrupadorAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
    
    private fun loadRegistros() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        emptyState.visibility = View.GONE
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getAllRegistros()
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        val registros = response.body()!!
                        if (registros.isEmpty()) {
                            emptyState.visibility = View.VISIBLE
                        } else {
                            recyclerView.visibility = View.VISIBLE
                            
                            val totalAlpacas = registros.sumOf { it.cantidad }
                            val ganaderos = registros.map { it.ganaderoNombre.ifEmpty { "Ganadero #${it.ganaderoId}" } }.distinct()
                            
                            tvTotalAlpacas.text = totalAlpacas.toString()
                            tvTotalGanaderos.text = ganaderos.size.toString()
                            tvTotalRegistros.text = registros.size.toString()
                            
                            val agrupados = agruparPorGanadero(registros)
                            adapter.submitList(agrupados)
                        }
                    } else {
                        Toast.makeText(this@AlpacasRegistrosActivity, "Error al cargar registros", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@AlpacasRegistrosActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun agruparPorGanadero(registros: List<AlpacaRegistroResponse>): List<GanaderoConAlpacas> {
        return registros.groupBy { 
            it.ganaderoNombre.ifEmpty { "Ganadero #${it.ganaderoId}" } 
        }.map { (nombre, regs) ->
            GanaderoConAlpacas(
                ganaderoNombre = nombre,
                totalAlpacas = regs.sumOf { it.cantidad },
                totalAdultos = regs.sumOf { it.adultos },
                totalCrias = regs.sumOf { it.crias },
                registros = regs
            )
        }.sortedByDescending { it.totalAlpacas }
    }
}

data class GanaderoConAlpacas(
    val ganaderoNombre: String,
    val totalAlpacas: Int,
    val totalAdultos: Int,
    val totalCrias: Int,
    val registros: List<AlpacaRegistroResponse>
)
