package com.example.intermodular.data.remote.dto

import java.time.Instant

data class BookingDto (
    val _id : String,
    val room: String,
    val client: String,
    val checkInDate : Instant,
    val checkOutDate: Instant,
    val payDate: Instant,
    val totalPrice: Int,
    val pricePerNight: Int,
    val offer: Int,
    val status: String,
    val guests: Int,
    val totalNights: Int
)