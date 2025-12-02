package com.alpaca.knm.utils.extensions

/**
 * Extensiones para String
 */

fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(minLength: Int = 6): Boolean {
    return this.length >= minLength
}
