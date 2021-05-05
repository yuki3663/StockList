package com.tom.stocktable.network

import com.riningan.retrofit2.converter.csv.CsvConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkManager
{
    fun provideOkHttpClient(): OkHttpClient {
        val httpRequestInterceptor: HttpRequestInterceptor = HttpRequestInterceptor()
        return OkHttpClient.Builder()
            .addInterceptor(httpRequestInterceptor)
            .build()
    }

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(STOCK_PRICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}