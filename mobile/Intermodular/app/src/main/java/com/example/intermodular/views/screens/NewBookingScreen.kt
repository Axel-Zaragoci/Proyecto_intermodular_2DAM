package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.intermodular.models.Room
import com.example.intermodular.viewmodels.NewBookingViewModel
import com.example.intermodular.views.components.InformationComponent
import java.time.LocalDate

@Composable
fun NewBookingScreen(
    loading : Boolean,
    error : String?,
    guests : String,
    startDate : LocalDate?,
    endDate : LocalDate?,
    room : Room?
) {
    Column() {
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

        InformationComponent(
            title = "Huéspedes: ",
            value = guests
        )

        InformationComponent(
            title = "Fecha de inicio: ",
            value = "" + startDate?.dayOfMonth + "/" + startDate?.monthValue + "/" + startDate?.year
        )

        InformationComponent(
            title = "Fecha de fin: ",
            value = "" + endDate?.dayOfMonth + "/" + endDate?.monthValue + "/" + endDate?.year
        )
    }
}

@Composable
fun NewBookingState(
    viewModel: NewBookingViewModel
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()

    val guests by viewModel.Guests.collectAsStateWithLifecycle()
    val selectedStartDate = viewModel.startDateAsLocalDate()
    val selectedEndDate = viewModel.endDateAsLocalDate()
    val room by viewModel.Room.collectAsStateWithLifecycle()

    NewBookingScreen(
        loading = isLoading,
        error = error,
        guests = guests,
        startDate = selectedStartDate,
        endDate = selectedEndDate,
        room = room
    )
}