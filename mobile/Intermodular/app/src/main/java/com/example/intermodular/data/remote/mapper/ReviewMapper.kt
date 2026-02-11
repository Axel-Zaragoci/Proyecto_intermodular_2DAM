package com.example.intermodular.data.remote.mapper

import com.example.intermodular.data.remote.dto.ReviewDto
import com.example.intermodular.models.Review

fun ReviewDto.toDomain(): Review {
    return Review(
        id = _id,
        userId = user?._id ?: "",
        userName = "${user?.firstName ?: ""} ${user?.lastName ?: ""}".trim(),
        roomId = room ?: "",
        bookingId = booking?._id ?: "",
        rating = rating,
        description = description,
        createdAt = createdAt
    )
}
