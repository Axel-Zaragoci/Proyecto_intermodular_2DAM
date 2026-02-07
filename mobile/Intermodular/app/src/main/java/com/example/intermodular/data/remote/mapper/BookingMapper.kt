package com.example.intermodular.data.remote.mapper


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.intermodular.data.remote.dto.BookingDto
import com.example.intermodular.models.Booking
import java.time.ZoneOffset


fun BookingDto.toDomain() : Booking {
    return Booking(
        id = _id,
        roomId = room,
        clientId = client,
        checkInDate = checkInDate
            .atZone(ZoneOffset.UTC)
            .toLocalDate(),
        checkOutDate = checkOutDate
            .atZone(ZoneOffset.UTC)
            .toLocalDate(),
        payDate = payDate
            .atZone(ZoneOffset.UTC)
            .toLocalDate(),
        totalPrice = totalPrice,
        pricePerNight = pricePerNight,
        offer = offer,
        status = status,
        guests = guests,
        totalNights = totalNights
    )
}