package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.auth.SessionManager
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
        SessionManager.setSession(response.token, response.id)
        return response
    }

    fun logout() {
        SessionManager.clearSession()
    }
}