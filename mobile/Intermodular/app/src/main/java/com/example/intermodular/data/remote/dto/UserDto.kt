package com.example.intermodular.data.remote.dto

import java.time.Instant


data class UserDto(
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val dni: String?,
    val phoneNumber: Long?,
    val birthDate: Instant,
    val cityName: String?,
    val gender: String?,
    val imageRoute: String?,
    val vipStatus: Boolean?
)