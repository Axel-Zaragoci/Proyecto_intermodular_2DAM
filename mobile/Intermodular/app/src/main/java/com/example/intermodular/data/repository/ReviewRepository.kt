package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.dto.toReview
import com.example.intermodular.models.Review

class ReviewRepository(
    private val apiService: ApiService
) {
    suspend fun getReviewsByRoom(roomId: String): List<Review> {
        return apiService.getReviewsByRoom(roomId).map { it.toReview() }
    }
}
