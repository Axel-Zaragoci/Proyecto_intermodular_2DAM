package com.example.intermodular.views.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.intermodular.R
import com.example.intermodular.data.remote.auth.SessionManager
import com.example.intermodular.viewmodels.RegisterViewModel
import androidx.compose.material.icons.filled.DateRange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit,
    onNavToLogin: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var cityName by remember { mutableStateOf("") }

    var gender by remember { mutableStateOf("Hombre") }
    var genderExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading, error) {
        if (!isLoading && error == null && !SessionManager.getToken().isNullOrBlank()) {
            onRegisterSuccess()
        }
    }

    val edge = Color(0xFF0F2854)
    val center = Color(0xFF1C4D8D)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(center, edge),
                    center = Offset.Unspecified,
                    radius = Float.POSITIVE_INFINITY
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.hotellogo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth,
            alpha = 0.25f
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(25.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Color.Black.copy(alpha = 0.2f),
                        spotColor = Color.Black.copy(alpha = 0.2f)
                    )
                    .background(
                        color = Color.White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(25.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Registro",
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally),
                    tint = Color.White
                )

                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("Nombre*") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )

                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Apellidos*") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email*") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña*") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )

                TextField(
                    value = dni,
                    onValueChange = { dni = it },
                    label = { Text("DNI*") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )

                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Teléfono (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )

                BirthDateField(
                    value = birthDate,
                    onValueSelected = { birthDate = it },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = cityName,
                    onValueChange = { cityName = it },
                    label = { Text("Ciudad*") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )

                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = !genderExpanded }
                ) {
                    TextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Género*") },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        enabled = !isLoading
                    )

                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        listOf("Hombre", "Mujer", "Otro").forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    gender = option
                                    genderExpanded = false
                                }
                            )
                        }
                    }
                }

                if (error != null) {
                    Text(
                        text = error!!,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = {
                        viewModel.onRegisterClick(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password,
                            dni = dni,
                            phoneNumber = phoneNumber,
                            birthDate = birthDate,
                            cityName = cityName,
                            gender = gender
                        )
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .width(180.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("Creando")
                    } else {
                        Text("Registrarse")
                    }
                }
            }

            TextButton(
                onClick = onNavToLogin,
                enabled = !isLoading,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDateField(
    value: String,
    onValueSelected: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var show by remember { mutableStateOf(false) }
    val formatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val initialMillis = remember(value) {
        runCatching { formatter.parse(value)?.time }.getOrNull()
    }
    val state = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    if (show) {
        DatePickerDialog(
            onDismissRequest = { show = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { onValueSelected(formatter.format(Date(it))) }
                    show = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton(onClick = { show = false }) { Text("Cancelar") } }
        ) { DatePicker(state = state) }
    }

    TextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        enabled = enabled,
        label = { Text("Nacimiento*") },
        modifier = modifier,
        trailingIcon = {
            IconButton(onClick = { show = true }, enabled = enabled) {
                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
            }
        }
    )
}