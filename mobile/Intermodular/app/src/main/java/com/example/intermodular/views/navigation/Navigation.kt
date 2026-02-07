package com.example.intermodular.views.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.intermodular.views.screens.BookingScreen
import com.example.intermodular.views.screens.RoomScreen
import com.example.intermodular.views.screens.UserScreen

@Composable
fun Navigation(
    navigationController: NavHostController,
    modifier: Modifier
) {
    NavHost (
        navController = navigationController,
        startDestination = Routes.Bookings.route,
        modifier = modifier
    ) {
        composable(Routes.Bookings.route) {
            BookingScreen()
        }
        composable(Routes.Rooms.route) {
            RoomScreen()
        }
        composable(Routes.User.route) {
            UserScreen()
        }
    }
}