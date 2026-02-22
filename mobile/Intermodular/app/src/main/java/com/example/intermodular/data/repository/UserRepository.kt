package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.mapper.toUserModel
import com.example.intermodular.models.UserModel

class UserRepository(
    private val api: ApiService
) {
    suspend fun getMe(): UserModel {
        return api.getMe().toUserModel()
    }
}