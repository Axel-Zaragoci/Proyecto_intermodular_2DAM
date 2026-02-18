package com.example.intermodular.data.remote.dto

class RegisterDto(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val dni: String,
    val phoneNumber: Long?,
    val birthDate: String,
    val cityName: String,
    val gender: String
)