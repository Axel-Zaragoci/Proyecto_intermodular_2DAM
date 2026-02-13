package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.intermodular.viewmodels.MyBookingDetailsViewModel
import com.example.intermodular.views.components.BookingDataForm

@Composable
fun MyBookingDetailsScreen(
    loading: Boolean,
    error: String?,
    room: Room?,
    status: String?,
    booking: Booking?,
    startDate: Long?,
    endDate: Long?,
    onUpdateClick: () -> Unit,
    onCancelClick: () -> Unit,
    onStartDateChange: (Long?) -> Unit,
    onEndDateChange: (Long?) -> Unit,
    onGuestsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Habitación nº${room?.roomNumber}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Estado: $status",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            BookingDataForm(
                create = false,
                startDate = startDate ?: 0L,
                endDate = endDate ?: 0L,
                guests = booking?.guests?.toString() ?: "",
                totalPrice = booking?.totalPrice,
                onButtonClick = onUpdateClick,
                onStartDateChange = onStartDateChange,
                onEndDateChange = onEndDateChange,
                onGuestsDataChange = onGuestsChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCancelClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar reserva")
            }
        }
    }
}

@Composable
fun MyBookingDetailsState(
    viewModel: MyBookingDetailsViewModel
) {
    val loading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()
    val room by viewModel.room.collectAsStateWithLifecycle()
    val booking by viewModel.booking.collectAsStateWithLifecycle()

    MyBookingDetailsScreen(
        loading = loading,
        error = error,
        room = room,
        status = booking?.status,
        booking = booking,
        startDate = viewModel.checkInDateToMilliseconds(),
        endDate = viewModel.checkOutDateToMilliseconds(),
        onUpdateClick = viewModel::updateBooking,
        onCancelClick = viewModel::cancelBooking,
        onStartDateChange = viewModel::onStartDateChange,
        onEndDateChange = viewModel::onEndDateChange,
        onGuestsChange = viewModel::onGuestsChange
    )
}