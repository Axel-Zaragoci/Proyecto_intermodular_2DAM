package com.example.intermodular.views.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.RetrofitProvider
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.viewmodels.BookingViewModel
import com.example.intermodular.viewmodels.BookingViewModelFactory
import com.example.intermodular.views.screens.BookingScreen
import com.example.intermodular.views.screens.RoomScreen
import com.example.intermodular.views.screens.UserScreen
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.viewmodels.RoomViewModel
import com.example.intermodular.viewmodels.RoomViewModelFactory

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
            val api = RetrofitProvider.api
            val repository = BookingRepository(api)

            val viewModel: BookingViewModel = viewModel(
                factory = BookingViewModelFactory(repository)
            )

            BookingScreen(
                viewModel = viewModel
            )
        }
        composable(Routes.Rooms.route) {
            val api = RetrofitProvider.api
            val repository = RoomRepository(api)
            
             val viewModel: RoomViewModel = viewModel(
                factory = RoomViewModelFactory(repository)
            )

            RoomScreen(
                roomViewModel = viewModel
            )
        }
        composable(Routes.User.route) {
            UserScreen()
        }
    }
}