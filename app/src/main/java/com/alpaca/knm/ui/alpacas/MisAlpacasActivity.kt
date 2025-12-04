package com.alpaca.knm.ui.alpacas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.R
import com.alpaca.knm.data.local.SessionManager
import com.alpaca.knm.data.repository.AlpacaRegistroRepositoryImpl
import com.alpaca.knm.domain.model.AlpacaRegistro
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MisAlpacasActivity : AppCompatActivity() {
    
    private lateinit var toolbar: MaterialToolbar
    private lateinit var rvAlpacas: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var fabAdd: FloatingActionButton
    
    private lateinit var adapter: AlpacaRegistroAdapter
    private val repository = AlpacaRegistroRepositoryImpl()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_alpacas)
        
        setupViews()
        setupListeners()
        loadMisAlpacas()
    }
    
    override fun onResume() {
        super.onResume()
        loadMisAlpacas()
    }
    
    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar)
        rvAlpacas = findViewById(R.id.rvAlpacas)
        progressBar = findViewById(R.id.progressBar)
        tvEmpty = findViewById(R.id.tvEmpty)
        fabAdd = findViewById(R.id.fabAdd)
        
        adapter = AlpacaRegistroAdapter { registro ->
            editarRegistro(registro)
        }
        rvAlpacas.layoutManager = LinearLayoutManager(this)
        rvAlpacas.adapter = adapter
    }
    
    private fun setupListeners() {
        toolbar.setNavigationOnClickListener { finish() }
        
        fabAdd.setOnClickListener {
            startActivity(Intent(this, AlpacaRegistroActivity::class.java))
        }
    }
    
    private fun loadMisAlpacas() {
        val token = SessionManager.getToken(this)
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        progressBar.visibility = View.VISIBLE
        tvEmpty.visibility = View.GONE
        
        lifecycleScope.launch {
            repository.getMisRegistros(token).fold(
                onSuccess = { registros ->
                    progressBar.visibility = View.GONE
                    if (registros.isEmpty()) {
                        tvEmpty.visibility = View.VISIBLE
                        rvAlpacas.visibility = View.GONE
                    } else {
                        tvEmpty.visibility = View.GONE
                        rvAlpacas.visibility = View.VISIBLE
                        adapter.submitList(registros)
                    }
                },
                onFailure = { error ->
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@MisAlpacasActivity, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
    
    private fun editarRegistro(registro: AlpacaRegistro) {
        val intent = Intent(this, AlpacaRegistroActivity::class.java).apply {
            putExtra("REGISTRO_ID", registro.id)
            putExtra("RAZA", registro.raza.name)
            putExtra("ADULTOS", registro.adultos)
            putExtra("CRIAS", registro.crias)
        }
        startActivity(intent)
    }
}
