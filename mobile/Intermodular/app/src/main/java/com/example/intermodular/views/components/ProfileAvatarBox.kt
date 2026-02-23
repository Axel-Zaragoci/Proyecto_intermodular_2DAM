package com.example.intermodular.views.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.intermodular.BuildConfig
import com.example.intermodular.BuildConfig.BASE_URL

/**
 * Avatar de perfil con:
 * - Imagen remota si existe ([AsyncImage])
 * - Iniciales como fallback si no hay imagen
 * - Botón flotante para editar/cambiar foto
 * - Badge VIP (estrella) si el usuario tiene estado VIP
 *
 * La URL final de la imagen se construye con [BuildConfig.BASE_URL] cuando la ruta es relativa.
 *
 * @author Ian Rodriguez
 *
 * @param imageRoute - Ruta/URL de la imagen del usuario (puede ser relativa o absoluta)
 * @param initials - Iniciales del usuario a mostrar cuando no hay imagen
 * @param isVip - Indica si se debe mostrar el distintivo VIP
 * @param onEditPhoto - Callback al pulsar el botón de editar foto
 */
@Composable
fun ProfileAvatar(
    imageRoute: String?,
    initials: String,
    isVip: Boolean,
    onEditPhoto: () -> Unit
) {
    val context = LocalContext.current

    // Construcción de la URL final (absoluta) para cargar la imagen
    val imageUrl = imageRoute?.let { route ->
        val cleanPath =
            if (route.startsWith("/")) route.substring(1) else route

        if (route.startsWith("http"))
            route
        else
            "${BuildConfig.BASE_URL}$cleanPath"
    }

    Box(contentAlignment = Alignment.Center) {
        // Contenedor del avatar (imagen o iniciales)
        Surface(
            shape = CircleShape,
            tonalElevation = 6.dp,
            modifier = Modifier.size(96.dp)
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Foto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)
                )
            } else {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = initials,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }

        // Botón flotante para editar/cambiar foto
        Surface(
            shape = CircleShape,
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.TopStart)
        ) {
            Box(contentAlignment = Alignment.Center) {
                IconButton(onClick = onEditPhoto, modifier = Modifier.size(30.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Cambiar foto",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Badge VIP
        if (isVip) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}