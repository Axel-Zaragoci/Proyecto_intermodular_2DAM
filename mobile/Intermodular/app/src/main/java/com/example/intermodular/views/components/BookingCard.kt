package com.example.intermodular.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.intermodular.BuildConfig
import com.example.intermodular.models.Booking
import com.example.intermodular.models.Room

@Composable
fun BookingCard(
    booking: Booking,
    room : Room
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            val relativePath =
                if (room.mainImage.startsWith("/")) room.mainImage.substring(1) else room.mainImage
            val imageUrl =
                if (room.mainImage.startsWith("http")) room.mainImage else "${BuildConfig.BASE_URL}$relativePath"

            android.util.Log.d("Booking", "Carga de imagen: $imageUrl")

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .listener(
                        onError = { _, result ->
                            android.util.Log.e(
                                "Booking",
                                "Error cargando imagen: $imageUrl",
                                result.throwable
                            )
                        },
                        onSuccess = { _, _ ->
                            android.util.Log.d("Booking", "Imagen cargada correctamente: $imageUrl")
                        }
                    )
                    .build(),
                contentDescription = "Imagen de la habitación",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                error = androidx.compose.ui.graphics.painter.ColorPainter(MaterialTheme.colorScheme.errorContainer)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Habitación ${room.roomNumber} - ${room.type}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${booking.totalPrice}€ en total",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "${booking.guests} huéspedes",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (room.isAvailable) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InformationComponent(
                        title = "Fecha de inicio",
                        value = booking.checkInDate.dayOfMonth.toString() + "/" + booking.checkInDate.monthValue + "/" + booking.checkInDate.year
                    )

                    InformationComponent(
                        title = "Fecha de fin",
                        value = booking.checkOutDate.dayOfMonth.toString() + "/" + booking.checkOutDate.monthValue + "/" + booking.checkOutDate.year
                    )
                }
            }
        }
    }
}