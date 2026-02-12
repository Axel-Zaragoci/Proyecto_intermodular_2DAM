package com.example.intermodular.data.remote

import com.example.intermodular.BuildConfig
import com.example.intermodular.data.remote.auth.AuthInterceptor
import com.example.intermodular.data.remote.auth.SessionManager
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitProvider {

    private fun moshi(): Moshi =
        Moshi.Builder()
            .add(InstantJsonAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

    private fun okHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(SessionManager))
            .build()

    private fun retrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi()))
            .build()

    val api: ApiService by lazy {
        retrofit().create(ApiService::class.java)
    }
}