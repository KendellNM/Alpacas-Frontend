package com.alpaca.knm.presentation.alpacas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.domain.repository.AlpacaRegistroRepository

class AlpacaRegistroViewModelFactory(
    private val repository: AlpacaRegistroRepository
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlpacaRegistroViewModel::class.java)) {
            return AlpacaRegistroViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
