package com.alpaca.knm.data.repository

import com.alpaca.knm.data.remote.datasource.ProfileRemoteDataSource
import com.alpaca.knm.domain.model.UserProfile
import com.alpaca.knm.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    
    override suspend fun getUserProfile(token: String): Result<UserProfile> {
        return try {
            Result.success(remoteDataSource.getUserProfile(token))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
