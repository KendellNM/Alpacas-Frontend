package com.alpaca.knm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("role")
    val role: String,
    @SerializedName("is_active")
    val isActive: Boolean
)

data class UserDetailDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("role")
    val role: String,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("dni")
    val dni: String?,
    @SerializedName("telefono")
    val telefono: String?,
    @SerializedName("sexo")
    val sexo: String?,
    @SerializedName("fecha_nacimiento")
    val fechaNacimiento: String?
)

data class CreateUserRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("email")
    val email: String?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("role")
    val role: String = "ROLE_GANADERO",
    @SerializedName("dni")
    val dni: String? = null,
    @SerializedName("telefono")
    val telefono: String? = null,
    @SerializedName("numero_alpacas")
    val numeroAlpacas: Int? = null,
    @SerializedName("sexo")
    val sexo: String? = null,
    @SerializedName("fecha_nacimiento")
    val fechaNacimiento: String? = null
)

data class UpdateUserRequest(
    @SerializedName("email")
    val email: String?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("telefono")
    val telefono: String? = null,
    @SerializedName("sexo")
    val sexo: String? = null,
    @SerializedName("fecha_nacimiento")
    val fechaNacimiento: String? = null
)
