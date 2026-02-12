package com.example.intermodular.data.remote

import com.example.intermodular.data.remote.dto.BookingDto
import com.example.intermodular.data.remote.dto.CreateBookingDto
import com.example.intermodular.data.remote.dto.LoginDto
import com.example.intermodular.data.remote.dto.RoomsResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("booking")
    suspend fun getBookings() : List<BookingDto>

    @GET("booking/{id}")
    suspend fun getBookingById(@Path("id") id : String) : BookingDto

    @GET("booking/client/{id}")
    suspend fun getBookingsByUserId(@Path("id") id: String) : List<BookingDto>

    @POST("booking")
    suspend fun createBooking(
        @Body body: CreateBookingDto
    ): BookingDto


    @GET("room")
    suspend fun getRooms(
        @Query("type") type: String? = null,
        @Query("isAvailable") isAvailable: Boolean? = null,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("guests") guests: Int? = null,
        @Query("hasExtraBed") hasExtraBed: Boolean? = null,
        @Query("hasCrib") hasCrib: Boolean? = null,
        @Query("hasOffer") hasOffer: Boolean? = null,
        @Query("extras") extras: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null
    ) : RoomsResponseDto

    @POST("auth/login")
    suspend fun login(
        @Body body: Map<String, String>
    ): LoginDto
}