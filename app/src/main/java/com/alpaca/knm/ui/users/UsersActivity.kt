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
import android.app.DatePickerDialog
import java.util.Calendar
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
        val layoutGanaderoFields = dialogView.findViewById<View>(R.id.layoutGanaderoFields)
        val etDni = dialogView.findViewById<TextInputEditText>(R.id.etDni)
        val etPhone = dialogView.findViewById<TextInputEditText>(R.id.etPhone)
        val actvSexo = dialogView.findViewById<AutoCompleteTextView>(R.id.actvSexo)
        val tilBirthDate = dialogView.findViewById<View>(R.id.tilBirthDate)
        
        tilBirthDate.visibility = View.GONE
        
        val sexos = arrayOf("Masculino", "Femenino")
        actvSexo.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sexos))
        
        val roles = arrayOf("ROLE_GANADERO", "ROLE_ADMIN")
        actvRole.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles))
        actvRole.setText("ROLE_GANADERO", false)
        layoutGanaderoFields.visibility = View.VISIBLE
        
        actvRole.setOnItemClickListener { _, _, position, _ ->
            layoutGanaderoFields.visibility = if (roles[position] == "ROLE_GANADERO") View.VISIBLE else View.GONE
        }
        
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
                    val dni = etDni.text.toString().trim()
                    val phone = etPhone.text.toString().trim()
                    val sexo = actvSexo.text.toString().trim()
                    
                    if (role == "ROLE_GANADERO" && (fullName.isEmpty() || dni.isEmpty())) {
                        Toast.makeText(this, "Para ganadero: nombre completo y DNI son requeridos", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    
                    val dniForUser = if (role == "ROLE_GANADERO") dni.ifEmpty { null } else null
                    createUser(CreateUserRequest(
                        username = username, 
                        password = password, 
                        email = email.ifEmpty { null }, 
                        fullName = fullName.ifEmpty { null }, 
                        role = role, 
                        dni = dniForUser,
                        telefono = phone.ifEmpty { null },
                        numeroAlpacas = null,
                        sexo = sexo.ifEmpty { null },
                        fechaNacimiento = null
                    ))
                } else {
                    Toast.makeText(this, "Usuario y contraseña son requeridos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun showEditUserDialog(user: UserDto) {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getUserDetail(user.id)
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        showEditUserDialogWithDetail(response.body()!!)
                    } else {
                        Toast.makeText(this@UsersActivity, "Error al cargar datos", Toast.LENGTH_SHORT).show()
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
    
    private fun showEditUserDialogWithDetail(user: com.alpaca.knm.data.remote.dto.UserDetailDto) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_user_form, null)
        val etUsername = dialogView.findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = dialogView.findViewById<TextInputEditText>(R.id.etPassword)
        val etEmail = dialogView.findViewById<TextInputEditText>(R.id.etEmail)
        val etFullName = dialogView.findViewById<TextInputEditText>(R.id.etFullName)
        val actvRole = dialogView.findViewById<AutoCompleteTextView>(R.id.actvRole)
        val layoutGanaderoFields = dialogView.findViewById<View>(R.id.layoutGanaderoFields)
        val etDni = dialogView.findViewById<TextInputEditText>(R.id.etDni)
        val etPhone = dialogView.findViewById<TextInputEditText>(R.id.etPhone)
        val actvSexo = dialogView.findViewById<AutoCompleteTextView>(R.id.actvSexo)
        val etBirthDate = dialogView.findViewById<TextInputEditText>(R.id.etBirthDate)
        
        etUsername.setText(user.username)
        etUsername.isEnabled = false
        etEmail.setText(user.email ?: "")
        etFullName.setText(user.fullName ?: "")
        
        val passwordLayout = etPassword.parent.parent as View
        passwordLayout.visibility = View.GONE
        
        val sexos = arrayOf("Masculino", "Femenino")
        actvSexo.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sexos))
        
        etDni.setText(user.dni ?: "")
        etPhone.setText(user.telefono ?: "")
        if (!user.sexo.isNullOrEmpty()) {
            actvSexo.setText(user.sexo, false)
        }
        
        var selectedBirthDate: String? = user.fechaNacimiento
        if (!user.fechaNacimiento.isNullOrEmpty()) {
            try {
                val parts = user.fechaNacimiento.split("-")
                if (parts.size == 3) {
                    etBirthDate.setText("${parts[2]}/${parts[1]}/${parts[0]}")
                }
            } catch (_: Exception) { }
        }
        etBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                selectedBirthDate = String.format("%04d-%02d-%02d", year, month + 1, day)
                etBirthDate.setText(String.format("%02d/%02d/%04d", day, month + 1, year))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        
        val roles = arrayOf("ROLE_GANADERO", "ROLE_ADMIN")
        actvRole.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles))
        actvRole.setText(user.role, false)
        
        if (user.role == "ROLE_GANADERO") {
            layoutGanaderoFields.visibility = View.VISIBLE
            etDni.isEnabled = false
        }
        
        actvRole.setOnItemClickListener { _, _, position, _ ->
            layoutGanaderoFields.visibility = if (roles[position] == "ROLE_GANADERO") View.VISIBLE else View.GONE
        }
        
        AlertDialog.Builder(this)
            .setTitle("Editar Usuario")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val email = etEmail.text.toString().trim()
                val fullName = etFullName.text.toString().trim()
                val role = actvRole.text.toString()
                val phone = etPhone.text.toString().trim()
                val sexo = actvSexo.text.toString().trim()
                
                updateUser(user.id, email.ifEmpty { null }, fullName.ifEmpty { null }, role, null, 
                    phone.ifEmpty { null }, sexo.ifEmpty { null }, selectedBirthDate)
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
    
    private fun updateUser(id: Int, email: String?, fullName: String?, role: String?, password: String?,
                          telefono: String? = null, sexo: String? = null, fechaNacimiento: String? = null) {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = com.alpaca.knm.data.remote.dto.UpdateUserRequest(
                    email, fullName, role, password, telefono, sexo, fechaNacimiento
                )
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
