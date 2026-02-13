package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.intermodular.BuildConfig
import com.example.intermodular.models.Booking
import com.example.intermodular.models.Room
import com.example.intermodular.viewmodels.NewBookingViewModel
import com.example.intermodular.views.components.BookingDataForm
import com.example.intermodular.views.components.PaymentPopup

@Composable
fun NewBookingScreen(
    loading : Boolean,
    error : String?,
    room : Room?,
    guests : String,
    startDate : Long?,
    endDate : Long?,
    totalPrice: Int?,
    newBooking: Boolean,
    mostrarPopup : Boolean,
    mensajePopup : String,
    onPopupDismiss: () -> Unit,
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

            val relativePath = if (room?.mainImage?.startsWith("/") == true)
                room.mainImage.substring(1)
            else
                room?.mainImage

            val imageUrl = if (room?.mainImage?.startsWith("http") == true)
                room.mainImage
            else
                "${BuildConfig.BASE_URL}$relativePath"

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de la habitación",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                error = ColorPainter(MaterialTheme.colorScheme.errorContainer)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp)
            ) {
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
        }

        if (newBooking) {
            Text(
                text = "Nueva reserva creada",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        PaymentPopup(
            mostrar = mostrarPopup,
            mensaje = mensajePopup,
            onDismiss = onPopupDismiss
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
    val selectedStartDate by viewModel.StartDate.collectAsStateWithLifecycle()
    val selectedEndDate by viewModel.EndDate.collectAsStateWithLifecycle()
    val totalPrice by viewModel.totalPrice.collectAsStateWithLifecycle()
    val newBooking by viewModel.bookingCreated.collectAsStateWithLifecycle()
    val mostrarPopup by viewModel.mostrarPopup.collectAsStateWithLifecycle()
    val mensajePopup by viewModel.mensajePopup.collectAsStateWithLifecycle()
    val room by viewModel.Room.collectAsStateWithLifecycle()

    NewBookingScreen(
        loading = isLoading,
        error = error,
        room = room,
        guests = guests,
        startDate = selectedStartDate,
        endDate = selectedEndDate,
        totalPrice = totalPrice,
        newBooking = newBooking,
        mostrarPopup = mostrarPopup,
        mensajePopup = mensajePopup,
        onPopupDismiss = {},
        onFormButtonClick = viewModel::createBooking,
        onStartDateChange = viewModel::onStartDateChange,
        onEndDateChange = viewModel::onEndDateChange,
        onGuestsChange = viewModel::onGuestsChange
    )
}