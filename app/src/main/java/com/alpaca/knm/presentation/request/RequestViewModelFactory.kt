package com.alpaca.knm.presentation.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.domain.usecase.CreateAdvanceRequestUseCase

/**
 * Factory para crear instancias de RequestViewModel
 */
class RequestViewModelFactory(
    private val createAdvanceRequestUseCase: CreateAdvanceRequestUseCase
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RequestViewModel::class.java)) {
            return RequestViewModel(createAdvanceRequestUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
