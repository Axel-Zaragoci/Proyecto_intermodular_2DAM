package com.example.intermodular.views.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.intermodular.BuildConfig
import com.example.intermodular.data.remote.utils.uriToMultipart
import com.example.intermodular.models.UserModel
import com.example.intermodular.viewmodels.UserViewModel
import com.example.intermodular.views.navigation.Routes
import java.time.format.DateTimeFormatter

@Composable
fun UserScreen(
    isLoading: Boolean,
    user: UserModel?,
    error: String?,
    onRetry: () -> Unit,
    onErrorShown: () -> Unit,
    onViewMyBookings: () -> Unit,
    onChangePhoto: () -> Unit,
    onEditProfile: () -> Unit
) {
    when {
        isLoading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        error != null -> {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(error)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    onErrorShown()
                    onRetry()
                }) {
                    Text("Reintentar")
                }
            }
        }

        user != null -> {

            val formattedDate = user.birthDate.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(18.dp))

                ProfileAvatar(
                    imageRoute = user.imageRoute,
                    initials = "${user.firstName.take(1)}${user.lastName.take(1)}".uppercase(),
                    isVip = user.vipStatus,
                    onChangePhoto
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "${user.firstName} ${user.lastName}".trim(),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(18.dp))

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 240.dp),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Informacion Personal",
                                style = MaterialTheme.typography.titleMedium
                            )
                            IconButton(onClick = onEditProfile) {
                                Icon(Icons.Outlined.Edit, contentDescription = "Editar perfil")
                            }
                        }

                        Divider()

                        ProfileRow(
                            icon = Icons.Outlined.Email,
                            label = "Email",
                            value = user.email
                        )
                        ProfileRow(
                            icon = Icons.Outlined.Badge,
                            label = "DNI",
                            value = user.dni
                        )
                        ProfileRow(
                            icon = Icons.Outlined.Phone,
                            label = "Teléfono",
                            value = user.phoneNumber?.toString() ?: "Desconocido"
                        )
                        ProfileRow(
                            icon = Icons.Outlined.CalendarMonth,
                            label = "Nacimiento",
                            value = formattedDate
                        )
                        ProfileRow(
                            icon = Icons.Outlined.LocationOn,
                            label = "Ciudad",
                            value = user.cityName
                        )
                        ProfileRow(
                            icon = Icons.Outlined.Person,
                            label = "Género",
                            value = user.gender
                        )
                    }
                }

                Spacer(Modifier.height(22.dp))
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onViewMyBookings
                    ) {
                        Text(
                            text = "Ver mis reservas"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileAvatar(
    imageRoute: String?,
    initials: String,
    isVip: Boolean,
    onEditPhoto: () -> Unit
) {
    val context = LocalContext.current

    val imageUrl = imageRoute?.let { route ->
        val cleanPath =
            if (route.startsWith("/")) route.substring(1) else route

        if (route.startsWith("http"))
            route
        else
            "${BuildConfig.BASE_URL}$cleanPath"
    }

    Box(contentAlignment = Alignment.Center) {
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

        Surface(
            shape = CircleShape,
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(30.dp)
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

        if (isVip) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
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

@Composable
private fun ProfileRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 2.dp,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.labelMedium)
            Text(
                text = value.ifBlank { "-" },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun UserScreenState(
    viewModel: UserViewModel,
    navController: NavController
) {
    val navigation = navController
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val part = uriToMultipart(context, uri, partName = "photo")
            viewModel.updatePhoto(part)
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refresh()
    }

    UserScreen(
        isLoading = uiState.isLoading,
        user = uiState.user,
        error = error,
        onRetry = { viewModel.refresh() },
        onErrorShown = { viewModel.clearError() },
        onViewMyBookings = { navigation.navigate(Routes.MyBookings.route) },
        onChangePhoto = { pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
        onEditProfile = { navigation.navigate(Routes.UpdateProfile.route) }
    )
}