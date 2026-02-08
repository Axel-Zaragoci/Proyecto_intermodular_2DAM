package com.example.intermodular.views.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.intermodular.data.remote.RetrofitProvider
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.viewmodels.BookingViewModel
import com.example.intermodular.viewmodels.MyBookingDetailsViewModel
import com.example.intermodular.viewmodels.viewModelFacotry.BookingViewModelFactory
import com.example.intermodular.viewmodels.MyBookingsViewModel
import com.example.intermodular.viewmodels.viewModelFacotry.MyBookingDetailsViewModelFactory
import com.example.intermodular.viewmodels.viewModelFacotry.MyBookingsViewModelFactory
import com.example.intermodular.views.screens.BookingScreenState
import com.example.intermodular.views.screens.MyBookingDetailsState
import com.example.intermodular.views.screens.MyBookingsScreenState
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
            val api = RetrofitProvider.api
            val repository = BookingRepository(api)

            val viewModel: BookingViewModel = viewModel(
                factory = BookingViewModelFactory(repository)
            )

            BookingScreenState(
                viewModel = viewModel
            )
        }
        composable(Routes.Rooms.route) {
            RoomScreen()
        }
        composable(Routes.User.route) {
            UserScreen(navigationController)
        }
        composable(Routes.MyBookings.route) {
            val api = RetrofitProvider.api
            val repository = BookingRepository(api)

            val viewModel : MyBookingsViewModel = viewModel(
                factory = MyBookingsViewModelFactory(repository)
            )

            MyBookingsScreenState(
                viewModel = viewModel,
                navController = navigationController
            )
        }

        composable(
            route = Routes.MyBookingDetails.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getString("id")!!

            val api = RetrofitProvider.api
            val repository = BookingRepository(api)

            val viewModel: MyBookingDetailsViewModel = viewModel(
                factory = MyBookingDetailsViewModelFactory(id, repository)
            )

            MyBookingDetailsState(viewModel)
        }
    }
}