package com.alpaca.knm.data.remote.datasource

import com.alpaca.knm.data.remote.ErrorHandler
import com.alpaca.knm.data.remote.RetrofitClient
import com.alpaca.knm.data.remote.api.ProfileApiService
import com.alpaca.knm.domain.model.ApiException
import com.alpaca.knm.domain.model.UserProfile

class ProfileRemoteDataSource {
    
    private val apiService: ProfileApiService = 
        RetrofitClient.createService(ProfileApiService::class.java)
    
    suspend fun getUserProfile(token: String): UserProfile {
        try {
            val response = apiService.getUserProfile("Bearer $token")
            
            if (response.isSuccessful) {
                val body = response.body() 
                    ?: throw ApiException.ServerException("Respuesta vacia del servidor")
                
                return UserProfile(
                    id = body.id,
                    firstName = body.firstName,
                    lastName = body.lastName,
                    gender = body.gender,
                    location = body.location,
                    avatarUrl = body.avatarUrl,
                    birthDate = body.birthDate,
                    alpacasCount = body.alpacasCount
                )
            } else {
                throw ErrorHandler.handleError(response)
            }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ErrorHandler.handleException(e)
        }
    }
}
