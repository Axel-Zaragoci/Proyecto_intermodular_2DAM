package com.example.intermodular.views.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.RetrofitProvider
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.data.repository.ReviewRepository
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.viewmodels.BookingViewModel
import com.example.intermodular.viewmodels.BookingViewModelFactory
import com.example.intermodular.viewmodels.RoomDetailViewModel
import com.example.intermodular.viewmodels.RoomDetailViewModelFactory
import com.example.intermodular.viewmodels.RoomViewModel
import com.example.intermodular.viewmodels.RoomViewModelFactory
import com.example.intermodular.views.screens.BookingScreen
import com.example.intermodular.views.screens.RoomDetailScreen
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

            BookingScreen(
                viewModel = viewModel
            )
        }
        composable(Routes.Rooms.route) {
            val api = RetrofitProvider.api
            val roomRepository = RoomRepository(api)
            val reviewRepository = ReviewRepository(api)
            
             val viewModel: RoomViewModel = viewModel(
                factory = RoomViewModelFactory(roomRepository, reviewRepository)
            )

            RoomScreen(
                roomViewModel = viewModel,
                onNavigateToDetail = { roomId: String ->
                    navigationController.navigate(Routes.RoomDetail.createRoute(roomId))
                }
            )
        }
        composable(Routes.User.route) {
            UserScreen()
        }
        composable(
            route = Routes.RoomDetail.route,
            arguments = listOf(navArgument("roomId") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: return@composable
            val api = RetrofitProvider.api
            val roomRepository = RoomRepository(api)
            val reviewRepository = ReviewRepository(api)

            val viewModel: RoomDetailViewModel = viewModel(
                factory = RoomDetailViewModelFactory(roomRepository, reviewRepository)
            )

            RoomDetailScreen(
                roomId = roomId,
                viewModel = viewModel
            )
        }
    }
}
