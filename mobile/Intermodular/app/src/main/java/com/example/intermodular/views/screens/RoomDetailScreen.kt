package com.example.intermodular.views.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.intermodular.BuildConfig
import com.example.intermodular.models.Room
import com.example.intermodular.viewmodels.RoomDetailViewModel
import com.example.intermodular.views.components.ReviewCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDetailScreen(
    roomId: String,
    viewModel: RoomDetailViewModel
) {
    val room by viewModel.room.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(roomId) {
        viewModel.loadRoomDetails(roomId)
        viewModel.loadReviews(roomId)
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        errorMessage != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = errorMessage ?: "Unknown error")
            }
        }
        room != null -> {
            RoomDetailContent(
                room = room!!,
                reviews = reviews,
                averageRating = viewModel.calculateAverageRating()
            )
        }
    }
}

@Composable
fun RoomDetailContent(
    room: Room,
    reviews: List<com.example.intermodular.models.Review>,
    averageRating: Double
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            ImageGallery(room = room)
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Habitación ${room.roomNumber}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = room.type,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFFFFA726)
                        )
                        Text(
                            text = String.format("%.1f", averageRating),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                PriceSection(room = room)

                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = "Detalles de la habitación",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                RoomInfoSection(room = room)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = room.description,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (room.extras.isNotEmpty()) {
                    ExtrasList(extras = room.extras)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Divider()
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Reseñas (${reviews.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (reviews.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sin reseñas aún",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(reviews) { review ->
                ReviewCard(
                    review = review,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageGallery(room: Room) {
    // Filter out empty or blank images and combine main + extra images
    val allImages = (listOf(room.mainImage) + room.extraImages)
        .filter { it.isNotBlank() }
    
    // Ensure we have at least one image
    if (allImages.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Sin imágenes disponibles")
        }
        return
    }
    
    val pagerState = rememberPagerState(pageCount = { allImages.size })

    Box {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { page ->
            val imageUrl = allImages[page]
            val relativePath = if (imageUrl.startsWith("/")) imageUrl.substring(1) else imageUrl
            val fullUrl = if (imageUrl.startsWith("http")) imageUrl else "${BuildConfig.BASE_URL}$relativePath"

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(fullUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Room Image ${page + 1}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = androidx.compose.ui.graphics.painter.ColorPainter(MaterialTheme.colorScheme.errorContainer)
            )
        }

        if (allImages.size > 1) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
            ) {
                Text(
                    text = "${pagerState.currentPage + 1}/${allImages.size}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun PriceSection(room: Room) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            if (room.offer != null && room.offer > 0) {
                Text(
                    text = "${room.pricePerNight}€/night",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                )
                val discountedPrice = (room.pricePerNight * ((1 - room.offer)/100)) + room.pricePerNight
                Text(
                    text = "${String.format("%.2f", discountedPrice)}€/noche",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${room.offer.toInt()}% OFF",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "${room.pricePerNight}€/noche",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Text(
            text = if (room.isAvailable) "Disponible" else "No disponible",
            style = MaterialTheme.typography.titleMedium,
            color = if (room.isAvailable) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RoomInfoSection(room: Room) {
    Column(modifier = Modifier.fillMaxWidth()) {
        InfoRow(label = "Capacidad", value = room.maxGuests.toString())
        
        if (room.extraBed) {
            InfoRow(label = "Cama Extra", value = "Sí")
        }
        
        if (room.crib) {
            InfoRow(label = "Cuna", value = "Sí")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = value,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun ExtrasList(extras: List<String>) {
    Column {
        Text(
            text = "Extras",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        extras.forEach { extra ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(6.dp)
                ) {}
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = extra,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
