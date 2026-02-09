package com.example.intermodular.models

data class RoomFilter(
    val type: String? = null,
    val isAvailable: Boolean? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val guests: Int? = null,
    val hasExtraBed: Boolean? = null,
    val hasCrib: Boolean? = null,
    val hasOffer: Boolean? = null,
    val extras: List<String>? = null,
    val sortBy: String? = "roomNumber",
    val sortOrder: String? = "asc"
)
