package com.tom.stocktable.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

//TODO 只是記log 攔quest response 中間要做的事情
class HttpRequestInterceptor : Interceptor {
    val TAG = this.javaClass.name
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder().url(originalRequest.url()).build()
        Log.d(TAG, request.toString())
        return chain.proceed(request)
    }
}