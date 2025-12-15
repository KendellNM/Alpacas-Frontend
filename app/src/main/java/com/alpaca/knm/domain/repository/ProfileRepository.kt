package com.alpaca.knm.domain.repository

import com.alpaca.knm.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getUserProfile(token: String): Result<UserProfile>
}
