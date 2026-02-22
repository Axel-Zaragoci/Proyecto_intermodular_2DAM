package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.auth.SessionManager
import com.example.intermodular.data.remote.dto.ApiError
import com.example.intermodular.data.remote.dto.LoginDto
import com.example.intermodular.data.remote.dto.RegisterDto
import com.google.gson.Gson
import retrofit2.HttpException

class RegisterRepository(
    private val api: ApiService,
    private val loginRepository: LoginRepository
) {
    suspend fun registerAndLogin(body: RegisterDto) {
        try {
            api.register(body)
            loginRepository.login(body.email, body.password)
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
}
