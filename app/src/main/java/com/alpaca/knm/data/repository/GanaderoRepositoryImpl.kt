package com.alpaca.knm.data.repository

import com.alpaca.knm.data.source.remote.GanaderoRemoteDataSource
import com.alpaca.knm.domain.model.Ganadero
import com.alpaca.knm.domain.repository.GanaderoRepository

/**
 * Implementación del repositorio de Ganaderos
 */
class GanaderoRepositoryImpl(
    private val remoteDataSource: GanaderoRemoteDataSource
) : GanaderoRepository {
    
    override suspend fun getGanaderos(token: String): Result<List<Ganadero>> {
        return try {
            val ganaderos = remoteDataSource.getGanaderos(token)
            Result.success(ganaderos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getGanaderoById(token: String, id: String): Result<Ganadero> {
        return try {
            val ganadero = remoteDataSource.getGanaderoById(token, id)
            Result.success(ganadero)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createGanadero(token: String, ganadero: Ganadero): Result<Ganadero> {
        return try {
            val created = remoteDataSource.createGanadero(token, ganadero)
            Result.success(created)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateGanadero(
        token: String,
        id: String,
        ganadero: Ganadero
    ): Result<Ganadero> {
        return try {
            val updated = remoteDataSource.updateGanadero(token, id, ganadero)
            Result.success(updated)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteGanadero(token: String, id: String): Result<Unit> {
        return try {
            remoteDataSource.deleteGanadero(token, id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
