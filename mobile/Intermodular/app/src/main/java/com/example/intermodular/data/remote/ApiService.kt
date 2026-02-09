package com.example.intermodular.data.remote

import com.example.intermodular.data.remote.dto.BookingDto
import com.example.intermodular.data.remote.dto.RoomDto
import com.example.intermodular.data.remote.dto.RoomsResponseDto
import retrofit2.http.GET

interface ApiService {
    @GET("booking")
    suspend fun getBookings() : List<BookingDto>

    @GET("room")
    suspend fun getRooms() : RoomsResponseDto
}