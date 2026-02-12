package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.intermodular.views.components.InformationComponent

@Composable
fun MyBookingDetailsScreen(
    loading : Boolean,
    error : String?,
    booking : Booking?,
    room : Room?
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val relativePath = if (room?.mainImage?.startsWith("/") == true) room.mainImage.substring(1) else room?.mainImage
            val imageUrl = if (room?.mainImage?.startsWith("http") == true) room.mainImage else "${BuildConfig.BASE_URL}$relativePath"
            // Log the URL for debugging
            android.util.Log.d("RoomCard", "Loading image: $imageUrl")

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .listener(
                        onError = { _, result ->
                            android.util.Log.e("RoomCard", "Error loading image: $imageUrl", result.throwable)
                        },
                        onSuccess = { _, _ ->
                            android.util.Log.d("RoomCard", "Image loaded successfully: $imageUrl")
                        }
                    )
                    .build(),
                contentDescription = "Room Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                error = androidx.compose.ui.graphics.painter.ColorPainter(MaterialTheme.colorScheme.errorContainer) // Placeholder for error
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
                    value = "Nº " + room?.roomNumber
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
    val booking by viewModel.booking.collectAsStateWithLifecycle()
    val loading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()
    val room by viewModel.room.collectAsStateWithLifecycle()


    MyBookingDetailsScreen(
        loading = loading,
        error = error,
        booking = booking,
        room = room
    )
}