package com.alpaca.knm.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException

class RenderWakeUpInterceptor : Interceptor {
    
    companion object {
        var isRenderWakingUp = false
            private set
        
        fun setRenderWakingUp(waking: Boolean) {
            isRenderWakingUp = waking
        }
        
        fun reset() {
            isRenderWakingUp = false
        }
    }
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()
        
        return try {
            val response = chain.proceed(request)
            val duration = System.currentTimeMillis() - startTime
            
            if (response.isSuccessful) {
                setRenderWakingUp(false)
            } else if (request.url.host.contains("onrender.com") && duration > 10000) {
                setRenderWakingUp(true)
            }
            
            response
        } catch (e: Exception) {
            val duration = System.currentTimeMillis() - startTime
            
            when (e) {
                is SocketTimeoutException, is IOException -> {
                    if (request.url.host.contains("onrender.com")) {
                        setRenderWakingUp(true)
                    }
                }
            }
            throw e
        }
    }
}