package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.auth.SessionManager
import com.example.intermodular.data.remote.dto.CreateReviewDto
import com.example.intermodular.data.remote.dto.toReview
import com.example.intermodular.models.Review

class ReviewRepository(
    private val apiService: ApiService
) {
    suspend fun getReviewsByRoom(roomId: String): List<Review> {
        return apiService.getReviewsByRoom(roomId).map { it.toReview() }
    }

    suspend fun createReview(
        roomId: String,
        bookingId: String,
        rating: Int,
        description: String
    ): Review {
        val userId = SessionManager.getUserId()
            ?: throw IllegalStateException("Usuario no autenticado")

        val dto = CreateReviewDto(
            user = userId,
            room = roomId,
            booking = bookingId,
            rating = rating,
            description = description
        )

        return apiService.createReview(dto).toReview()
    }
}
