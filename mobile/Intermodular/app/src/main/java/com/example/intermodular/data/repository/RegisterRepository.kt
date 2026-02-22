package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.dto.RegisterDto

class RegisterRepository(
    private val api: ApiService,
    private val loginRepository: LoginRepository
) {
    suspend fun registerAndLogin(body: RegisterDto) {
        api.register(body)
        loginRepository.login(body.email, body.password)
    }
}