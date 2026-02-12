package com.example.intermodular.views.navigation

sealed class Routes(
    val route: String
) {
    object Bookings : Routes("bookings")
    object Rooms : Routes("rooms")
    object User : Routes("user")
    object MyBookings : Routes("myBookings")
    object MyBookingDetails : Routes ("details/{id}") {
        fun createRoute(bookingId: String) = "details/$bookingId"
    }

    object BookRoom : Routes ("bookRoom/{roomId}") {
        fun createRoute(roomId: String) = "bookRoom/$roomId"
    }
}