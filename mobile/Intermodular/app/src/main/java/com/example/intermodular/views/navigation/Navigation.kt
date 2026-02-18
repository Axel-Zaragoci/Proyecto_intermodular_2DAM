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
import com.example.intermodular.viewmodels.MyBookingDetailsViewModel
import com.example.intermodular.viewmodels.NewBookingViewModel
import com.example.intermodular.viewmodels.RoomViewModel
import com.example.intermodular.viewmodels.viewModelFacotry.MyBookingDetailsViewModelFactory
import com.example.intermodular.viewmodels.viewModelFacotry.NewBookingViewModelFactory
import com.example.intermodular.viewmodels.viewModelFacotry.RoomViewModelFactory
import com.example.intermodular.views.screens.MyBookingDetailsState
import com.example.intermodular.views.screens.NewBookingState
import com.example.intermodular.viewmodels.RoomDetailViewModel

/**
 * Componente de navegación de la aplicación
 *
 * Inyección de dependencias:
 * Cada composable crea las dependencias necesarias:
 * 1. Obtiene la instancia de API de [RetrofitProvider]
 * 2. Crea los repositorios correspondientes
 * 3. Crea las fábricas de ViewModels con las dependencias
 * 4. Instancia los ViewModels usando las fábricas
 *
 * @param navigationController - Controlador de navegación para manejar las transiciones
 * @param modifier - Modificador para adaptar el padding al Scaffold
 */
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

        /**
         * Pantalla principal de búsqueda y filtrado de habitaciones.
         *
         * @author Axel Zaragoci
         *
         * - **Ruta** - [Routes.Bookings]
         * - **ViewModel** - [BookingViewModel]
         * - **Repositorios** - BookingRepository, RoomRepository
         * - **Navegación** - Puede navegar a BookRoom
         */
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
                roomViewModel = viewModel,
                onRoomClick = { roomId ->
                    navigationController.navigate(Routes.RoomDetail.createRoute(roomId))
                }
            )
        }

        composable(Routes.User.route) {
            UserScreen(navigationController)
        }

        /**
         * Pantalla que lista todas las reservas del usuario actual.
         *
         * @author Axel Zaragoci
         *
         * - **Ruta** - [Routes.MyBookings]
         * - **ViewModel** - [MyBookingsViewModel]
         * - **Repositorios** - BookingRepository, RoomRepository
         * - **Navegación** - Puede navegar a MyBookingDetails
         */
        composable(Routes.MyBookings.route) {
            val api = RetrofitProvider.api
            val bookingRepository = BookingRepository(api)
            val roomRepository = RoomRepository(api)

            val viewModel : MyBookingsViewModel = viewModel(
                factory = MyBookingsViewModelFactory(bookingRepository, roomRepository)
            )

            MyBookingsScreenState(
                viewModel = viewModel,
                navController = navigationController
            )
        }

        /**
         * Pantalla para crear una nueva reserva.
         *
         * @author Axel Zaragoci
         *
         * ## Parámetros de ruta:
         * - **roomId** - ID de la habitación a reservar (requerido)
         * - **startDate** - Fecha de entrada (timestamp)
         * - **endDate** - Fecha de salida (timestamp)
         * - **guests** - Número de huéspedes
         *
         * - **Ruta** - [Routes.BookRoom]
         * - **ViewModel** - [NewBookingViewModel]
         * - **Repositorios** - BookingRepository, RoomRepository
         */
        composable(
            route = Routes.BookRoom.route,
            arguments = listOf(
                navArgument("roomId") { type = NavType.StringType },
                navArgument("startDate") { type = NavType.LongType },
                navArgument("endDate") { type = NavType.LongType },
                navArgument("guests") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val roomId = backStackEntry.arguments?.getString("roomId")!!
            val startDate = backStackEntry.arguments?.getLong("startDate")
            val endDate = backStackEntry.arguments?.getLong("endDate")
            val guests = backStackEntry.arguments?.getString("guests")!!

            val api = RetrofitProvider.api
            val bookingRepository = BookingRepository(api)
            val roomRepository = RoomRepository(api)

            val viewModel: NewBookingViewModel = viewModel(
                factory = NewBookingViewModelFactory(bookingRepository, roomRepository, roomId, startDate!!, endDate!!, guests)
            )

            NewBookingState(viewModel)
        }

        /**
         * Pantalla de detalle y actualización de una reserva existente.
         *
         * @author Axel Zaragoci
         *
         * ## Parámetros de ruta:
         * - **bookingId** - ID de la reserva a mostrar (requerido)
         *
         * - **Ruta** - [Routes.MyBookingDetails]
         * - **ViewModel** - [MyBookingDetailsViewModel]
         * - **Repositorios** - BookingRepository, RoomRepository
         */
        composable(
            route = Routes.MyBookingDetails.route,
            arguments = listOf(
                navArgument("bookingId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId")!!

            val api = RetrofitProvider.api
            val bookingRepository = BookingRepository(api)
            val roomRepository = RoomRepository(api)

            val viewModel: MyBookingDetailsViewModel = viewModel(
                factory = MyBookingDetailsViewModelFactory(
                    bookingId,
                    bookingRepository,
                    roomRepository
                )
            )

            MyBookingDetailsState(viewModel)
        }
        
        composable(
            route = Routes.RoomDetail.route,
            arguments = listOf(
                navArgument("roomId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId")!!
            
            val api = RetrofitProvider.api
            val roomRepository = RoomRepository(api)
            val reviewRepository = com.example.intermodular.data.repository.ReviewRepository(api)
            
            val viewModel = RoomDetailViewModel(
                roomId = roomId,
                roomRepository = roomRepository,
                reviewRepository = reviewRepository
            )
            
            com.example.intermodular.views.screens.RoomDetailScreen(
                viewModel = viewModel,
                onBackClick = { navigationController.popBackStack() }
            )
        }
    }
}