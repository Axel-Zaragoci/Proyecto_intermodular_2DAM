package com.example.intermodular.data.remote.dto

data class UpdateUserResponseDto(
    val message: String,
    val user: UserDto
)

data class UpdateUserRequestDto(
    val id: String?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val dni: String,
    val phoneNumber: Long?,
    val birthDate: String,
    val cityName: String,
    val gender: String,
    val imageRoute: String?
)