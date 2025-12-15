package com.alpaca.knm.data.repository

import com.alpaca.knm.data.remote.datasource.GanaderoRemoteDataSource
import com.alpaca.knm.domain.model.Ganadero
import com.alpaca.knm.domain.repository.GanaderoRepository

class GanaderoRepositoryImpl(
    private val remoteDataSource: GanaderoRemoteDataSource
) : GanaderoRepository {
    
    override suspend fun getGanaderos(token: String): Result<List<Ganadero>> {
        return try {
            Result.success(remoteDataSource.getGanaderos(token))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getGanaderoById(token: String, id: String): Result<Ganadero> {
        return try {
            Result.success(remoteDataSource.getGanaderoById(token, id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createGanadero(token: String, ganadero: Ganadero): Result<Ganadero> {
        return try {
            Result.success(remoteDataSource.createGanadero(token, ganadero))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateGanadero(token: String, id: String, ganadero: Ganadero): Result<Ganadero> {
        return try {
            Result.success(remoteDataSource.updateGanadero(token, id, ganadero))
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
