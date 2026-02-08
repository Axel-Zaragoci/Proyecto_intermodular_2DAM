package com.example.intermodular.data.remote.dto

data class RoomDto(
    val _id: String,
    val type: String,
    val roomNumber: String,
    val maxGuests: Int,
    val description: String,
    val mainImage: String,
    val pricePerNight: Int,
    val extraBed: Boolean,
    val crib: Boolean,
    val offer: Int,
    val extras: List<String>,
    val extraImages: List<String>,
    val isAvailable: Boolean,
    val rate: Int
)
