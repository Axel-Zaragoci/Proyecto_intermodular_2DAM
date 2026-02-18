package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.auth.SessionManager
import com.example.intermodular.data.remote.dto.ApiError
import com.example.intermodular.data.remote.dto.LoginDto
import com.google.gson.Gson
import retrofit2.HttpException

class LoginRepository(
    private val api: ApiService
) {
    suspend fun login(email: String, password: String): LoginDto {
        try {
            val response = api.login(
                mapOf(
                    "email" to email,
                    "password" to password
                )
            )
            SessionManager.setSession(response.token, response.id)

            return response

        } catch (e: HttpException) {
            val errorJson = e.response()?.errorBody()?.string()

            val backendMessage = try {
                Gson().fromJson(errorJson, ApiError::class.java)?.error
            } catch (_: Exception) {
                null
            }

            throw Exception(backendMessage ?: "Error ${e.code()}")
        } catch (e: Exception) {
            throw Exception(e.message ?: "Error de conexión")
        }
    }
    fun logout() {
        SessionManager.clearSession()
    }
}