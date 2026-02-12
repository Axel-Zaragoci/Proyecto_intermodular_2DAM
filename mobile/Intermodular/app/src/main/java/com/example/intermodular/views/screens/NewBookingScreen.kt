package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.intermodular.models.Booking
import com.example.intermodular.viewmodels.NewBookingViewModel
import com.example.intermodular.views.components.BookingDataForm

@Composable
fun NewBookingScreen(
    loading : Boolean,
    error : String?,
    guests : String,
    startDate : Long?,
    endDate : Long?,
    totalPrice: Int?,
    newBooking: Boolean,
    onFormButtonClick: () -> Unit,
    onStartDateChange: (Long?) -> Unit,
    onEndDateChange: (Long?) -> Unit,
    onGuestsChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        if (!newBooking) {
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

            BookingDataForm(
                create = true,
                startDate = startDate!!,
                endDate = endDate!!,
                guests = guests,
                totalPrice = totalPrice,
                onButtonClick = onFormButtonClick,
                onStartDateChange = onStartDateChange,
                onEndDateChange = onEndDateChange,
                onGuestsDataChange = onGuestsChange
            )
        }

        if (newBooking) {
            Text(
                text = "Nueva reserva creada",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun NewBookingState(
    viewModel: NewBookingViewModel
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()

    val guests by viewModel.Guests.collectAsStateWithLifecycle()
    val selectedStartDate by viewModel.StartDate.collectAsStateWithLifecycle()
    val selectedEndDate by viewModel.EndDate.collectAsStateWithLifecycle()
    val totalPrice by viewModel.totalPrice.collectAsStateWithLifecycle()
    val newBooking by viewModel.bookingCreated.collectAsStateWithLifecycle()

    NewBookingScreen(
        loading = isLoading,
        error = error,
        guests = guests,
        startDate = selectedStartDate,
        endDate = selectedEndDate,
        totalPrice = totalPrice,
        newBooking = newBooking,
        onFormButtonClick = viewModel::createBooking,
        onStartDateChange = viewModel::onStartDateChange,
        onEndDateChange = viewModel::onEndDateChange,
        onGuestsChange = viewModel::onGuestsChange
    )
}