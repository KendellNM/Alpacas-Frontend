package com.alpaca.knm.data.repository

import com.alpaca.knm.data.remote.datasource.AlpacaRemoteDataSource
import com.alpaca.knm.data.remote.dto.AlpacaCreateRequest
import com.alpaca.knm.domain.model.Alpaca
import com.alpaca.knm.domain.repository.AlpacaRepository

class AlpacaRepositoryImpl(
    private val remoteDataSource: AlpacaRemoteDataSource
) : AlpacaRepository {
    
    override suspend fun getAllAlpacas(): Result<List<Alpaca>> {
        return remoteDataSource.getAllAlpacas()
            .map { dtos -> dtos.map { it.toDomain() } }
    }
    
    override suspend fun getAlpacasByGanadero(ganaderoId: Long): Result<List<Alpaca>> {
        return remoteDataSource.getAlpacasByGanadero(ganaderoId)
            .map { dtos -> dtos.map { it.toDomain() } }
    }
    
    override suspend fun getAlpacaById(id: Long): Result<Alpaca> {
        return remoteDataSource.getAlpacaById(id)
            .map { it.toDomain() }
    }
    
    override suspend fun createAlpaca(alpaca: Alpaca): Result<Alpaca> {
        val request = AlpacaCreateRequest(
            ganaderoId = alpaca.ganaderoId,
            nombre = alpaca.nombre,
            raza = alpaca.raza.name,
            color = alpaca.color,
            edad = alpaca.edad,
            peso = alpaca.peso,
            sexo = alpaca.sexo.name,
            estado = alpaca.estado.name,
            observaciones = alpaca.observaciones
        )
        return remoteDataSource.createAlpaca(request)
            .map { it.toDomain() }
    }
    
    override suspend fun updateAlpaca(id: Long, alpaca: Alpaca): Result<Alpaca> {
        val request = AlpacaCreateRequest(
            ganaderoId = alpaca.ganaderoId,
            nombre = alpaca.nombre,
            raza = alpaca.raza.name,
            color = alpaca.color,
            edad = alpaca.edad,
            peso = alpaca.peso,
            sexo = alpaca.sexo.name,
            estado = alpaca.estado.name,
            observaciones = alpaca.observaciones
        )
        return remoteDataSource.updateAlpaca(id, request)
            .map { it.toDomain() }
    }
    
    override suspend fun deleteAlpaca(id: Long): Result<Unit> {
        return remoteDataSource.deleteAlpaca(id)
    }
}
