package com.example.intermodular.models

import java.time.LocalDate

data class Booking(
    val id: String,
    val roomId: String,
    val clientId: String,
    val checkInDate: LocalDate,
    val checkOutDate: LocalDate,
    val payDate: LocalDate,
    val totalPrice: Int,
    val pricePerNight: Int,
    val offer: Int,
    val status: String,
    val guests: Int,
    val totalNights: Int
)