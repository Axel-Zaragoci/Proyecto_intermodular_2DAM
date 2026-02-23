package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.intermodular.models.UserModel
import com.example.intermodular.viewmodels.UserViewModel
import com.example.intermodular.views.components.BirthDateField
import java.time.format.DateTimeFormatter
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreen(
    user: UserModel,
    isSaving: Boolean,
    error: String?,
    onBack: () -> Unit,
    onSave: (firstName: String, lastName: String, email: String, dni: String, phoneNumber: Long?, birthDateIso: String, cityName: String, gender: String) -> Unit
) {
    var firstName by remember { mutableStateOf(user.firstName) }
    var lastName by remember { mutableStateOf(user.lastName) }
    var email by remember { mutableStateOf(user.email) }
    var dni by remember { mutableStateOf(user.dni) }
    var phone by remember { mutableStateOf(user.phoneNumber?.toString().orEmpty()) }
    var city by remember { mutableStateOf(user.cityName) }
    var gender by remember { mutableStateOf(user.gender) }
    var genderExpanded by remember { mutableStateOf(false) }

    var birthIso by remember {
        mutableStateOf(user.birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "Volver")
            }
            Spacer(Modifier.width(8.dp))
            Text("Editar perfil", style = MaterialTheme.typography.titleLarge)
        }

        ElevatedCard(shape = RoundedCornerShape(22.dp)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dni,
                    onValueChange = { dni = it },
                    label = { Text("DNI") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth()
                )

                BirthDateField(
                    value = birthIso,
                    onValueSelected = { birthIso = it },
                    enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Ciudad") },
                    modifier = Modifier.fillMaxWidth()
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
            }
        }

        if (!error.isNullOrBlank()) {
            Text(text = error, color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center)
        }

        Button(
            onClick = { onSave(firstName.trim(), lastName.trim(), email.trim(), dni.trim(), phone.trim().toLongOrNull(), birthIso.trim(), city.trim(), gender.trim()) },
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSaving) "Guardando" else "Guardar Cambios")
        }
    }
}

@Composable
fun UpdateProfileScreenState(
    viewModel: UserViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()
    val user = uiState.user ?: return

    var saveRequested by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading, error, saveRequested) {
        if (saveRequested && !uiState.isLoading && error == null) {
            saveRequested = false
            viewModel.refresh()
            navController.popBackStack()
        }
    }

    UpdateProfileScreen(
        user = user,
        isSaving = uiState.isLoading,
        error = error,
        onBack = { navController.popBackStack() },
        onSave = { first, last, email, dni, phone, birthIso, city, gender ->
            saveRequested = true
            viewModel.clearError()
            viewModel.updateUser(first, last, email, dni, phone, birthIso, city, gender)
        }
    )
}
