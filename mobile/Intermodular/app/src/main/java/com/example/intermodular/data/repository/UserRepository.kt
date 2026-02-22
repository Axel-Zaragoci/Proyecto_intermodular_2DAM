package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.auth.SessionManager
import com.example.intermodular.data.remote.dto.UpdateUserRequestDto
import com.example.intermodular.data.remote.mapper.toUserModel
import com.example.intermodular.models.UserModel
import okhttp3.MultipartBody
import java.time.format.DateTimeFormatter

class UserRepository(
    private val api: ApiService
) {
    suspend fun getMe(): UserModel {
        return api.getMe().toUserModel()
    }
    suspend fun updateProfilePhoto(
        currentUser: UserModel,
        photoPart: MultipartBody.Part
    ): UserModel {

        val upload = api.uploadPhoto(photoPart)

        val body = UpdateUserRequestDto(
            id = SessionManager.getUserId(),
            firstName = currentUser.firstName,
            lastName = currentUser.lastName,
            email = currentUser.email,
            dni = currentUser.dni,
            phoneNumber = currentUser.phoneNumber,
            birthDate = currentUser.birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
            cityName = currentUser.cityName,
            gender = currentUser.gender,
            imageRoute = upload.url
        )

        val updated = api.updateUser(body).user
        return updated.toUserModel()
    }
}