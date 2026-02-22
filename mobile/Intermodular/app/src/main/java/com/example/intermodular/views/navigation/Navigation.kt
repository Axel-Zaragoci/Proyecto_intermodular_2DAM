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
import com.example.intermodular.data.repository.LoginRepository
import com.example.intermodular.data.repository.RegisterRepository
import com.example.intermodular.viewmodels.BookingViewModel
import com.example.intermodular.viewmodels.BookingViewModelFactory
import com.example.intermodular.views.screens.BookingScreen
import com.example.intermodular.views.screens.RoomScreen
import com.example.intermodular.views.screens.UserScreen
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.viewmodels.LoginViewModel
import com.example.intermodular.viewmodels.LoginViewModelFactory
import com.example.intermodular.viewmodels.RegisterViewModel
import com.example.intermodular.viewmodels.RegisterViewModelFactory
import com.example.intermodular.viewmodels.RoomViewModel
import com.example.intermodular.viewmodels.RoomViewModelFactory
import com.example.intermodular.views.screens.LoginScreen
import com.example.intermodular.views.screens.RegisterScreen

@Composable
fun Navigation(
    navigationController: NavHostController,
    modifier: Modifier
) {
    NavHost (
        navController = navigationController,
        startDestination = Routes.Login.route,
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
        composable(Routes.Login.route) {
            val api = RetrofitProvider.api
            val repository = LoginRepository(api)
            val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(repository))


            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navigationController.navigate(Routes.Bookings.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onNavToRegister = {
                    navigationController.navigate(Routes.Register.route)
                }
            )
        }
        composable(Routes.Register.route) {
            val api = RetrofitProvider.api

            val loginRepository = LoginRepository(api)
            val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(loginRepository))
            val registerRepository = RegisterRepository(api, loginRepository)

            val viewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(registerRepository)
            )

            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navigationController.navigate(Routes.Bookings.route) {
                        popUpTo(Routes.Register.route) { inclusive = true }
                    }
                },
                onNavToLogin = {
                    navigationController.popBackStack()
                }
            )
        }
    }
}