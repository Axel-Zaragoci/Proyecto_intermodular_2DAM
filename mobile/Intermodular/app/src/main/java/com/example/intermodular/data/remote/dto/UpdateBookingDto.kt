package com.example.intermodular.data.remote.dto

data class UpdateBookingDto(
    val checkInDate: Long,
    val checkOutDate: Long,
    val guests: Int
)