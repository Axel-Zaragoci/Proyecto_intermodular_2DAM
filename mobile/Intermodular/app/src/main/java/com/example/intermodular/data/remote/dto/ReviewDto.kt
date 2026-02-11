package com.example.intermodular.data.remote.dto

data class ReviewDto(
    val _id: String,
    val user: UserDto?,
    val room: String?,
    val booking: BookingInfoDto?,
    val rating: Int,
    val description: String,
    val createdAt: String,
    val updatedAt: String
)

data class UserDto(
    val _id: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?
)

data class BookingInfoDto(
    val _id: String?,
    val checkInDate: String?,
    val checkOutDate: String?
)
