package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.intermodular.models.Booking
import com.example.intermodular.viewmodels.MyBookingDetailsViewModel
import com.example.intermodular.views.components.BookingCard
import com.example.intermodular.views.components.InformationComponent

@Composable
fun MyBookingDetailsScreen(
    loading : Boolean,
    error : String?,
    booking : Booking?
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

        Row(

        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            ) {

                Text(
                    text = "Sobre la reserva: ",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                InformationComponent(
                    title = "Habitación: ",
                    value = "Nº " + booking?.roomId
                )

                InformationComponent(
                    title = "Fecha de inicio: ",
                    value = booking?.checkInDate.toString()
                )

                InformationComponent(
                    title = "Fecha de fin: ",
                    value = booking?.checkOutDate.toString()
                )

                InformationComponent(
                    title = "Huéspedes: ",
                    value = booking?.guests.toString()
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            ) {

                Text(
                    text = "Sobre el pago: ",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                InformationComponent(
                    title = "Fecha de pago: ",
                    value = booking?.payDate.toString()
                )

                InformationComponent(
                    title = "Precio pagado: ",
                    value = booking?.totalPrice.toString() + " €"
                )

                InformationComponent(
                    title = "Descuento: ",
                    value = booking?.offer.toString() + " %"
                )

                InformationComponent(
                    title = "Precio por noche: ",
                    value = booking?.pricePerNight.toString() + " €"
                )
            }
        }
    }
}

@Composable
fun MyBookingDetailsState(
    viewModel: MyBookingDetailsViewModel
) {
    val booking by viewModel.booking.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()


    MyBookingDetailsScreen(
        loading = loading,
        error = error,
        booking = booking,
    )
}