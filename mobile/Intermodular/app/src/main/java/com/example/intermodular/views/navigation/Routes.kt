package com.example.intermodular.views.navigation

sealed class Routes(
    val route: String
) {
    object Bookings : Routes("bookings")
    object Rooms : Routes("rooms")
    object User : Routes("user")
}