package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.mapper.toDomain
import com.example.intermodular.models.Room

import com.example.intermodular.models.RoomFilter

class RoomRepository(
    private val api: ApiService
) {

    suspend fun getRooms(filter: RoomFilter = RoomFilter()): List<Room> {
        return api.getRooms(
            type = filter.type,
            isAvailable = filter.isAvailable,
            minPrice = filter.minPrice,
            maxPrice = filter.maxPrice,
            guests = filter.guests,
            hasExtraBed = filter.hasExtraBed,
            hasCrib = filter.hasCrib,
            hasOffer = filter.hasOffer,
            extras = filter.extras?.joinToString(","),
            sortBy = filter.sortBy,
            sortOrder = filter.sortOrder
        ).items.map { it.toDomain() }
    }
}
