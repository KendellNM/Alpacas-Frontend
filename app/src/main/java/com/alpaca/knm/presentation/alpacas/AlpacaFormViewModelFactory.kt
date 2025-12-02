package com.alpaca.knm.presentation.alpacas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.domain.usecase.alpaca.CreateAlpacaUseCase
import com.alpaca.knm.domain.usecase.alpaca.UpdateAlpacaUseCase

class AlpacaFormViewModelFactory(
    private val createAlpacaUseCase: CreateAlpacaUseCase,
    private val updateAlpacaUseCase: UpdateAlpacaUseCase
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlpacaFormViewModel::class.java)) {
            return AlpacaFormViewModel(
                createAlpacaUseCase,
                updateAlpacaUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
