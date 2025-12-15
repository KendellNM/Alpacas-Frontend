package com.alpaca.knm.data.source.local

import android.content.Context
import android.content.SharedPreferences
import com.alpaca.knm.domain.model.User

class AuthLocalDataSource(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    fun saveUser(user: User) {
        prefs.edit().apply {
            putString(KEY_USER_ID, user.id)
            putString(KEY_USERNAME, user.username)
            putString(KEY_EMAIL, user.email)
            putString(KEY_TOKEN, user.token)
            putString(KEY_ROLE, user.role)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }
    
    fun getCurrentUser(): User? {
        if (!isUserLoggedIn()) return null
        
        val id = prefs.getString(KEY_USER_ID, "") ?: ""
        val username = prefs.getString(KEY_USERNAME, "") ?: ""
        val email = prefs.getString(KEY_EMAIL, "") ?: ""
        val token = prefs.getString(KEY_TOKEN, null)
        val role = prefs.getString(KEY_ROLE, null)
        
        return if (username.isNotEmpty()) {
            User(id, username, email, token, role)
        } else {
            null
        }
    }
    
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }
    
    fun isUserLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    fun clearUser() {
        prefs.edit().clear().apply()
    }
    
    companion object {
        private const val PREFS_NAME = "alpaca_auth_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_TOKEN = "token"
        private const val KEY_ROLE = "role"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
}
