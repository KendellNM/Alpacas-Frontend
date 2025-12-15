package com.alpaca.knm.domain.model

data class UserProfile(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "",
    val location: String = "",
    val avatarUrl: String? = null,
    val birthDate: String? = null,
    val alpacasCount: Int? = null
)
