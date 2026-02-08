package com.example.intermodular.data.remote

import com.example.intermodular.data.remote.dto.BookingDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("booking")
    suspend fun getBookings() : List<BookingDto>

    @GET("booking/{id}")
    suspend fun getBookingById(@Path("id") id : String) : BookingDto
}