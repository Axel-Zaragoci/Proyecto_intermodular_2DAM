package com.example.intermodular.data.remote.mapper

import com.example.intermodular.data.remote.dto.RoomDto
import com.example.intermodular.models.Room

fun RoomDto.toDomain(): Room {
    return Room(
        id = _id,
        type = type,
        roomNumber = roomNumber,
        maxGuests = maxGuests,
        description = description,
        mainImage = mainImage,
        pricePerNight = pricePerNight,
        extraBed = extraBed,
        crib = crib,
        offer = offer,
        extras = extras,
        extraImages = extraImages,
        isAvailable = isAvailable,
        rate = rate
    )
}
