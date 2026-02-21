package com.example.intermodular.data.remote.dto

data class CreateReviewDto (
    val user: String,
    val room: String,
    val booking: String,
    val rating: Int,
    val description: String
)