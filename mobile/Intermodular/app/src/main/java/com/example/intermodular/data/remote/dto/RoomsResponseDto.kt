package com.example.intermodular.data.remote.dto

data class RoomsResponseDto(
    val items: List<RoomDto>,
    val appliedFilter: Any?,
    val sort: Map<String, Int>?
)
