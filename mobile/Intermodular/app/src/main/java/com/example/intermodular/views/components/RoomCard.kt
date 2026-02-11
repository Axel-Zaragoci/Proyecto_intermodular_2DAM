package com.example.intermodular.views.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.intermodular.models.Room
import com.example.intermodular.BuildConfig

@Composable
fun RoomCard(
    room: Room,
    averageRating: Double? = null,
    onRoomClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onRoomClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            val imageUrl = room.mainImage
            val relativePath = if (imageUrl.startsWith("/")) imageUrl.substring(1) else imageUrl
            val fullUrl = if (imageUrl.startsWith("http")) imageUrl else "${BuildConfig.BASE_URL}$relativePath"

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(fullUrl)
                    .crossfade(true)
                    .listener(
                        onError = { _, result ->
                            android.util.Log.e("RoomCard", "Error loading image: $imageUrl", result.throwable)
                        },
                        onSuccess = { _, _ ->
                            android.util.Log.d("RoomCard", "Image loaded successfully: $fullUrl")
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

            Column(modifier = Modifier.padding(16.dp)) {
                // Room Number and Type with Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Habitación ${room.roomNumber} - ${room.type}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (averageRating != null && averageRating > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                modifier = Modifier.size(18.dp),
                                tint = Color(0xFFFFA726)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = String.format("%.1f", averageRating),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))

                // Price and Availability
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        if (room.offer != null && room.offer > 0) {
                            Text(
                                text = "${room.pricePerNight}€/noche",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                            val discountedPrice = (room.pricePerNight * ((1 - room.offer)/100)) + room.pricePerNight
                            Text(
                                text = "${String.format("%.2f", discountedPrice)}€/noche",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = "${room.pricePerNight}€/noche",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Text(
                        text = if (room.isAvailable) "Disponible" else "No disponible",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (room.isAvailable) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Description (truncated)
                Text(
                    text = room.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = onRoomClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Más Detalles")
                }
            }
        }
    }
}
