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

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
        }

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

        LazyColumn {
            items(rooms) { room ->
                RoomCard(
                    room = room,
                    buttonText = "Reservar",
                    onButtonClick = onBookButtonClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreenState(
    viewModel: BookingViewModel,
    navController: NavHostController
) {
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
            navController.navigate("bookRoom/${room.id}")
        }
    )
}