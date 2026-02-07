package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.mapper.toDomain
import com.example.intermodular.models.Booking

class BookingRepository(
    private val api: ApiService
) {

    suspend fun getBookings(): List<Booking> {
        return api.getBookings()
            .map { it.toDomain() }
    }
}
