package com.alpaca.knm.domain.usecase

/**
 * Caso de uso: Validar credenciales
 * Valida el formato y requisitos de las credenciales
 */
class ValidateCredentialsUseCase {
    
    data class ValidationResult(
        val isValid: Boolean,
        val usernameError: String? = null,
        val passwordError: String? = null
    )
    
    operator fun invoke(username: String, password: String): ValidationResult {
        val usernameError = validateUsername(username)
        val passwordError = validatePassword(password)
        
        return ValidationResult(
            isValid = usernameError == null && passwordError == null,
            usernameError = usernameError,
            passwordError = passwordError
        )
    }
    
    private fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "El usuario es requerido"
            username.length < 3 -> "El usuario debe tener al menos 3 caracteres"
            else -> null
        }
    }
    
    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "La contraseña es requerida"
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }
}
