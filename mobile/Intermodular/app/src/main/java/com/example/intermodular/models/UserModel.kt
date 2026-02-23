package com.example.intermodular.models

import java.time.LocalDate

data class UserModel(
    val firstName: String,
    val lastName: String,
    val email: String,
    val dni: String,
    val phoneNumber: Long?,
    val birthDate: LocalDate,
    val cityName: String,
    val gender: String,
    val imageRoute: String?,
    val vipStatus: Boolean
)