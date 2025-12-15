package com.alpaca.knm.data.remote.api

import com.alpaca.knm.data.remote.dto.UserDto
import com.alpaca.knm.data.remote.dto.UserDetailDto
import com.alpaca.knm.data.remote.dto.CreateUserRequest
import com.alpaca.knm.data.remote.dto.UpdateUserRequest
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {
    
    @GET("users/")
    suspend fun getAllUsers(): Response<List<UserDto>>
    
    @GET("users/{id}")
    suspend fun getUserDetail(@Path("id") id: Int): Response<UserDetailDto>
    
    @POST("users/")
    suspend fun createUser(@Body request: CreateUserRequest): Response<UserDto>
    
    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body request: UpdateUserRequest): Response<UserDto>
    
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
}
