package com.alpaca.knm.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    
    private const val BASE_URL = "https://proyecto-alpacas.onrender.com/api/"
    private var authToken: String? = null
    
    fun setAuthToken(token: String?) { authToken = token }
    fun getAuthToken(): String? = authToken
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = authToken
        val newRequest = if (token != null && !originalRequest.url.encodedPath.contains("auth/login")) {
            originalRequest.newBuilder().header("Authorization", "Bearer $token").build()
        } else {
            originalRequest
        }
        chain.proceed(newRequest)
    }
    
    private val renderWakeUpInterceptor = RenderWakeUpInterceptor()
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(renderWakeUpInterceptor)
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
    
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    fun <T> createService(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}
