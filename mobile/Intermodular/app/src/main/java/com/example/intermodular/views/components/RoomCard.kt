package com.example.intermodular.views.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Room Image
            // Remove leading slash from room.mainImage if present to avoid double slash with BASE_URL
            val relativePath = if (room.mainImage.startsWith("/")) room.mainImage.substring(1) else room.mainImage
            val imageUrl = if (room.mainImage.startsWith("http")) room.mainImage else "${BuildConfig.BASE_URL}$relativePath"
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

            Column(modifier = Modifier.padding(16.dp)) {
                // Room Number and Type
                Text(
                    text = "Room ${room.roomNumber} - ${room.type}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))

                // Price and Availability
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$${room.pricePerNight}/night",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                     Text(
                        text = if (room.isAvailable) "Available" else "Occupied",
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
                    onClick = { /* TODO: Navigate to details */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("More Details")
                }
            }
        }
    }
}
