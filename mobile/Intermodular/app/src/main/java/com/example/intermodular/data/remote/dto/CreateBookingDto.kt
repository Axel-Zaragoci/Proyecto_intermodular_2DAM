package com.example.intermodular.data.remote.dto

data class CreateBookingDto(
    val client: String,
    val room: String,
    val checkInDate: Long,
    val checkOutDate: Long,
    val guests: Int
)
