package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.auth.SessionManager
import com.example.intermodular.data.remote.dto.CreateBookingDto
import com.example.intermodular.data.remote.mapper.toDomain
import com.example.intermodular.models.Booking

class BookingRepository(
    private val api: ApiService
) {

    suspend fun getBookings(): List<Booking> {
        return api.getBookings()
            .map { it.toDomain() }
    }

    suspend fun getBookingById(id : String) : Booking {
        return api.getBookingById(id).toDomain()
    }

    suspend fun getBookingsByUserId(id: String) : List<Booking> {
        return api.getBookingsByUserId(id)
            .map { it.toDomain() }
    }

    suspend fun createBooking(
        roomId: String,
        checkIn: Long,
        checkOut: Long,
        guests: Int
    ): Booking {

        val clientId = SessionManager.getUserId()
            ?: throw IllegalStateException("Usuario no autenticado")

        val dto = CreateBookingDto(
            client = clientId,
            room = roomId,
            checkInDate = checkIn,
            checkOutDate = checkOut,
            guests = guests
        )

        return api.createBooking(dto).toDomain()
    }

}
