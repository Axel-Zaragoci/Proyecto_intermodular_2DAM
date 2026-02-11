package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.mapper.toDomain
import com.example.intermodular.models.Review

class ReviewRepository(
    private val api: ApiService
) {
    suspend fun getReviewsByRoom(roomId: String): List<Review> {
        return api.getReviewsByRoom(roomId).map { it.toDomain() }
    }
}
