package com.alpaca.knm.presentation.alpacas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.domain.usecase.alpaca.DeleteAlpacaUseCase
import com.alpaca.knm.domain.usecase.alpaca.GetAllAlpacasUseCase
import com.alpaca.knm.domain.usecase.alpaca.GetAlpacasByGanaderoUseCase

class AlpacasViewModelFactory(
    private val getAllAlpacasUseCase: GetAllAlpacasUseCase,
    private val getAlpacasByGanaderoUseCase: GetAlpacasByGanaderoUseCase,
    private val deleteAlpacaUseCase: DeleteAlpacaUseCase
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlpacasViewModel::class.java)) {
            return AlpacasViewModel(
                getAllAlpacasUseCase,
                getAlpacasByGanaderoUseCase,
                deleteAlpacaUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
