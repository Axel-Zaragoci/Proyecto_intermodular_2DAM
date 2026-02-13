package com.example.intermodular.data.remote.dto

import com.example.intermodular.models.Review
import com.squareup.moshi.Json

data class ReviewDto(
    @Json(name = "_id")
    val id: String,
    
    @Json(name = "user")
    val user: ReviewUserDto,
    
    @Json(name = "room")
    val room: String,
    
    @Json(name = "booking")
    val booking: ReviewBookingDto,
    
    @Json(name = "rating")
    val rating: Int,
    
    @Json(name = "description")
    val description: String,
    
    @Json(name = "createdAt")
    val createdAt: String,
    
    @Json(name = "updatedAt")
    val updatedAt: String
)

data class ReviewUserDto(
    @Json(name = "_id")
    val id: String,
    
    @Json(name = "firstName")
    val firstName: String?,
    
    @Json(name = "lastName")
    val lastName: String?,
    
    @Json(name = "email")
    val email: String?
)

data class ReviewBookingDto(
    @Json(name = "_id")
    val id: String,
    
    @Json(name = "checkInDate")
    val checkInDate: String?,
    
    @Json(name = "checkOutDate")
    val checkOutDate: String?
)

fun ReviewDto.toReview(): Review {
    val userName = buildString {
        if (user.firstName != null) append(user.firstName)
        if (user.firstName != null && user.lastName != null) append(" ")
        if (user.lastName != null) append(user.lastName)
    }.ifEmpty { "Usuario" }
    
    return Review(
        id = id,
        userId = user.id,
        userName = userName,
        roomId = room,
        bookingId = booking.id,
        rating = rating,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
