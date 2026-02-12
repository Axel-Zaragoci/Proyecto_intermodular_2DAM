package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.auth.TokenProviderImpl
import com.example.intermodular.data.remote.dto.LoginDto

class LoginRepository(
    private val api: ApiService
) {

    suspend fun login(email: String, password: String): LoginDto {

        val response = api.login(
            mapOf(
                "email" to email,
                "password" to password
            )
        )

        TokenProviderImpl.setToken(response.token)

        return response
    }

    fun logout() {
        TokenProviderImpl.clearToken()
    }
}