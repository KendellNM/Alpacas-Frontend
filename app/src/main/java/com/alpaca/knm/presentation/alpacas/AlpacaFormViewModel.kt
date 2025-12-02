package com.alpaca.knm.presentation.alpacas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.knm.domain.model.Alpaca
import com.alpaca.knm.domain.model.AlpacaEstado
import com.alpaca.knm.domain.model.AlpacaRaza
import com.alpaca.knm.domain.model.AlpacaSexo
import com.alpaca.knm.domain.usecase.alpaca.CreateAlpacaUseCase
import com.alpaca.knm.domain.usecase.alpaca.UpdateAlpacaUseCase
import kotlinx.coroutines.launch
import java.util.Date

class AlpacaFormViewModel(
    private val createAlpacaUseCase: CreateAlpacaUseCase,
    private val updateAlpacaUseCase: UpdateAlpacaUseCase
) : ViewModel() {
    
    private val _saveResult = MutableLiveData<SaveResult>()
    val saveResult: LiveData<SaveResult> = _saveResult
    
    fun saveAlpaca(
        id: Long?,
        ganaderoId: Long,
        nombre: String,
        raza: AlpacaRaza,
        color: String,
        edad: Int,
        peso: Double,
        sexo: AlpacaSexo,
        estado: AlpacaEstado,
        observaciones: String?
    ) {
        viewModelScope.launch {
            val alpaca = Alpaca(
                id = id ?: 0,
                ganaderoId = ganaderoId,
                nombre = nombre.trim(),
                raza = raza,
                color = color.trim(),
                edad = edad,
                peso = peso,
                sexo = sexo,
                estado = estado,
                fechaRegistro = Date(),
                observaciones = observaciones?.trim()
            )
            
            val result = if (id == null) {
                createAlpacaUseCase(alpaca)
            } else {
                updateAlpacaUseCase(id, alpaca)
            }
            
            result.fold(
                onSuccess = {
                    _saveResult.value = SaveResult.Success(
                        if (id == null) "Alpaca creada exitosamente" 
                        else "Alpaca actualizada exitosamente"
                    )
                },
                onFailure = { error ->
                    _saveResult.value = SaveResult.Error(
                        error.message ?: "Error al guardar alpaca"
                    )
                }
            )
        }
    }
    
    fun clearSaveResult() {
        _saveResult.value = null
    }
}

sealed class SaveResult {
    data class Success(val message: String) : SaveResult()
    data class Error(val message: String) : SaveResult()
}
