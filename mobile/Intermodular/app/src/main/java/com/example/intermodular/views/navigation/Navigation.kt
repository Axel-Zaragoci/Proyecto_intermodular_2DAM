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
import com.example.intermodular.viewmodels.viewModelFacotry.BookingViewModelFactory
import com.example.intermodular.viewmodels.MyBookingsViewModel
import com.example.intermodular.viewmodels.viewModelFacotry.MyBookingsViewModelFactory
import com.example.intermodular.views.screens.BookingScreenState
import com.example.intermodular.views.screens.MyBookingsScreenState
import com.example.intermodular.views.screens.RoomScreen
import com.example.intermodular.views.screens.UserScreen
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.viewmodels.NewBookingViewModel
import com.example.intermodular.viewmodels.RoomViewModel
import com.example.intermodular.viewmodels.viewModelFacotry.NewBookingViewModelFactory
import com.example.intermodular.viewmodels.viewModelFacotry.RoomViewModelFactory
import com.example.intermodular.views.screens.NewBookingState

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
            val bookingRepository = BookingRepository(api)
            val roomRepository = RoomRepository(api)

            val viewModel: BookingViewModel = viewModel(
                factory = BookingViewModelFactory(bookingRepository, roomRepository)
            )

            BookingScreenState(
                viewModel = viewModel,
                navController = navigationController
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
            UserScreen(navigationController)
        }
        composable(Routes.MyBookings.route) {
            val api = RetrofitProvider.api
            val bookingRepository = BookingRepository(api)
            val roomRepository = RoomRepository(api)

            val viewModel : MyBookingsViewModel = viewModel(
                factory = MyBookingsViewModelFactory(bookingRepository, roomRepository)
            )

            MyBookingsScreenState(
                viewModel = viewModel
            )
        }

        composable(
            route = Routes.BookRoom.route,
            arguments = listOf(
                navArgument("roomId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val roomId = backStackEntry.arguments?.getString("roomId")!!

            val api = RetrofitProvider.api
            val bookingRepository = BookingRepository(api)
            val roomRepository = RoomRepository(api)

            val viewModel: NewBookingViewModel = viewModel(
                factory = NewBookingViewModelFactory(bookingRepository, roomRepository, roomId)
            )

            NewBookingState(viewModel)
        }
    }
}