package com.alpaca.knm.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alpaca.knm.domain.usecase.auth.LogoutUseCase
import com.alpaca.knm.domain.usecase.profile.GetUserProfileUseCase

class ProfileViewModelFactory(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(getUserProfileUseCase, logoutUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
