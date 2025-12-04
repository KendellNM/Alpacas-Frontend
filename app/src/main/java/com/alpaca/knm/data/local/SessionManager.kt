package com.alpaca.knm.data.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Maneja la sesión del usuario (token JWT y datos básicos)
 */
object SessionManager {
    private const val PREF_NAME = "alpaca_session"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_ROLE = "user_role"
    
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    
    fun saveSession(context: Context, token: String, userId: Int, userName: String, role: String) {
        getPrefs(context).edit().apply {
            putString(KEY_TOKEN, token)
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_ROLE, role)
            apply()
        }
    }
    
    fun getToken(context: Context): String? {
        return getPrefs(context).getString(KEY_TOKEN, null)
    }
    
    fun getUserId(context: Context): Int {
        return getPrefs(context).getInt(KEY_USER_ID, 0)
    }
    
    fun getUserName(context: Context): String? {
        return getPrefs(context).getString(KEY_USER_NAME, null)
    }
    
    fun getUserRole(context: Context): String? {
        return getPrefs(context).getString(KEY_USER_ROLE, null)
    }
    
    fun isLoggedIn(context: Context): Boolean {
        return getToken(context) != null
    }
    
    fun clearSession(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
