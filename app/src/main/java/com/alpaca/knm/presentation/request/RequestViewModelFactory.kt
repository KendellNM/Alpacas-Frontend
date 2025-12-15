package com.alpaca.knm.presentation.request

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.domain.usecase.advance.CreateAdvanceRequestUseCase

class RequestViewModelFactory(
    private val application: Application,
    private val createAdvanceRequestUseCase: CreateAdvanceRequestUseCase,
    private val ganaderoId: Long
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RequestViewModel::class.java)) {
            return RequestViewModel(application, createAdvanceRequestUseCase, ganaderoId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
