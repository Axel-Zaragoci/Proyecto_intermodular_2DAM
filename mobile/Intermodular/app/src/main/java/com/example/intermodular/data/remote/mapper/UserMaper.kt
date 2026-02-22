package com.example.intermodular.data.remote.mapper

import com.example.intermodular.data.remote.dto.UserDto
import com.example.intermodular.models.UserModel
import java.time.ZoneOffset

fun UserDto.toUserModel(): UserModel {
    return UserModel(
        firstName = firstName.orEmpty(),
        lastName = lastName.orEmpty(),
        email = email.orEmpty(),
        dni = dni.orEmpty(),
        phoneNumber = phoneNumber,
        birthDate = birthDate
            .atZone(ZoneOffset.UTC)
            .toLocalDate(),
        cityName = cityName.orEmpty(),
        gender = gender.orEmpty(),
        imageRoute = imageRoute,
        vipStatus = vipStatus ?: false
    )
}