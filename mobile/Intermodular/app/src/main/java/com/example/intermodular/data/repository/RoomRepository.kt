package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.mapper.toDomain
import com.example.intermodular.models.Room

class RoomRepository(
    private val api: ApiService
) {

    suspend fun getRooms(): List<Room> {
        return api.getRooms().items
            .map { it.toDomain() }
    }
}
