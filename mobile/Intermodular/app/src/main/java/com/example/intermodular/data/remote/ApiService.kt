package com.example.intermodular.data.remote

import com.example.intermodular.data.remote.dto.BookingDto
import retrofit2.http.GET

interface ApiService {
    @GET("booking")
    suspend fun getBookings() : List<BookingDto>
}