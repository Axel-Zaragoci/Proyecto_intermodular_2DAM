package com.example.intermodular.models

data class Review(
    val id: String,
    val userId: String,
    val userName: String,
    val roomId: String,
    val bookingId: String,
    val rating: Int,
    val description: String,
    val createdAt: String
)
