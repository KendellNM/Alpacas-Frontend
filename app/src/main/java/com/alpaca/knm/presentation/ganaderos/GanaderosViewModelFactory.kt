package com.alpaca.knm.presentation.ganaderos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.domain.usecase.ganadero.DeleteGanaderoUseCase
import com.alpaca.knm.domain.usecase.ganadero.GetGanaderosUseCase

class GanaderosViewModelFactory(
    private val getGanaderosUseCase: GetGanaderosUseCase,
    private val deleteGanaderoUseCase: DeleteGanaderoUseCase
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GanaderosViewModel::class.java)) {
            return GanaderosViewModel(getGanaderosUseCase, deleteGanaderoUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
