package com.example.intermodular.views.navigation

sealed class Routes(
    val route: String
) {
    object Bookings : Routes("bookings")
    object Rooms : Routes("rooms")
    object User : Routes("user")
    object MyBookings : Routes("myBookings")

    object MyBookingDetails : Routes ("details/{bookingId}") {
        fun createRoute(bookingId: String) = "details/$bookingId"
    }

    object BookRoom : Routes ("bookRoom/{roomId}?startDate={startDate}&endDate={endDate}&guests={guests}") {
        fun createRoute(roomId: String,
                        startDate : Long,
                        endDate : Long,
                        guests : String) = "bookRoom/$roomId?startDate=$startDate&endDate=$endDate&guests=$guests"
    }
}