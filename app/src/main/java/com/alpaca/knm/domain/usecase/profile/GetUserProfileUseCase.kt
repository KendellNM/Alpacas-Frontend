package com.alpaca.knm.domain.usecase.profile

import com.alpaca.knm.domain.model.UserProfile
import com.alpaca.knm.domain.repository.AuthRepository
import com.alpaca.knm.domain.repository.ProfileRepository

class GetUserProfileUseCase(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<UserProfile> {
        val token = authRepository.getCurrentUser()?.token
            ?: return Result.failure(Exception("No token available"))
        
        return profileRepository.getUserProfile(token)
    }
}
