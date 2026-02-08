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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.intermodular.models.Booking
import com.example.intermodular.viewmodels.BookingViewModel
import com.example.intermodular.views.components.FilterList
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    showFilters: Boolean,
    changeFilterVisibility: () -> Unit,
    loading : Boolean,
    error : String?,
    bookings : List<Booking>,
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
    filterOffer: () -> Unit
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
            items(bookings) { booking ->
                Text(
                    text = booking.id
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreenState(
    viewModel: BookingViewModel
) {
    val bookings by viewModel.bookings.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()
    val selectedStartDate by viewModel.selectedStartDate.collectAsState()
    val selectedEndDate by viewModel.selectedEndDate.collectAsState()
    val maxPrice by viewModel.maxPrice.collectAsState()
    val guests by viewModel.guests.collectAsState()
    val extraBed by viewModel.extraBed.collectAsState()
    val cradle by viewModel.cradle.collectAsState()
    val showFilters by viewModel.showFilters.collectAsState()

    BookingScreen(
        showFilters = showFilters,
        changeFilterVisibility = viewModel::changeFilterVisibility,
        loading = loading,
        error = error,
        bookings = bookings,
        selectedStartDate = selectedStartDate,
        selectedEndDate = selectedEndDate,
        onStartDateSelected = viewModel::onStartDateSelected,
        onEndDateSelected = viewModel::onEndDateSelected,
        maxPrice = maxPrice,
        onMaxPriceChanged = viewModel::onMaxPriceChanged,
        guests = guests,
        onGuestsChanged = viewModel::onGuestsChanged,
        extraBed = extraBed,
        onExtraBedCheckChanged = viewModel::onExtraBedCheckChanged,
        cradle = cradle,
        onCradleCheckChanged = viewModel::onCradleCheckChanged,
        filter = viewModel::filter,
        filterOffer = viewModel::filterOffer
    )
}