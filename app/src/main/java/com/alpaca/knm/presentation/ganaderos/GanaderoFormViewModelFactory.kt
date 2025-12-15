package com.alpaca.knm.presentation.ganaderos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.domain.usecase.ganadero.CreateGanaderoUseCase
import com.alpaca.knm.domain.usecase.ganadero.UpdateGanaderoUseCase

class GanaderoFormViewModelFactory(
    private val createGanaderoUseCase: CreateGanaderoUseCase,
    private val updateGanaderoUseCase: UpdateGanaderoUseCase
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GanaderoFormViewModel::class.java)) {
            return GanaderoFormViewModel(createGanaderoUseCase, updateGanaderoUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
