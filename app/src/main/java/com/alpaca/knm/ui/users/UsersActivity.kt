package com.alpaca.knm.ui.users

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.R
import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.UserApiService
import com.alpaca.knm.data.remote.dto.CreateUserRequest
import com.alpaca.knm.data.remote.dto.UserDto
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersActivity : AppCompatActivity() {
    
    private lateinit var toolbar: MaterialToolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var emptyView: View
    
    private val apiService: UserApiService by lazy {
        RetrofitClient.createService(UserApiService::class.java)
    }
    
    private val users = mutableListOf<UserDto>()
    private lateinit var adapter: UsersAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        
        setupViews()
        setupRecyclerView()
        loadUsers()
    }
    
    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.rvUsers)
        progressBar = findViewById(R.id.progressBar)
        fabAdd = findViewById(R.id.fabAdd)
        emptyView = findViewById(R.id.emptyState)
        
        toolbar.setNavigationOnClickListener { finish() }
        fabAdd.setOnClickListener { showCreateUserDialog() }
    }
    
    private fun setupRecyclerView() {
        adapter = UsersAdapter(
            users,
            onEdit = { user -> showEditUserDialog(user) },
            onDelete = { user -> showDeleteConfirmation(user) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
    
    private fun loadUsers() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        emptyView.visibility = View.GONE
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getAllUsers()
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        users.clear()
                        users.addAll(response.body()!!)
                        adapter.notifyDataSetChanged()
                        
                        if (users.isEmpty()) {
                            emptyView.visibility = View.VISIBLE
                        } else {
                            recyclerView.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(this@UsersActivity, "Error al cargar usuarios", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@UsersActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun showCreateUserDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_user_form, null)
        val etUsername = dialogView.findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = dialogView.findViewById<TextInputEditText>(R.id.etPassword)
        val etEmail = dialogView.findViewById<TextInputEditText>(R.id.etEmail)
        val etFullName = dialogView.findViewById<TextInputEditText>(R.id.etFullName)
        val actvRole = dialogView.findViewById<AutoCompleteTextView>(R.id.actvRole)
        
        val roles = arrayOf("ROLE_GANADERO", "ROLE_ADMIN")
        actvRole.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles))
        actvRole.setText("ROLE_GANADERO", false)
        
        AlertDialog.Builder(this)
            .setTitle("Crear Usuario")
            .setView(dialogView)
            .setPositiveButton("Crear") { _, _ ->
                val username = etUsername.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val fullName = etFullName.text.toString().trim()
                val role = actvRole.text.toString()
                
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    createUser(CreateUserRequest(username, password, email.ifEmpty { null }, fullName.ifEmpty { null }, role))
                } else {
                    Toast.makeText(this, "Usuario y contraseña son requeridos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun showEditUserDialog(user: UserDto) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_user_form, null)
        val etUsername = dialogView.findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = dialogView.findViewById<TextInputEditText>(R.id.etPassword)
        val etEmail = dialogView.findViewById<TextInputEditText>(R.id.etEmail)
        val etFullName = dialogView.findViewById<TextInputEditText>(R.id.etFullName)
        val actvRole = dialogView.findViewById<AutoCompleteTextView>(R.id.actvRole)
        
        etUsername.setText(user.username)
        etUsername.isEnabled = false
        etEmail.setText(user.email ?: "")
        etFullName.setText(user.fullName ?: "")
        etPassword.hint = "Nueva contraseña (dejar vacío para no cambiar)"
        
        val roles = arrayOf("ROLE_GANADERO", "ROLE_ADMIN")
        actvRole.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles))
        actvRole.setText(user.role, false)
        
        AlertDialog.Builder(this)
            .setTitle("Editar Usuario")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val password = etPassword.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val fullName = etFullName.text.toString().trim()
                val role = actvRole.text.toString()
                
                updateUser(user.id, email.ifEmpty { null }, fullName.ifEmpty { null }, role, password.ifEmpty { null })
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun showDeleteConfirmation(user: UserDto) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Usuario")
            .setMessage("¿Estás seguro de eliminar a ${user.username}?")
            .setPositiveButton("Eliminar") { _, _ -> deleteUser(user.id) }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun createUser(request: CreateUserRequest) {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.createUser(request)
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(this@UsersActivity, "Usuario creado", Toast.LENGTH_SHORT).show()
                        loadUsers()
                    } else {
                        Toast.makeText(this@UsersActivity, "Error al crear usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@UsersActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun updateUser(id: Int, email: String?, fullName: String?, role: String?, password: String?) {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = com.alpaca.knm.data.remote.dto.UpdateUserRequest(email, fullName, role, password)
                val response = apiService.updateUser(id, request)
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(this@UsersActivity, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                        loadUsers()
                    } else {
                        Toast.makeText(this@UsersActivity, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@UsersActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun deleteUser(id: Int) {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.deleteUser(id)
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(this@UsersActivity, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                        loadUsers()
                    } else {
                        Toast.makeText(this@UsersActivity, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@UsersActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
