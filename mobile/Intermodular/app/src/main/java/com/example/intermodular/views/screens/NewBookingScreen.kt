package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.intermodular.viewmodels.NewBookingViewModel
import com.example.intermodular.views.components.InformationComponent
import java.time.LocalDate

@Composable
fun NewBookingScreen(
    guests : String,
    startDate : LocalDate?,
    endDate : LocalDate?
) {
    Column() {
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
    val guests by viewModel.Guests.collectAsStateWithLifecycle()
    val selectedStartDate = viewModel.startDateAsLocalDate()
    val selectedEndDate = viewModel.endDateAsLocalDate()

    NewBookingScreen(
        guests,
        selectedStartDate,
        selectedEndDate
    )
}