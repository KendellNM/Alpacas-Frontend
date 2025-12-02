package com.alpaca.knm.data.repository

import com.alpaca.knm.data.source.remote.ProfileRemoteDataSource
import com.alpaca.knm.domain.model.UserProfile
import com.alpaca.knm.domain.repository.ProfileRepository

/**
 * Implementación del repositorio de perfil
 */
class ProfileRepositoryImpl(
    private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    
    override suspend fun getUserProfile(token: String): Result<UserProfile> {
        return try {
            val profile = remoteDataSource.getUserProfile(token)
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
