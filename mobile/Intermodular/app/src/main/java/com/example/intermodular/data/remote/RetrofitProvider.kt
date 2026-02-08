package com.example.intermodular.data.remote

import com.example.intermodular.BuildConfig
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitProvider {

    private fun retrofit(): Retrofit {
        val moshi = Moshi.Builder()
            .add(InstantJsonAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }


    val api: ApiService by lazy {
        retrofit().create(ApiService::class.java)
    }
}