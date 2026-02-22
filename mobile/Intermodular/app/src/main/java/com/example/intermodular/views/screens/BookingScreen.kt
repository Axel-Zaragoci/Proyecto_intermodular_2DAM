package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.intermodular.models.Room
import com.example.intermodular.viewmodels.BookingViewModel
import com.example.intermodular.views.components.FilterList
import com.example.intermodular.views.components.RoomCard

 /**
 * Esta pantalla está compuesta por:
 * - Un panel de filtros [FilterList] para búsqueda
 * - Un listado de habitaciones mostradas con [RoomCard] con opción de reservar
 * - Estados de carga y error
 * - Mensaje informativo cuando no hay resultados
 *
 * Flujo de UI:
 * 1. Muestra indicador de carga mientras se obtienen datos
 * 2. Muestra mensaje de error si ocurre algún problema
 * 3. Muestra panel de filtros expandible
 * 4. Muestra habitaciones disponibles en un LazyColumn
 * 5. Muestra mensaje "sin resultados" si no hay habitaciones
 *
 * @author Axel Zaragoci
 *
 * @param showFilters - Controla la visibilidad del componente de filtros
 * @param changeFilterVisibility - Callback para cambiar la visibilidad de filtros
 * @param loading - Estado de carga de datos
 * @param error - Mensaje de error a mostrar (null si no hay error)
 * @param rooms - Lista de habitaciones a mostrar
 * @param selectedStartDate - Fecha de inicio seleccionada (en milisegundos)
 * @param selectedEndDate - Fecha de fin seleccionada (en milisegundos)
 * @param onStartDateSelected - Callback al seleccionar fecha de inicio
 * @param onEndDateSelected - Callback al seleccionar fecha de fin
 * @param maxPrice - Precio máximo seleccionado para filtrar
 * @param onMaxPriceChanged - Callback al cambiar precio máximo
 * @param guests - Número de huéspedes como String
 * @param onGuestsChanged - Callback al cambiar número de huéspedes
 * @param extraBed - Estado del filtro de cama supletoria
 * @param onExtraBedCheckChanged - Callback al cambiar filtro de cama supletoria
 * @param cradle - Estado del filtro de cuna
 * @param onCradleCheckChanged - Callback al cambiar filtro de cuna
 * @param filter - Callback para aplicar filtros normales
 * @param filterOffer - Callback para aplicar filtros con ofertas
 * @param onBookButtonClick - Callback al hacer clic en "Reservar" de una habitación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    showFilters: Boolean,
    changeFilterVisibility: () -> Unit,
    loading : Boolean,
    error : String?,
    rooms : List<Room>,
    selectedStartDate : Long?,
    selectedEndDate : Long?,
    onStartDateSelected : (Long?) -> Unit,
    onEndDateSelected : (Long?)-> Unit,
    maxPrice : Int,
    onMaxPriceChanged : (Int) -> Unit,
    guests : String,
    onGuestsChanged : (String) -> Unit,
    extraBed : Boolean,
    onExtraBedCheckChanged : (Boolean) -> Unit,
    cradle : Boolean,
    onCradleCheckChanged : (Boolean) -> Unit,
    filter : () -> Unit,
    filterOffer: () -> Unit,
    onBookButtonClick: (Room) -> Unit
) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Indicador de carga
        if (loading) {
            Box (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Mostrar mensaje en caso de error
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Componente de filtros
        FilterList(
            showFilters = showFilters,
            changeVisibility = changeFilterVisibility,
            selectedStartDate = selectedStartDate,
            selectedEndDate = selectedEndDate,
            onStartDateSelected = onStartDateSelected,
            onEndDateSelected = onEndDateSelected,
            maxPrice = maxPrice,
            onMaxPriceChanged = onMaxPriceChanged,
            guests = guests,
            onGuestsChanged = onGuestsChanged,
            extraBed = extraBed,
            onExtraBedCheckChanged = onExtraBedCheckChanged,
            cradle = cradle,
            onCradleCheckChanged = onCradleCheckChanged,
            filter = filter,
            filterOffer = filterOffer
        )

        // Listado de habitaciones
        LazyColumn {
            items(rooms) { room ->
                RoomCard(
                    room = room,
                    buttonText = "Reservar",
                    onButtonClick = onBookButtonClick
                )
            }
        }

        // Mensaje cuando no hay resultados al buscar
        if (rooms.isEmpty() && !showFilters) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se encontrar habitaciones disponibles",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Versión de [BookingScreen] con estado y conectada al ViewModel
 *
 * Funciones:
 * 1. Recolectar estados del [BookingViewModel] usando [collectAsStateWithLifecycle]
 * 2. Pasar los estados a [BookingScreen]
 * 3. Manejar la navegación al hacer click en "Reservar"
 *
 * Navegación:
 * Al hacer click en "Reservar", navega a la pantalla de reservar habitación y se pasan los datos seleccionados por el usuario con la ruta:
 * "bookRoom/${room.id}?startDate=$selectedStartDate&endDate=$selectedEndDate&guests=$guests"
 *
 * @author Axel Zaragoci
 *
 * @param viewModel - ViewModel del que obtener los estados y las funciones
 * @param navController - Controlador de la navegación para ir a reservar habitación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreenState(
    viewModel: BookingViewModel,
    navController: NavHostController
) {
    // Obtener estados del ViewModel
    val rooms by viewModel.filteredRooms.collectAsStateWithLifecycle()
    val loading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()
    val selectedStartDate by viewModel.selectedStartDate.collectAsStateWithLifecycle()
    val selectedEndDate by viewModel.selectedEndDate.collectAsStateWithLifecycle()
    val maxPrice by viewModel.maxPrice.collectAsStateWithLifecycle()
    val guests by viewModel.guests.collectAsStateWithLifecycle()
    val extraBed by viewModel.extraBed.collectAsStateWithLifecycle()
    val cradle by viewModel.cradle.collectAsStateWithLifecycle()
    val showFilters by viewModel.showFilters.collectAsStateWithLifecycle()

    // Carga de la vista sin estado
    BookingScreen(
        showFilters = showFilters,
        changeFilterVisibility = viewModel::changeFilterVisibility,
        loading = loading,
        error = error,
        rooms = rooms,
        selectedStartDate = selectedStartDate,
        selectedEndDate = selectedEndDate,
        onStartDateSelected = viewModel::onStartDateSelected,
        onEndDateSelected = viewModel::onEndDateSelected,
        maxPrice = maxPrice,
        onMaxPriceChanged = viewModel::onMaxPriceChanged,
        guests = guests.toString(),
        onGuestsChanged = viewModel::onGuestsChanged,
        extraBed = extraBed,
        onExtraBedCheckChanged = viewModel::onExtraBedCheckChanged,
        cradle = cradle,
        onCradleCheckChanged = viewModel::onCradleCheckChanged,
        filter = viewModel::filter,
        filterOffer = viewModel::filterOffer,
        onBookButtonClick = { room ->
            // Navegación con parámetros
            navController.navigate("bookRoom/${room.id}?startDate=$selectedStartDate&endDate=$selectedEndDate&guests=$guests")
        }
    )
}