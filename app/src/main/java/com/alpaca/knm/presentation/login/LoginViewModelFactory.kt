package com.alpaca.knm.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.domain.usecase.LoginUseCase
import com.alpaca.knm.domain.usecase.ValidateCredentialsUseCase

/**
 * Factory para crear instancias de LoginViewModel
 */
class LoginViewModelFactory(
    private val loginUseCase: LoginUseCase,
    private val validateCredentialsUseCase: ValidateCredentialsUseCase
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginUseCase, validateCredentialsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
