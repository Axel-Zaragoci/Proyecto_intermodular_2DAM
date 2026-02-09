package com.example.intermodular.models

data class Room(
    val id: String,
    val type: String,
    val roomNumber: String,
    val maxGuests: Int,
    val description: String,
    val mainImage: String,
    val pricePerNight: Int,
    val extraBed: Boolean,
    val crib: Boolean,
    val offer: Double?,
    val extras: List<String>,
    val extraImages: List<String>,
    val isAvailable: Boolean,
    val rate: Int
)
